<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="tr">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Kart Sohbeti</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { padding-top: 2rem; background: #f8f9fa; }
        .chat-box { max-height: 400px; overflow-y: auto; background: #fff; padding: 1rem; border: 1px solid #ddd; border-radius: .25rem; }
        .chat-entry { margin-bottom: .75rem; }
        .chat-entry.user .message { text-align: right; }
        .message { display: inline-block; padding: .5rem 1rem; border-radius: 1rem; }
        .user .message { background: #0d6efd; color: #fff; }
        .bot .message { background: #e9ecef; color: #000; }
    </style>
</head>
<body>
<div class="container">
    <h2 class="mb-4 text-center">Kart Sohbeti</h2>
    <div class="row mb-3">
        <div class="col-md-4">
            <select id="card-select" class="form-select">
                <option value="">Kart seçiniz</option>
            </select>
        </div>
    </div>
    <div id="details" class="mb-4"></div>
    <div class="chat-box mb-3" id="chat-box"></div>
    <form id="chat-form" class="d-flex">
        <input type="text" id="question" class="form-control me-2" placeholder="Sorunuzu yazın..." disabled />
        <button type="submit" class="btn btn-primary" disabled>Gönder</button>
    </form>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    const contextPath = '${pageContext.request.contextPath}';
    const select = document.getElementById('card-select');
    const details = document.getElementById('details');
    const chatBox = document.getElementById('chat-box');
    const form = document.getElementById('chat-form');
    const questionInput = document.getElementById('question');
    const submitBtn = form.querySelector('button');

    // Kart listesini çek
    function loadCard(name) {
        if (!name) return;
    const url = contextPath + '/api/cards/';
    fetch(url)
        .then(r => r.json())
        .then(list => list.forEach(name => {
            const opt = document.createElement('option');
            opt.value = name;
            opt.textContent = name;
            select.appendChild(opt);
        }));

    // Kart seçildiğinde detayları göster
    select.addEventListener('change', () => {
        const name = select.value;
        chatBox.innerHTML = '';
        if (!name) {
            details.innerHTML = '';
            questionInput.disabled = true;
            submitBtn.disabled = true;
            return;
        }
        const url = contextPath + '/api/cards/' + encodeURIComponent(name);
        fetch(url)
            .then(r => r.json())
            .then(card => {
                let html = `<h5>${card.adi}</h5><p>${card.aciklamasi}</p><ul>`;
                card.pinList.forEach(p => {
                    html += `<li><strong>${p.adi}</strong>: ${p.aciklamasi}</li>`;
                });
                html += '</ul>';
                details.innerHTML = html;
                questionInput.disabled = false;
                submitBtn.disabled = false;
            });
    });

    // Form gönderildiğinde chat API çağrısı
    form.addEventListener('submit', e => {
        e.preventDefault();
        const question = questionInput.value.trim();
        const name = select.value;
        if (!question || !name) return;
        // Kullanıcı mesajı kutuya ekle
        appendMessage('user', question);
        questionInput.value = '';
        const url = contextPath + '/api/cards/' + encodeURIComponent(name)+ '/chat';
        fetch(url, {
            method: 'POST',
            headers: {'Content-Type':'application/json'},
            body: JSON.stringify({question})
        })
            .then(r => r.text())
            .then(response => appendMessage('bot', response))
            .catch(() => appendMessage('bot', 'Sunucu hatası'));
    });

    function appendMessage(who, text) {
        const div = document.createElement('div');
        div.classList.add('chat-entry', who);
        const msg = document.createElement('div');
        msg.classList.add('message');
        msg.textContent = text;
        div.appendChild(msg);
        chatBox.appendChild(div);
        chatBox.scrollTop = chatBox.scrollHeight;
    }
</script>
</body>
</html>
