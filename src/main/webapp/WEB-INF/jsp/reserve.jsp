<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%
    String date = (String) request.getAttribute("date");
    Integer sid  = (Integer) request.getAttribute("sid");

    String color1 = (String) request.getAttribute("planningColorPrimary");
%>

<html>
<head>
    <title>Réserver</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-gray-100">
<%@ include file="/WEB-INF/jsp/includes/header.jspf" %>

<div class="max-w-md mx-auto bg-white p-6 mt-10 shadow-lg rounded-xl">

    <h2 class="text-2xl font-bold mb-4">Confirmer votre réservation</h2>

    <form method="post" action="reserve">
        <input type="hidden" name="date" value="<%= date %>">
        <input type="hidden" name="sid" value="<%= sid %>">

        <p class="mb-4">Vous êtes sur le point de réserver ce créneau.</p>

        <button class="px-4 py-2 text-white rounded"
                style="background:<%= color1 %>;">
            Confirmer
        </button>
    </form>

    <a href="day?date=<%= date %>"
       class="mt-4 inline-block text-blue-600 hover:underline">
       Retour
    </a>

</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jspf" %>
</body>
</html>