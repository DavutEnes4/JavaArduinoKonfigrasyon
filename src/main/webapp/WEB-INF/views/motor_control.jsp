<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="tr">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Motor Kontrol Paneli</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        .btn-kontrol { width: 120px; height: 80px; font-size: 1.2rem; margin: 10px; }
        .joypad-area { border: 2px dashed #ccc; border-radius: 10px; padding: 20px; margin-top: 20px; }
        .tab-content { margin-top: 20px; }
    </style>
</head>
<body class="bg-light">
<div class="container mt-4">
    <h3 class="text-center">Motor Kontrol Paneli</h3>

    <!-- Port Seçimi -->
    <div class="d-flex flex-column align-items-center w-50 mx-auto">
        <select id="port" class="form-select mb-2">
            <option value="">Port seçiniz</option>
        </select>
        <input type="number" id="baudrate" class="form-control" placeholder="Baudrate" value="9600"/>
    </div>

    <!-- Sekmeler -->
    <ul class="nav nav-tabs justify-content-center" id="kontrolTabs" role="tablist">
        <li class="nav-item" role="presentation">
            <button class="nav-link active" id="buton-tab" data-bs-toggle="tab" data-bs-target="#buton" type="button" role="tab">Buton ile Kontrol</button>
        </li>
        <li class="nav-item" role="presentation">
            <button class="nav-link" id="joypad-tab" data-bs-toggle="tab" data-bs-target="#joypad" type="button" role="tab">Joypad ile Kontrol</button>
        </li>
    </ul>

    <!-- Sekme İçerikleri -->
    <div class="tab-content" id="kontrolTabContent">
        <div class="tab-pane fade show active text-center" id="buton" role="tabpanel">
            <div><button class="btn btn-primary btn-kontrol" onclick="gonder('ILERI')">↑ İleri</button></div>
            <div>
                <button class="btn btn-warning btn-kontrol" onclick="gonder('SOL')">← Sol</button>
                <button class="btn btn-secondary btn-kontrol" onclick="gonder('DUR')">■ Dur</button>
                <button class="btn btn-warning btn-kontrol" onclick="gonder('SAG')">Sağ →</button>
            </div>
            <div><button class="btn btn-danger btn-kontrol" onclick="gonder('GERI')">↓ Geri</button></div>
        </div>
        <div class="tab-pane fade text-center" id="joypad" role="tabpanel">
            <div class="joypad-area">
                <p><strong>Yön Tuşlarını</strong> kullanarak motorları kontrol edebilirsiniz.</p>
                <p><kbd>↑</kbd> ileri, <kbd>↓</kbd> geri, <kbd>←</kbd> sol, <kbd>→</kbd> sağ, <kbd>boşluk</kbd> durdur.</p>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap Bundle (Popper + JS) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Servis mapping ile uyumlu URL
    const getPortsUrl = "${pageContext.request.contextPath}/motor/get_ports";

    // Port listesini çek
    window.addEventListener('DOMContentLoaded', () => {
        fetch(getPortsUrl)
            .then(res => res.json())
            .then(ports => {
                const select = document.getElementById('port');
                select.innerHTML = '<option value="">Port seçiniz</option>';
                if (!ports.length) {
                    select.innerHTML += '<option disabled>Hiç port bulunamadı</option>';
                }
                ports.forEach(p => {
                    const opt = document.createElement('option');
                    opt.value = p.device;
                    opt.textContent = p.description;
                    select.appendChild(opt);
                });
            })
            .catch(err => console.error('Port listesi hatası:', err));
    });

    // Komut haritası
    const komutKarsiliklari = { ILERI: 'I', GERI: 'G', SOL: 'L', SAG: 'R', DUR: 'S' };
    function gonder(cmd) {
        const port = document.getElementById('port').value;
        const baudrate = +document.getElementById('baudrate').value;
        const char = komutKarsiliklari[cmd];
        if (!port) { alert('Port seçiniz!'); return; }
        fetch(`${pageContext.request.contextPath}/motor/kontrol_komut`, {
            method: 'POST', headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ komut: char, port, baudrate })
        })
            .then(r => r.json())
            .then(j => { if (j.status !== 'ok') alert('Hata: ' + j.message); })
            .catch(e => console.error('Komut hatası:', e));
    }

    document.addEventListener('keydown', e => {
        if (!document.getElementById('joypad-tab').classList.contains('active')) return;
        const keyMap = { ArrowUp: 'ILERI', ArrowDown: 'GERI', ArrowLeft: 'SOL', ArrowRight: 'SAG', ' ': 'DUR' };
        if (keyMap[e.key]) { gonder(keyMap[e.key]); e.preventDefault(); }
    });
</script>
</body>
</html>
