<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.time.*" %>
<%@ page import="java.util.*" %>
<%@ page import="fr.but3.utils.Creneau" %>

<%
    LocalDate date = (LocalDate) request.getAttribute("date");
    String mode = (String) request.getAttribute("mode");

    List<Creneau> creneaux = (List<Creneau>) request.getAttribute("creneaux");
    Map<Integer, List<Creneau>> timeline =
        (Map<Integer, List<Creneau>>) request.getAttribute("timeline");

    LocalTime start = (LocalTime) request.getAttribute("start");
    LocalTime end   = (LocalTime) request.getAttribute("end");
    boolean overnight = (boolean) request.getAttribute("overnight");

    String colorPri   = (String) request.getAttribute("planningColorPrimary");
    String colorSec   = (String) request.getAttribute("planningColorSecondary");
%>

<html>
<head>
    <title>Planning du <%= date %></title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-gray-100">
<%@ include file="/WEB-INF/includes/header.jspf" %>

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

            <% for (Creneau c : creneaux) { %>

                <li class="p-4 rounded shadow"
                    style="background:<%= c.complet ? "#FEE2E2" : colorSec %>">

                    <div class="flex justify-between items-center">
                        <span class="font-bold"><%= c.timeStart %> - <%= c.timeEnd %></span>

                        <% if (!c.complet) { %>
                            <a href="reserve?date=<%= c.dateReelle %>&start=<%= c.timeStart %>"
                               class="px-3 py-1 text-white rounded"
                               style="background:<%= colorPri %>;">
                               Réserver
                            </a>
                        <% } else { %>
                            <span class="text-red-700 font-semibold">Complet</span>
                        <% } %>
                    </div>

                    <div><%= c.nbInscrits %> / <%= c.maxPers %> personnes</div>
                </li>

            <% } %>

        </ul>

    <% } else { %>

        <div class="space-y-3">

            <% for (int h = 0; h < 24; h++) { %>

                <div class="grid grid-cols-12 gap-3 p-3 rounded"
                     style="background:<%= colorSec %>">

                    <div class="col-span-2 font-semibold text-lg">
                        <%= String.format("%02d:00", h) %>
                    </div>

                    <div class="col-span-10 flex flex-wrap gap-2">

                        <% List<Creneau> hourList = timeline.get(h); %>

                        <% if (hourList.isEmpty()) { %>

                            <span class="text-gray-400 italic">Aucun créneau</span>

                        <% } else { %>

                            <% for (Creneau c : hourList) { %>
                                <div class="px-4 py-2 rounded shadow text-sm"
                                     style="background:<%= c.complet ? "#FECACA" : colorSec %>">

                                    <div class="font-semibold"><%= c.timeStart %> – <%= c.timeEnd %></div>
                                    <div class="text-xs">
                                        <%= c.nbInscrits %> / <%= c.maxPers %> pers
                                    </div>

                                    <% if (!c.complet) { %>
                                        <a href="reserve?date=<%= date %>&start=<%= c.timeStart %>"
                                           class="inline-block mt-1 px-2 py-1 text-white rounded text-xs"
                                           style="background:<%= colorPri %>">
                                            Réserver
                                        </a>
                                    <% } else { %>
                                        <div class="text-red-700 font-semibold text-xs">Complet</div>
                                    <% } %>

                                </div>
                            <% } %>
                        <% } %>

                    </div>
                </div>

            <% } %>

        </div>

    <% } %>

</div>

<%@ include file="/WEB-INF/includes/footer.jspf" %>
</body>
</html>
