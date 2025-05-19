<%--
  Created by IntelliJ IDEA.
  User: Decoder
  Date: 19.05.2025
  Time: 04:15
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="tr">
<head>
    <meta charset="UTF-8">
    <title>Tüm Konfig Dosyaları</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script>
        const contextPath = '${pageContext.request.contextPath}';
        document.addEventListener('DOMContentLoaded', ()=>{});
    </script>
</head>
<body class="bg-body-secondary">
    <div class="container py-5">
        <h2 class="mb-4 text-center text-primary">Tüm Konfig Dosyaları</h2>
        <form class="row mb-4" method="get" action="${pageContext.request.contextPath}/configlar">
            <div class="col-auto">
                <input type="text" name="search" class="form-control" placeholder="Arama yap..." value="${search != null ? search : ''}">
            </div>
            <div class="col-auto">
                <button class="btn btn-primary" type="submit">Ara</button>
            </div>
        </form>
        <div class="table-responsive">
            <table class="table table-bordered table-hover align-middle shadow">
                <thead class="table-primary">
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Kart Adı</th>
                    <th scope="col">Konfig İsmi</th>
                    <th scope="col">Pin Değerleri</th>
                    <th scope="col">Tarih</th>
                    <th scope="col">İşlem</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="config" items="${configlar}" varStatus="loop">
                    <tr data-id="${config.id}" data-kart-id="${config.devreKarti.id}">
                        <td>${(currentPage * pageSize) + loop.index + 1}</td>
                        <td>${config.devreKarti.adi}</td>
                        <td>${config.adi}</td>
                        <td>${config.pinValues}</td>
                        <td><fmt:formatDate value="${config.olusturulmaTarihi}" pattern="dd.MM.yyyy HH:mm"/></td>
                        <td>
                            <button class="btn btn-warning btn-sm" onclick="guncelle(this)">Güncelle</button>
                            <button class="btn btn-danger  btn-sm" onclick="sil(this)">Sil</button>
                            <button class="btn btn-success btn-sm" onclick="yukle(this)">Yükle</button>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${fn:length(configlar) == 0}">
                    <tr>
                        <td colspan="5" class="text-center text-muted">Kayıt bulunamadı.</td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
        <nav>
            <ul class="pagination justify-content-center">
                <c:set var="pageCount" value="${(totalCount / pageSize) + (totalCount % pageSize > 0 ? 1 : 0)}" />
                <c:if test="${pageCount > 0}">
                    <c:forEach begin="0" end="${pageCount - 1}" var="i">
                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                            <a class="page-link"
                               href="${pageContext.request.contextPath}/configlar?page=${i}&size=${pageSize}&search=${search}">
                                    ${i + 1}
                            </a>
                        </li>
                    </c:forEach>
                </c:if>
            </ul>
        </nav>
    </div>
    <script>
        function sil(btn) {
            const row = btn.closest('tr');
            const id  = row.dataset.id;
            const url = contextPath + "/configlar/delete"
            fetch(url, {
                method: 'POST',
                headers:{'Content-Type':'application/json'},
                body: JSON.stringify({ id })
            })
                .then(r=>r.json())
                .then(json=>{
                    if (json.status==='ok') row.remove();
                    else alert('Hata: '+json.message);
                })
                .catch(e=>alert('Silme hatası: '+e));
        }

        function guncelle(button) {
            const row = button.closest('tr');
            const id  = row.dataset.id;

            window.location.href = contextPath+ '/configlar/edit?id=' + id;
        }

        function yukle(btn){
            const id  = btn.closest('tr').dataset.id;
            fetch(contextPath +'/configlar/upload', {
                method: 'POST',
                headers: {'Content-Type':'application/json'},
                body: JSON.stringify({ id })
            })
                .then(r=>r.json())
                .then(json=>{
                    if (json.status==='ok') alert('Kart ID gönderildi: '+id);
                    else alert('Hata: '+json.message);
                })
                .catch(e=>alert('Yükleme hatası: '+e));
        }
    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
