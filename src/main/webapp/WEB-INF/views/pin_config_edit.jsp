<%--
  Created by IntelliJ IDEA.
  User: Decoder
  Date: 19.05.2025
  Time: 17:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html lang="${lang}">
<head>
  <meta charset="UTF-8">
  <title>Konfig Güncelle — ${config.adi} (#${config.id})</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    body { background: #eef0f3; }
    .table > :not(caption) > * > * { vertical-align: middle; }
    #pinPreview { font-family: monospace; }
  </style>
</head>
<body>
<div class="container py-5">
  <a href="${pageContext.request.contextPath}/configlar" class="btn btn-link mb-4">
    &larr; Geri
  </a>
  <h2 class="mb-4 text-primary">
    ${config.devreKarti.adi} — Konfig Güncelle (#${config.id})
  </h2>

  <form id="editConfigForm" action="${pageContext.request.contextPath}/configlar/edit" method="post" autocomplete="off">
    <!-- ID’yi gizli gönderiyoruz -->
    <input type="hidden" name="id" value="${config.id}" />

    <div class="form-floating mb-4" style="max-width:400px;">
      <input type="text"
             class="form-control"
             id="isimInput"
             name="adi"
             placeholder=" "
             value="${config.adi}"
             required>
      <label for="isimInput">Konfig İsmi</label>
    </div>

    <table class="table table-striped table-bordered shadow bg-white">
      <thead class="table-light">
      <tr>
        <th>#</th>
        <th>Pin Adı</th>
        <th>Açıklama</th>
        <th>Arduino Pin</th>
      </tr>
      </thead>
      <tbody>
      <c:set var="selectedPins" value="${fn:split(config.pinValues, ',')}" />
      <c:set var="arduinoPinListesi" value="D0:0,D1:1,D2:2,D3:3,D4:4,D5:5,D6:6,D7:7,D8:8,D9:9,D10:10,D11:11,D12:12,D13:13,A0:14,A1:15,A2:16,A3:17,A4:18,A5:19" />
      <c:forEach var="pin" items="${config.devreKarti.pinler}" varStatus="loop">
        <tr>
          <td>${loop.index + 1}</td>
          <td>${pin.adi}</td>
          <td>
            <c:forEach var="aciklama" items="${pin.aciklamalar}">
              <c:if test="${aciklama.dil eq lang}">
                <span class="badge bg-info text-dark mb-1">${aciklama.aciklama}</span>
              </c:if>
            </c:forEach>
          </td>
          <td>
            <select class="form-select pin-select" name="arduinoPinler">
              <option value="">-- Seçiniz --</option>
              <c:forEach var="entry" items="${fn:split(arduinoPinListesi, ',')}">
                <c:set var="parts" value="${fn:split(entry, ':')}" />
                <c:set var="label" value="${parts[0]}" />
                <c:set var="value" value="${parts[1]}" />
                <option value="${value}"
                        <c:if test="${fn:contains(selectedPins, value)}">selected</c:if>>
                    ${label}
                </option>
              </c:forEach>
            </select>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>

    <div class="mb-3">
      <label class="form-label">Seçilen Pin Değerleri:</label>
      <pre id="pinPreview" class="bg-light p-2 border">${config.pinValues}</pre>
    </div>

    <button type="submit" class="btn btn-success">Kaydet</button>
  </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
  const form = document.getElementById('editConfigForm');
  const preview = document.getElementById('pinPreview');
  const selects = document.querySelectorAll('.pin-select');

  function updatePreview() {
    const vals = Array.from(selects)
            .map(s=>s.value)
            .filter(v=>v && v.trim() !== '');
    preview.innerText = vals.join(',');
  }

  selects.forEach(s => s.addEventListener('change', updatePreview));
  updatePreview();

  form.addEventListener('submit', e => {
    e.preventDefault();
    const data = new URLSearchParams(new FormData(form));
    // pinValues alanı sadece preview’dan kopyalayalım
    data.set('pinValues', preview.innerText);

    fetch(form.action, {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: data.toString()
    }).then(res=>{
      if (res.ok) {
        // güncelleme sonrası ana listeye yönlendir
        window.location = '${pageContext.request.contextPath}/configlar';
      } else {
        alert('Kaydetme sırasında hata oluştu');
      }
    });
  });
</script>
</body>
</html>

