<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="fr.but3.model.Reservation" %>
<%@ page import="fr.but3.model.Slot" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%
    List<Reservation> reservations =
        (List<Reservation>) request.getAttribute("reservations");

    String colorPri = (String) request.getAttribute("planningColorPrimary");
    String colorSec = (String) request.getAttribute("planningColorSecondary");

    DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter tf = DateTimeFormatter.ofPattern("HH:mm");
%>

<html>
<head>
    <title>Admin — Réservations</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-gray-100">

<%@ include file="/WEB-INF/includes/header.jspf" %>

<div class="max-w-5xl mx-auto bg-white p-6 mt-10 shadow-lg rounded-xl">

    <h2 class="text-3xl font-bold mb-6">
        Administration — Toutes les réservations
    </h2>

    <table class="w-full border-collapse">
        <thead>
            <tr style="background:<%= colorSec %>;">
                <th class="border px-3 py-2 text-left">Utilisateur</th>
                <th class="border px-3 py-2 text-left">Date</th>
                <th class="border px-3 py-2 text-left">Heure</th>
                <th class="border px-3 py-2 text-left">Nb Pers.</th>
                <th class="border px-3 py-2 text-left">Action</th>
            </tr>
        </thead>
        <tbody>

        <% for (Reservation r : reservations) {
               Slot s = r.getSlot();
        %>
            <tr class="hover:bg-gray-50">
                <td class="border px-3 py-2"><%= r.getUser().getName() %></td>

                <td class="border px-3 py-2"><%= s.getDate().format(df) %></td>

                <td class="border px-3 py-2">
                    <%= s.getStartTime().format(tf) %> - 
                    <%= s.getEndTime().format(tf) %>
                </td>

                <td class="border px-3 py-2"><%= r.getNbPersonnes() %></td>

                <td class="border px-3 py-2">
                    <form method="post" action="delete-reservation"
                          onsubmit="return confirm('Supprimer cette réservation ?');">
                        <input type="hidden" name="id" value="<%= r.getId() %>">
                        <button class="px-3 py-1 text-white rounded text-sm"
                                style="background:#dc2626;">
                            Supprimer
                        </button>
                    </form>
                </td>
            </tr>
        <% } %>

        </tbody>
    </table>

</div>

<%@ include file="/WEB-INF/includes/footer.jspf" %>

</body>
</html>