<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>

<html>
<head>
    <title>Admin - Base de données</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-gray-100">
<%@ include file="/WEB-INF/includes/header.jspf" %>

<div class="max-w-6xl mx-auto bg-white p-6 mt-10 rounded-xl shadow">

    <h2 class="text-2xl font-bold mb-4">Explorateur de base de données</h2>

    <a href="dashboard"
       class="mb-6 inline-block px-4 py-2 text-white rounded"
       style="background:#2563eb;">Retour</a>

    <h3 class="text-xl font-semibold mb-2">Table : Users</h3>
    <table class="w-full mb-6 border-collapse text-sm">
        <%
            List<Object[]> users = (List<Object[]>) request.getAttribute("users");
            for (Object[] row : users) {
        %>
        <tr class="border-b">
            <% for (Object col : row) { %>
                <td class="border px-2 py-1"><%= col %></td>
            <% } %>
        </tr>
        <% } %>
    </table>

    <h3 class="text-xl font-semibold mb-2">Table : Slots</h3>
    <table class="w-full mb-6 border-collapse text-sm">
        <%
            List<Object[]> slots = (List<Object[]>) request.getAttribute("slots");
            for (Object[] row : slots) {
        %>
        <tr class="border-b">
            <% for (Object col : row) { %>
                <td class="border px-2 py-1"><%= col %></td>
            <% } %>
        </tr>
        <% } %>
    </table>

    <h3 class="text-xl font-semibold mb-2">Table : Reservations</h3>
    <table class="w-full border-collapse text-sm">
        <%
            List<Object[]> reservations = (List<Object[]>) request.getAttribute("reservations");
            for (Object[] row : reservations) {
        %>
        <tr class="border-b">
            <% for (Object col : row) { %>
                <td class="border px-2 py-1"><%= col %></td>
            <% } %>
        </tr>
        <% } %>
    </table>

</div>

<%@ include file="/WEB-INF/includes/footer.jspf" %>
</body>
</html>