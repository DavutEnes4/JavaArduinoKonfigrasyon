<%--
  Created by IntelliJ IDEA.
  User: Decoder
  Date: 18.05.2025
  Time: 13:05
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="${pageContext.response.locale.language}">
<head>
    <meta charset="UTF-8">
    <title><spring:message code="kartlar.title"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-body-secondary">
<div class="container py-5">
    <h2 class="mb-4 text-center text-primary">
        <spring:message code="kartlar.header"/>
    </h2>

    <!-- KARTLAR -->
    <div class="row g-4">
        <c:forEach var="kart" items="${kartlar}">
            <div class="col-12 col-md-4">
                <div class="card shadow h-100">
                    <div class="card-body">
                        <h5 class="card-title text-primary">${kart.adi}</h5>
                        <h6 class="card-subtitle mb-2 text-muted">#${kart.id}</h6>
                        <div class="mb-2" style="max-height:150px; overflow:auto; word-break:break-word;">
                            <strong><spring:message code="kartlar.aciklamaLabel"/></strong><br/>
                            <c:forEach var="aciklama" items="${kart.aciklamalar}">
                                <c:if test="${aciklama.dil eq lang}">
                                    <span class="badge text-dark mb-1"
                                          style="text-align:left; display:inline-block; white-space:normal; margin-bottom:2px;">
                                        <c:choose>
                                            <c:when test="${fn:length(aciklama.aciklama) > 40}">
                                                ${fn:substring(aciklama.aciklama, 0, 512)}
                                            </c:when>
                                            <c:otherwise>
                                                ${aciklama.aciklama}
                                            </c:otherwise>
                                        </c:choose>
                                    </span>
                                </c:if>
                            </c:forEach>
                        </div>
                        <div>
                            <strong><spring:message code="kartlar.pinSayisiLabel"/></strong>
                            <span class="badge bg-secondary">${fn:length(kart.pinler)}</span>
                        </div>
                    </div>
                    <div class="card-footer bg-transparent border-0">
                        <a href="${pageContext.request.contextPath}/kart/${kart.id}/config"
                           class="btn btn-outline-primary w-100">
                            <spring:message code="kartlar.configButton"/>
                        </a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <!-- SAYFALAMA BAR -->
    <nav aria-label="<spring:message code='kartlar.paginationAria'/>" class="mt-5">
        <ul class="pagination justify-content-center">
            <c:set var="totalPages" value="${(totalCount / pageSize) + (totalCount % pageSize > 0 ? 1 : 0)}"/>
            <c:forEach begin="0" end="${totalPages - 1}" var="i">
                <li class="page-item ${i == currentPage ? 'active' : ''}">
                    <a class="page-link"
                       href="?page=${i}&size=${pageSize}&search=${search}">
                            ${i + 1}
                    </a>
                </li>
            </c:forEach>
        </ul>
    </nav>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
