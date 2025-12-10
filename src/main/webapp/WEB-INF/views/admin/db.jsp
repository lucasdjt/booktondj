<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>

<html>
<head>
    <title>Base de données</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-gray-100 p-6">
<h1 class="text-3xl font-bold mb-6">Explorateur de la base</h1>

<h2 class="text-xl font-semibold mt-6 mb-2">Utilisateurs</h2>
<table class="table-auto bg-white shadow p-4 w-full">
    <tr class="font-bold bg-gray-200">
        <td>ID</td><td>Nom</td><td>Role</td>
    </tr>
    <% for (Object[] u : (List<Object[]>) request.getAttribute("users")) { %>
    <tr>
        <td><%= u[0] %></td>
        <td><%= u[1] %></td>
        <td><%= u[2] %></td>
    </tr>
    <% } %>
</table>

<h2 class="text-xl font-semibold mt-6 mb-2">Slots</h2>
<table class="table-auto bg-white shadow p-4 w-full">
    <tr class="font-bold bg-gray-200">
        <td>ID</td><td>Date</td><td>Début</td><td>Fin</td><td>Capacité</td>
    </tr>
    <% for (Object[] s : (List<Object[]>) request.getAttribute("slots")) { %>
    <tr>
        <td><%= s[0] %></td>
        <td><%= s[1] %></td>
        <td><%= s[2] %></td>
        <td><%= s[3] %></td>
        <td><%= s[4] %></td>
    </tr>
    <% } %>
</table>

<h2 class="text-xl font-semibold mt-6 mb-2">Réservations</h2>
<table class="table-auto bg-white shadow p-4 w-full">
    <tr class="font-bold bg-gray-200">
        <td>ID</td><td>User</td><td>Date</td><td>Début</td><td>Nb Personnes</td>
    </tr>
    <% for (Object[] r : (List<Object[]>) request.getAttribute("reservations")) { %>
    <tr>
        <td><%= r[0] %></td>
        <td><%= r[1] %></td>
        <td><%= r[2] %></td>
        <td><%= r[3] %></td>
        <td><%= r[4] %></td>
    </tr>
    <% } %>
</table>

</body>
</html>