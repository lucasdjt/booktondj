<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%
    String date = (String) request.getAttribute("date");
    String sid  = (String) request.getAttribute("sid");

    String planningColorPrimary   = (String) request.getAttribute("planningColorPrimary");
    String planningColorSecondary = (String) request.getAttribute("planningColorSecondary");
%>

<html>
<head>
    <title>Réserver</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-gray-100">
<%@ include file="/WEB-INF/includes/header.jspf" %>

<div class="max-w-md mx-auto bg-white p-6 mt-10 shadow-lg rounded-xl">

    <h2 class="text-2xl font-bold mb-4">Réserver le <%= date %></h2>

    <form method="post">
        <input type="hidden" name="date" value="<%= date %>">
        <input type="hidden" name="sid" value="<%= sid %>">

        <label class="block font-semibold">Votre nom :</label>
        <input name="name" class="w-full p-2 border rounded mb-4" required>

        <button class="px-4 py-2 text-white rounded"
                style="background:<%= planningColorPrimary %>;">
            Confirmer la réservation
        </button>
    </form>

    <a href="day?date=<%= date %>"
       class="mt-4 inline-block text-blue-600 hover:underline">
       Retour
    </a>

</div>

<%@ include file="/WEB-INF/includes/footer.jspf" %>
</body>
</html>