<%--
  Created by IntelliJ IDEA.
  User: Decoder
  Date: 18.05.2025
  Time: 22:29
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="${pageContext.response.locale.language}">
<head>
    <meta charset="UTF-8">
    <title>
        ${kart.adi} (#${kart.id}) – <spring:message code="config.title"/>
    </title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background: #eef0f3; }
        .badge { font-size: 1em; }
        .form-select { min-width: 90px; }
        .table > :not(caption) > * > * { vertical-align: middle; }
    </style>
</head>
<body>
<div class="container py-5">
    <a href="${pageContext.request.contextPath}/kartlar" class="btn btn-link mb-4">
        &larr; <spring:message code="back.to.cards"/>
    </a>

    <h2 class="mb-4 text-primary">
        ${kart.adi} (#${kart.id}) <spring:message code="config.title"/>
    </h2>

    <div class="mb-4">
        <strong><spring:message code="card.description"/></strong>
        <ul>
            <c:forEach var="aciklama" items="${kart.aciklamalar}">
                <c:if test="${aciklama.dil eq pageContext.response.locale.language}">
                    <li>
                      <span class="badge bg-info text-dark mb-1">
                              ${aciklama.aciklama}
                      </span>
                    </li>
                </c:if>
            </c:forEach>
        </ul>
    </div>

    <form id="pinConfigForm"
          action="${pageContext.request.contextPath}/kart/${kart.id}/config/save"
          method="post" autocomplete="off">
        <input type="hidden" name="kartId" value="${kart.id}" />

        <div class="form-floating mb-4" style="max-width:400px;">
            <input type="text" class="form-control" name="isim" id="isimInput"
                   placeholder=" " required>
            <label for="isimInput">
                <spring:message code="config.name"/>
            </label>
        </div>

        <table class="table table-striped table-bordered shadow bg-white">
            <thead>
            <tr>
                <th>#</th>
                <th><spring:message code="pin.name"/></th>
                <th><spring:message code="desc"/></th>
                <th><spring:message code="arduino.pin"/></th>
            </tr>
            </thead>
            <tbody>
            <c:set var="arduinoPinListesi"
                   value="D0:0,D1:1,D2:2,D3:3,D4:4,D5:5,D6:6,D7:7,D8:8,D9:9,D10:10,D11:11,D12:12,D13:13,A0:14,A1:15,A2:16,A3:17,A4:18,A5:19"/>
            <c:forEach var="pin" items="${kart.pinler}" varStatus="loop">
                <tr>
                    <td>${loop.index + 1}</td>
                    <td>${pin.adi}</td>
                    <td>
                        <c:forEach var="aciklama" items="${pin.aciklamalar}">
                            <c:if test="${aciklama.dil eq pageContext.response.locale.language}">
                                <span class="badge bg-info text-dark mb-1">
                                        ${aciklama.aciklama}
                                </span>
                            </c:if>
                        </c:forEach>
                    </td>
                    <td>
                        <select class="form-select" name="arduinoPinler">
                            <option value="">
                                -- <spring:message code="select"/> --
                            </option>
                            <c:forEach var="pinStr"
                                       items="${fn:split(arduinoPinListesi, ',')}">
                                <c:set var="pinArr" value="${fn:split(pinStr, ':')}" />
                                <option value="${pinArr[1]}">
                                        ${pinArr[0]}
                                </option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <button class="btn btn-success">
            <spring:message code="save"/>
        </button>
    </form>

    <div class="mb-3 mt-4">
        <label class="form-label">
            <spring:message code="selected.pins"/>
        </label>
        <pre id="pinPreview" class="bg-light p-2 border"></pre>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function updatePreview() {
        let selected = [];
        document.querySelectorAll('select[name="arduinoPinler"]').forEach(s=>{
            if(s.value) selected.push(s.value);
        });
        document.getElementById('pinPreview').innerText = selected.join(',');
    }
    document.querySelectorAll('select[name="arduinoPinler"]').forEach(el=>{
        el.addEventListener('change', updatePreview);
    });
    updatePreview();

    document.getElementById('pinConfigForm').addEventListener('submit', e=>{
        e.preventDefault();
        const isim = document.getElementById('isimInput').value;
        const pins = document.getElementById('pinPreview').innerText;
        fetch(e.target.action, {
            method:'POST',
            headers:{'Content-Type':'application/x-www-form-urlencoded'},
            body: 'isim='+encodeURIComponent(isim)+'&pinValues='+encodeURIComponent(pins)
        });
    });
</script>
</body>
</html>
