<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.time.*" %>
<%@ page import="java.util.*" %>
<%@ page import="fr.but3.model.Slot" %>

<%
    LocalDate date = (LocalDate) request.getAttribute("date");
    String mode = (String) request.getAttribute("mode");

    List<Map<String,Object>> creneaux =
        (List<Map<String,Object>>) request.getAttribute("creneaux");

    String colorPri   = (String) request.getAttribute("planningColorPrimary");
    String colorSec   = (String) request.getAttribute("planningColorSecondary");
%>

<html>
<head>
    <title>Planning du <%= date %></title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-gray-100">
<%@ include file="/WEB-INF/jsp/includes/header.jspf" %>

<div class="max-w-4xl mx-auto bg-white p-6 mt-10 shadow-lg rounded-xl">

    <h2 class="text-2xl font-bold mb-4">Planning du <%= date %></h2>

    <a href="calendar"
       class="mb-4 inline-block px-4 py-2 text-white rounded"
       style="background:<%= colorPri %>;">
       Retour au calendrier
    </a>

    <div class="flex justify-center my-6">
        <div class="flex bg-gray-200 rounded-full p-1">

            <a href="day?date=<%= date %>&mode=creneaux"
               class="px-4 py-2 rounded-full text-sm font-semibold
                      <%= mode.equals("creneaux") ? "text-white shadow" : "text-gray-600" %>"
               style="<%= mode.equals("creneaux") ? "background:"+colorPri : "" %>">
               Créneaux
            </a>

            <a href="day?date=<%= date %>&mode=heures"
               class="px-4 py-2 rounded-full text-sm font-semibold
                      <%= mode.equals("heures") ? "text-white shadow" : "text-gray-600" %>"
               style="<%= mode.equals("heures") ? "background:"+colorPri : "" %>">
               Heures
            </a>
        </div>
    </div>

<% if (mode.equals("creneaux")) { %>

    <ul class="space-y-3">

        <% for (Map<String,Object> m : creneaux) {

            Slot s = (Slot) m.get("slot");
            int used = (int) m.get("used");
            boolean complet = (boolean) m.get("complet");
            boolean reservable = (boolean) m.get("reservable");
        %>

            <li class="p-4 rounded shadow"
                style="background:<%= complet ? "#FEE2E2" : colorSec %>">

                <div class="flex justify-between items-center">
                    <span class="font-bold"><%= s.getStartTime() %> - <%= s.getEndTime() %></span>

                    <% if (reservable) { %>
                        <a href="reserve?sid=<%= s.getId() %>&date=<%= date %>"
                           class="px-3 py-1 text-white rounded"
                           style="background:<%= colorPri %>;">
                            Réserver
                        </a>
                    <% } else if (complet) { %>
                        <span class="text-red-700 font-semibold">Complet</span>
                    <% } else { %>
                        <span class="text-gray-500 font-semibold">Indisponible</span>
                    <% } %>
                </div>

                <div><%= used %> / <%= s.getCapacity() %> personnes</div>

            </li>

        <% } %>

    </ul>

<% } else { %>

    <%
        Map<Integer, List<Map<String,Object>>> hours = new LinkedHashMap<>();
        for (int h = 0; h < 24; h++) hours.put(h, new ArrayList<>());

        for (Map<String,Object> m : creneaux) {
            Slot s = (Slot) m.get("slot");
            hours.get(s.getStartTime().getHour()).add(m);
        }
    %>

    <div class="space-y-3">

        <% for (int h = 0; h < 24; h++) { %>

            <div class="grid grid-cols-12 gap-3 p-3 rounded" style="background:<%= colorSec %>">

                <div class="col-span-2 font-semibold text-lg">
                    <%= String.format("%02d:00", h) %>
                </div>

                <div class="col-span-10 flex flex-wrap gap-2">

                    <% List<Map<String,Object>> list = hours.get(h);

                    if (list.isEmpty()) { %>

                        <span class="text-gray-400 italic">Aucun créneau</span>

                    <% } else {
                        for (Map<String,Object> m : list) {
                            Slot s = (Slot) m.get("slot");
                            int used = (int) m.get("used");
                            boolean complet = (boolean) m.get("complet");
                            boolean reservable = (boolean) m.get("reservable");
                    %>

                        <div class="px-4 py-2 rounded shadow text-sm"
                             style="background:<%= complet ? "#FECACA" : colorSec %>">

                            <div class="font-semibold"><%= s.getStartTime() %> – <%= s.getEndTime() %></div>
                            <div class="text-xs"><%= used %> / <%= s.getCapacity() %> pers</div>

                            <% if (reservable) { %>
                                <a href="reserve?sid=<%= s.getId() %>&date=<%= date %>"
                                   class="inline-block mt-1 px-2 py-1 text-white rounded text-xs"
                                   style="background:<%= colorPri %>">
                                    Réserver
                                </a>
                            <% } else if (complet) { %>
                                <div class="text-red-700 font-semibold text-xs">Complet</div>
                            <% } else { %>
                                <div class="text-gray-500 font-semibold text-xs">Indisponible</div>
                            <% } %>

                        </div>

                    <% }} %>

                </div>
            </div>

        <% } %>

    </div>

<% } %>

</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jspf" %>
</body>
</html>