<%--
  Created by IntelliJ IDEA.
  User: Decoder
  Date: 19.05.2025
  Time: 04:15
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="${pageContext.response.locale.language}">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title><spring:message code="motor.title"/></title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        .btn-kontrol { width: 120px; height: 80px; font-size: 1.2rem; margin: 10px; }
        .joypad-area { border: 2px dashed #ccc; border-radius: 10px; padding: 20px; margin-top: 20px; }
        .tab-content { margin-top: 20px; }
    </style>
</head>
<body class="bg-light">
<fmt:setLocale value="${pageContext.response.locale.language}"/>
<fmt:bundle basename="messages">
    <div class="container mt-4">
        <h3 class="text-center"><fmt:message key="motor.title"/></h3>

        <!-- Port Seçimi -->
        <div class="d-flex flex-column align-items-center w-50 mx-auto">
            <select id="port" class="form-select mb-2">
                <option value=""><fmt:message key="motor.selectPort"/></option>
            </select>
            <input type="number" id="baudrate" class="form-control"
                   placeholder="<fmt:message key='motor.baudrate'/>"
                   value="9600"/>
        </div>

        <!-- Sekmeler -->
        <ul class="nav nav-tabs justify-content-center" id="kontrolTabs" role="tablist">
            <li class="nav-item" role="presentation">
                <button class="nav-link active" id="buton-tab" data-bs-toggle="tab" data-bs-target="#buton" type="button" role="tab">
                    <fmt:message key="motor.tab.buttonControl"/>
                </button>
            </li>
            <li class="nav-item" role="presentation">
                <button class="nav-link" id="joypad-tab" data-bs-toggle="tab" data-bs-target="#joypad" type="button" role="tab">
                    <fmt:message key="motor.tab.joypadControl"/>
                </button>
            </li>
        </ul>

        <!-- Sekme İçerikleri -->
        <div class="tab-content" id="kontrolTabContent">
            <div class="tab-pane fade show active text-center" id="buton" role="tabpanel">
                <div><button class="btn btn-primary btn-kontrol" onclick="gonder('ILERI')">
                    <fmt:message key="motor.button.up"/>
                </button></div>
                <div>
                    <button class="btn btn-warning btn-kontrol" onclick="gonder('SOL')">
                        <fmt:message key="motor.button.left"/>
                    </button>
                    <button class="btn btn-secondary btn-kontrol" onclick="gonder('DUR')">
                        <fmt:message key="motor.button.stop"/>
                    </button>
                    <button class="btn btn-warning btn-kontrol" onclick="gonder('SAG')">
                        <fmt:message key="motor.button.right"/>
                    </button>
                </div>
                <div><button class="btn btn-danger btn-kontrol" onclick="gonder('GERI')">
                    <fmt:message key="motor.button.down"/>
                </button></div>
            </div>
            <div class="tab-pane fade text-center" id="joypad" role="tabpanel">
                <div class="joypad-area">
                    <p><strong><fmt:message key="motor.joypad.instructions1"/></strong></p>
                    <p><kbd>↑</kbd> <fmt:message key="motor.joypad.up"/>, <kbd>↓</kbd> <fmt:message key="motor.joypad.down"/>, <kbd>←</kbd> <fmt:message key="motor.joypad.left"/>, <kbd>→</kbd> <fmt:message key="motor.joypad.right"/>, <kbd> </kbd> <fmt:message key="motor.joypad.stop"/>.</p>
                </div>
            </div>
        </div>
    </div>
</fmt:bundle>

<!-- Bootstrap Bundle (Popper + JS) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    const contextPath = '${pageContext.request.contextPath}';
    const getPortsUrl = contextPath + "/motor/get_ports";
    const selectPortMsg = '<spring:message code="motor.alert.selectPort"/>';
    const errPortList = '<spring:message code="motor.error.portList"/>';
    const errCommand  = '<spring:message code="motor.error.command"/>';

    window.addEventListener('DOMContentLoaded', () => {
        fetch(getPortsUrl)
            .then(res => res.json())
            .then(ports => {
                const select = document.getElementById('port');
                select.innerHTML = `<option value="">${selectPortMsg}</option>`;
                if (!ports.length) {
                    select.innerHTML += `<option disabled>${'<spring:message code="motor.noPorts"/>'}</option>`;
                }
                ports.forEach(p => {
                    const opt = document.createElement('option');
                    opt.value = p.device;
                    opt.textContent = p.description;
                    select.appendChild(opt);
                });
            })
            .catch(err => console.error(errPortList, err));
    });

    const komutKarsiliklari = { ILERI: 'I', GERI: 'G', SOL: 'L', SAG: 'R', DUR: 'S' };
    function gonder(cmd) {
        const port = document.getElementById('port').value;
        const baudrate = +document.getElementById('baudrate').value;
        if (!port) { alert(selectPortMsg); return; }
        const char = komutKarsiliklari[cmd];
        fetch(contextPath + "/motor/kontrol_komut", {
            method: 'POST', headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ komut: char, port, baudrate })
        })
            .then(r => r.json())
            .then(j => { if (j.status !== 'ok') alert(errCommand + j.message); })
            .catch(e => console.error(errCommand, e));
    }

    document.addEventListener('keydown', e => {
        if (!document.getElementById('joypad-tab').classList.contains('active')) return;
        const keyMap = { ArrowUp: 'ILERI', ArrowDown: 'GERI', ArrowLeft: 'SOL', ArrowRight: 'SAG', ' ': 'DUR' };
        if (keyMap[e.key]) { gonder(keyMap[e.key]); e.preventDefault(); }
    });
</script>
</body>
</html>