<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="fr.but3.model.Slot" %>

<html>
<head>
    <title>Admin - Créneaux</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-gray-100">
<%@ include file="/WEB-INF/jsp/includes/header.jspf" %>

<div class="max-w-5xl mx-auto bg-white p-6 mt-10 shadow-lg rounded-xl">

    <h2 class="text-2xl font-bold mb-4">Gestion des créneaux</h2>

    <a href="dashboard"
       class="mb-4 inline-block px-4 py-2 text-white rounded"
       style="background:#2563eb;">Retour</a>

    <table class="w-full mt-4 border-collapse">
        <thead>
            <tr class="bg-gray-200">
                <th class="border px-3 py-2">Date</th>
                <th class="border px-3 py-2">Heure</th>
                <th class="border px-3 py-2">Capacity</th>
                <th class="border px-3 py-2">Actions</th>
            </tr>
        </thead>

        <tbody>
        <%
            List<Slot> slots = (List<Slot>) request.getAttribute("slots");
            for (Slot s : slots) {
        %>

        <tr class="hover:bg-gray-50">
            <td class="border px-3 py-2"><%= s.getDate() %></td>
            <td class="border px-3 py-2"><%= s.getStartTime() %> - <%= s.getEndTime() %></td>
            <td class="border px-3 py-2"><%= s.getCapacity() %></td>

            <td class="border px-3 py-2">

                <form method="post" action="slots" class="inline"
                      onsubmit="return confirm('Supprimer ce créneau ?');">
                    <input type="hidden" name="action" value="delete-slot">
                    <input type="hidden" name="sid" value="<%= s.getId() %>">
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