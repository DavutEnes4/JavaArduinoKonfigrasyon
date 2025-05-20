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
<html lang="${pageContext.response.locale.language}">
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="configlar.title"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-body-secondary">
<fmt:setLocale value="${pageContext.response.locale.language}" scope="session"/>
<fmt:bundle basename="messages">
    <div class="container py-5">
        <h2 class="mb-4 text-center text-primary">
            <fmt:message key="configlar.title"/>
        </h2>
        <form class="row mb-4" method="get" action="${pageContext.request.contextPath}/configlar">
            <div class="col-auto">
                <input type="text" name="search" class="form-control"
                       placeholder="<fmt:message key='configlar.searchPlaceholder'/>"
                       value="${search != null ? search : ''}"/>
            </div>
            <div class="col-auto">
                <button class="btn btn-primary" type="submit">
                    <fmt:message key="configlar.searchButton"/>
                </button>
            </div>
            <div class="d-flex flex-column align-items-center w-50 mx-auto">
                <select id="port" class="form-select mb-2">
                    <option value=""><fmt:message key="motor.selectPort"/></option>
                </select>
                <input type="number" id="baudrate" class="form-control"
                       placeholder="<fmt:message key='motor.baudrate'/>"
                       value="9600"/>
            </div>

        </form>
        <div class="table-responsive">
            <table class="table table-bordered table-hover align-middle shadow">
                <thead class="table-primary">
                <tr>
                    <th scope="col">#</th>
                    <th scope="col"><fmt:message key="configlar.table.kartAdi"/></th>
                    <th scope="col"><fmt:message key="configlar.table.configName"/></th>
                    <th scope="col"><fmt:message key="configlar.table.pinValues"/></th>
                    <th scope="col"><fmt:message key="configlar.table.date"/></th>
                    <th scope="col"><fmt:message key="configlar.table.actions"/></th>
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
                            <button class="btn btn-warning btn-sm" onclick="guncelle(this)">
                                <fmt:message key="configlar.button.update"/>
                            </button>
                            <button class="btn btn-danger btn-sm" onclick="sil(this)">
                                <fmt:message key="configlar.button.delete"/>
                            </button>
                            <button class="btn btn-success btn-sm" onclick="yukle('${config.pinValues}')">
                                <fmt:message key="configlar.button.upload"/>
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${fn:length(configlar) == 0}">
                    <tr>
                        <td colspan="6" class="text-center text-muted">
                            <fmt:message key="configlar.noRecords"/>
                        </td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
        <nav>
            <ul class="pagination justify-content-center">
                <c:set var="pageCount"
                       value="${(totalCount / pageSize) + (totalCount % pageSize > 0 ? 1 : 0)}" />
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
</fmt:bundle>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    const contextPath = '${pageContext.request.contextPath}';
    //const getPortsUrl = contextPath + "/motor/get_ports";
    const selectPortMsg = '<spring:message code="motor.alert.selectPort"/>';

    window.addEventListener('DOMContentLoaded', () => {
        fetch(contextPath+"/motor/get_ports")
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

    function sil(btn) {
        const row = btn.closest('tr');
        const id  = row.dataset.id;
        const url = contextPath + "/configlar/delete";
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

    function guncelle(btn) {
        const id = btn.closest('tr').dataset.id;
        window.location.href = contextPath + '/configlar/edit?id=' + id;
    }

    function yukle(komut) {
        const port     = document.getElementById('port').value;
        const baudrate = +document.getElementById('baudrate').value;
        if (!port) {
            alert(selectPortMsg);
            return;
        }
        const url = contextPath+'/configlar/upload'
        fetch(contextPath+'/configlar/upload', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ komut, port, baudrate })
        })
            .then(r => r.json())
            .then(json => {
                if (json.status === 'ok') {
                    alert('Yükleme başarılı: ' + komut);
                } else {
                    alert('Hata alındı');
                    console.log(json.message);
                }
            })
            .catch(e => {
                alert('Yükleme hatası alındı ');
                console.log(e);
            };
    }
</script>
</body>
</html>