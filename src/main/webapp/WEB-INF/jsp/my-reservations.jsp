<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="fr.but3.model.Reservation" %>
<%@ page import="fr.but3.model.Slot" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%
    List<Reservation> reservations =
        (List<Reservation>) request.getAttribute("reservations");

    String filter = (String) request.getAttribute("filter");
    if (filter == null) filter = "all";

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
<%@ include file="/WEB-INF/jsp/includes/header.jspf" %>

<div class="max-w-4xl mx-auto bg-white p-6 mt-10 shadow-lg rounded-xl">

    <h2 class="text-2xl font-bold mb-4">Mes réservations</h2>

    <a href="calendar"
       class="mb-4 inline-block px-4 py-2 text-white rounded"
       style="background:<%= colorPri %>;">
        Retour au calendrier
    </a>

    <div class="flex gap-2 mt-2 mb-6">
        <a href="my-reservations?filter=all"
           class="px-3 py-2 rounded text-sm font-semibold border"
           style="<%= "all".equals(filter) ? ("background:"+colorPri+";color:white;border-color:"+colorPri) : "" %>">
            Toutes
        </a>

        <a href="my-reservations?filter=future"
           class="px-3 py-2 rounded text-sm font-semibold border"
           style="<%= "future".equals(filter) ? ("background:"+colorPri+";color:white;border-color:"+colorPri) : "" %>">
            Futures
        </a>

        <a href="my-reservations?filter=past"
           class="px-3 py-2 rounded text-sm font-semibold border"
           style="<%= "past".equals(filter) ? ("background:"+colorPri+";color:white;border-color:"+colorPri) : "" %>">
            Passées
        </a>
    </div>

    <% if (request.getParameter("error") != null) { %>
        <div class="mb-4 p-3 bg-red-100 text-red-800 rounded">
            Une erreur est survenue: <%= request.getParameter("error") %>
        </div>
    <% } %>

    <% if (reservations == null || reservations.isEmpty()) { %>

        <div class="mt-4 p-4 rounded bg-gray-100 text-gray-700">
            Aucune réservation pour ce filtre.
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
                            <input type="hidden" name="filter" value="<%= filter %>">
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

<%@ include file="/WEB-INF/jsp/includes/footer.jspf" %>
</body>
</html>