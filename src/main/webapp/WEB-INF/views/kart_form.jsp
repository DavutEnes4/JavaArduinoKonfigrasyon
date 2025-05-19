<%--
  Created by IntelliJ IDEA.
  User: Decoder
  Date: 18.05.2025
  Time: 19:18
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="tr">
<head>
    <meta charset="UTF-8">
    <title>Kart ve Pin Seçimi</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-body-secondary">
<div class="container py-5">
    <h3 class="mb-4">Kart ve Pin Seçimi</h3>
    <form id="mainForm" method="post">
        <div class="mb-4">
            <label for="kartSec" class="form-label">Kart Seçiniz</label>
            <select id="kartSec" name="kartId" class="form-select" required>
                <option value="">Kart seçiniz...</option>
                <!-- Sunucudan gelen kartlar -->
                <c:forEach var="kart" items="${kartlar}">
                    <option value="${kart.id}">${kart.adi}</option>
                </c:forEach>
            </select>
        </div>
        <!-- Dinamik olarak pin select'leri buraya eklenecek -->
        <div id="pinFormu"></div>
        <button type="submit" class="btn btn-primary mt-3">Kaydet</button>
    </form>
</div>
<script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
<script>
    $('#kartSec').change(function(){
        var kartId = $(this).val();
        if (kartId) {
            $.getJSON('/my_config_war/kartlar/kart/' + kartId + '/pinler', function(pinler){
                var html = '';
                $.each(pinler, function(i, pin){
                    html += '<div class="mb-3">';
                    html += '<label class="form-label">'+ pin.adi +'</label>';
                    html += '<select class="form-select" name="pin_' + pin.id + '" required>';
                    html += '<option value="">Seçiniz...</option>';
                    // Buraya istersen diğer port seçeneklerini de ekleyebilirsin:
                    html += '<option value="A">A</option><option value="B">B</option>';
                    html += '</select>';
                    html += '</div>';
                });
                $('#pinFormu').html(html);
            });
        } else {
            $('#pinFormu').empty();
        }
    });
</script>
</body>
</html>

