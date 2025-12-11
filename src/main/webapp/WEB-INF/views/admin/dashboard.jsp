<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="fr.but3.utils.Config" %>

<html>
<head>
    <title>Admin - Tableau de bord</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-gray-100">

<%@ include file="/WEB-INF/includes/header.jspf" %>

<div class="max-w-5xl mx-auto bg-white p-8 mt-10 rounded-xl shadow">

    <h1 class="text-3xl font-bold mb-6">Tableau de bord Admin</h1>

    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">

        <div class="p-6 rounded-xl text-white shadow" style="background:#2563eb;">
            <h2 class="text-xl font-semibold">Utilisateurs</h2>
            <p class="text-4xl font-bold mt-2"><%= request.getAttribute("users") %></p>
        </div>

        <div class="p-6 rounded-xl text-white shadow" style="background:#10b981;">
            <h2 class="text-xl font-semibold">Créneaux</h2>
            <p class="text-4xl font-bold mt-2"><%= request.getAttribute("slots") %></p>
        </div>

        <div class="p-6 rounded-xl text-white shadow" style="background:#f43f5e;">
            <h2 class="text-xl font-semibold">Réservations</h2>
            <p class="text-4xl font-bold mt-2"><%= request.getAttribute("reservations") %></p>
        </div>

    </div>

    <div class="mt-10 grid grid-cols-1 md:grid-cols-4 gap-4">
        <a href="users" class="p-4 rounded bg-gray-100 hover:bg-gray-200 text-center font-semibold">Gérer utilisateurs</a>
        <a href="slots" class="p-4 rounded bg-gray-100 hover:bg-gray-200 text-center font-semibold">Gérer créneaux</a>
        <a href="reservations" class="p-4 rounded bg-gray-100 hover:bg-gray-200 text-center font-semibold">Gérer réservations</a>
        <a href="db" class="p-4 rounded bg-gray-100 hover:bg-gray-200 text-center font-semibold">Base de données</a>
    </div>

</div>

<%@ include file="/WEB-INF/includes/footer.jspf" %>
</body>
</html>