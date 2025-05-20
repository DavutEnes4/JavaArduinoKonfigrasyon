<%--
  Created by IntelliJ IDEA.
  User: Decoder
  Date: 17.05.2025
  Time: 22:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title><spring:message code="index.title"/></title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
  <meta charset="UTF-8">
  <style>
    .dashboard-btn {
      width: 250px;
      height: 100px;
      font-size: 1.2rem;
      margin: 15px;
    }
  </style>
</head>
<body class="bg-body-secondary">
<div class="container text-center mt-5">
  <h2 class="mb-4">🛠️ <spring:message code="index.header"/> </h2>
  <p class="text-muted mb-5"><spring:message code="index.botHeader" /></p>

  <div class="d-flex justify-content-center flex-wrap">
    <a href="${pageContext.request.contextPath}/kartlar" class="btn btn-primary dashboard-btn">
      <i class="bi bi-sliders"></i> <spring:message code="index.configPanel" />
    </a>

    <a href="${pageContext.request.contextPath}/configlar" class="btn btn-info text-white dashboard-btn">
      <i class="bi bi-table"></i> <spring:message code="index.dataPanel" />
    </a>

    <a href="${pageContext.request.contextPath}/motor/panel" class="btn btn-success dashboard-btn">
      <i class="bi bi-controller"></i> <spring:message code="index.controlPanel" />
    </a>
  </div>
</div>
</body>
</html>
