<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="fr.but3.model.User" %>

<html>
<head>
    <title>Admin - Utilisateurs</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-gray-100">
<%@ include file="/WEB-INF/jsp/includes/header.jspf" %>

<div class="max-w-4xl mx-auto bg-white p-6 mt-10 shadow-lg rounded-xl">

    <h2 class="text-2xl font-bold mb-4">Gestion des utilisateurs</h2>

    <a href="dashboard"
       class="mb-4 inline-block px-4 py-2 text-white rounded"
       style="background:#2563eb;">Retour</a>

    <table class="w-full mt-4 border-collapse">
        <thead>
            <tr class="bg-gray-200">
                <th class="border px-3 py-2">ID</th>
                <th class="border px-3 py-2">Nom</th>
                <th class="border px-3 py-2">Rôle</th>
                <th class="border px-3 py-2">Actions</th>
            </tr>
        </thead>
        <tbody>

        <%
            List<User> users = (List<User>) request.getAttribute("users");
            for (User u : users) {
        %>

        <tr class="hover:bg-gray-50">
            <td class="border px-3 py-2"><%= u.getId() %></td>
            <td class="border px-3 py-2"><%= u.getName() %></td>
            <td class="border px-3 py-2"><%= u.getRole() %></td>

            <td class="border px-3 py-2">

                <form method="post" action="users" class="inline">
                    <input type="hidden" name="id" value="<%= u.getId() %>">
                    <input type="hidden" name="action" value="role-user">
                    <button class="px-3 py-1 rounded bg-blue-500 text-white text-sm">USER</button>
                </form>

                <form method="post" action="users" class="inline">
                    <input type="hidden" name="id" value="<%= u.getId() %>">
                    <input type="hidden" name="action" value="role-admin">
                    <button class="px-3 py-1 rounded bg-yellow-500 text-white text-sm">ADMIN</button>
                </form>

                <form method="post" action="users" class="inline"
                      onsubmit="return confirm('Supprimer cet utilisateur ?');">
                    <input type="hidden" name="id" value="<%= u.getId() %>">
                    <input type="hidden" name="action" value="delete">
                    <button class="px-3 py-1 rounded bg-red-600 text-white text-sm">Supprimer</button>
                </form>

            </td>

        </tr>

        <% } %>

        </tbody>
    </table>
</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jspf" %>
</body>
</html>