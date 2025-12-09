<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="fr.but3.model.Reservation" %>
<%@ page import="fr.but3.model.Slot" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%
    List<Reservation> reservations =
        (List<Reservation>) request.getAttribute("reservations");

    String colorPri   = (String) request.getAttribute("planningColorPrimary");
    String colorSec   = (String) request.getAttribute("planningColorSecondary");

    DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
%>

<html>
<head>
    <title>Mes réservations</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-gray-100">
<%@ include file="/WEB-INF/includes/header.jspf" %>

<div class="max-w-4xl mx-auto bg-white p-6 mt-10 shadow-lg rounded-xl">

    <h2 class="text-2xl font-bold mb-4">Mes réservations</h2>

    <a href="calendar"
       class="mb-4 inline-block px-4 py-2 text-white rounded"
       style="background:<%= colorPri %>;">
        Retour au calendrier
    </a>

    <% if (reservations == null || reservations.isEmpty()) { %>

        <div class="mt-4 p-4 rounded bg-gray-100 text-gray-700">
            Vous n'avez aucune réservation pour le moment.
        </div>

    <% } else { %>

        <table class="w-full mt-4 border-collapse">
            <thead>
                <tr style="background:<%= colorSec %>;">
                    <th class="border px-3 py-2 text-left">Date</th>
                    <th class="border px-3 py-2 text-left">Heure</th>
                    <th class="border px-3 py-2 text-left">Personnes</th>
                    <th class="border px-3 py-2 text-left">Actions</th>
                </tr>
            </thead>
            <tbody>
            <% for (Reservation r : reservations) {
                   Slot s = r.getSlot();
            %>
                <tr class="hover:bg-gray-50">
                    <td class="border px-3 py-2">
                        <%= s.getDate().format(dateFmt) %>
                    </td>
                    <td class="border px-3 py-2">
                        <%= s.getStartTime().format(timeFmt) %> - <%= s.getEndTime().format(timeFmt) %>
                    </td>
                    <td class="border px-3 py-2">
                        <%= r.getNbPersonnes() %>
                    </td>
                    <td class="border px-3 py-2">
                        <form method="post" action="my-reservations/delete"
                              onsubmit="return confirm('Annuler cette réservation ?');">
                            <input type="hidden" name="id" value="<%= r.getId() %>">
                            <button class="px-3 py-1 text-white rounded text-sm"
                                    style="background:#ef4444;">
                                Annuler
                            </button>
                        </form>
                    </td>
                </tr>
            <% } %>
            </tbody>
        </table>

    <% } %>

</div>

<%@ include file="/WEB-INF/includes/footer.jspf" %>
</body>
</html>