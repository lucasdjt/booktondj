<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="java.time.*" %>
<%@ page import="java.util.Map" %>
<%@ page import="fr.but3.model.JourStats" %>

<%
    Map<Integer, JourStats> stats =
        (Map<Integer, JourStats>) request.getAttribute("stats");

    int month = (int) request.getAttribute("month");
    int year = (int) request.getAttribute("year");
    int nbDays = (int) request.getAttribute("nbDays");
    int firstDay = (int) request.getAttribute("firstDay");

    LocalDate now = LocalDate.of(year, month, 1);
    LocalDate precedent = now.minusMonths(1);
    LocalDate next = now.plusMonths(1);

    String colorPri   = (String) request.getAttribute("planningColorPrimary");
    String colorSec   = (String) request.getAttribute("planningColorSecondary");

    LocalDate today = LocalDate.now();
%>

<html>
<head>
    <title>Planning Mensuel</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-gray-100">
<%@ include file="/WEB-INF/jsp/includes/header.jspf" %>

<div class="max-w-3xl mx-auto bg-white p-6 mt-10 shadow-lg rounded-xl">

    <div class="flex justify-between items-center mb-6">

        <a href="calendar?month=<%= precedent.getMonthValue() %>&year=<%= precedent.getYear() %>"
           class="px-4 py-2 text-white rounded shadow"
           style="background:<%= colorPri %>;">
            Précédent
        </a>

        <h2 class="text-2xl font-bold">
            <%= now.getMonth().getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.FRENCH) %>
            <%= year %>
        </h2>

        <a href="calendar?month=<%= next.getMonthValue() %>&year=<%= next.getYear() %>"
           class="px-4 py-2 text-white rounded shadow"
           style="background:<%= colorPri %>;">
            Suivant
        </a>
    </div>

    <table class="w-full border-collapse">
        <tr style="background:<%= colorPri %>;color:white;">
            <th class="p-2">Lun</th><th class="p-2">Mar</th><th class="p-2">Mer</th>
            <th class="p-2">Jeu</th><th class="p-2">Ven</th><th class="p-2">Sam</th><th class="p-2">Dim</th>
        </tr>

        <%
            int currentDay = 1;
            boolean started = false;

            for (int row = 0; row < 6; row++) {
                out.print("<tr>");

                for (int col = 1; col <= 7; col++) {

                    if (!started && col == firstDay) started = true;

                    if (started && currentDay <= nbDays) {

                        JourStats js = stats.get(currentDay);
                        LocalDate d = LocalDate.of(year, month, currentDay);

                        String bg;
                        if (js.isLimiteDepassee() || !js.isOuvert() || js.isFerie()) bg = "#E5E7EB";
                        else if (js.getTauxOccupation() < 0.33) bg = colorSec;
                        else if (js.getTauxOccupation() < 0.66) bg = "#FEF9C3";
                        else bg = "#FEE2E2";

                        boolean clickable = !js.isLimiteDepassee() && js.isOuvert() && !js.isFerie();

                        String openTag;
                        if (clickable) {
                            openTag = "<a href='day?date=" + d + "' class='block p-2 h-full'>";
                        } else {
                            openTag = "<div class='block p-2 h-full'>";
                        }
                        String closeTag = clickable ? "</a>" : "</div>";

                        out.print("<td class='border h-24 align-top' style='background:" + bg + ";'>");

                        out.print(openTag);
                        out.print("<div class='font-bold'>" + currentDay + "</div>");

                        if (js.isFerie())
                            out.print("<div class='text-xs text-red-700'>Férié</div>");
                        else if (!js.isOuvert())
                            out.print("<div class='text-xs text-gray-600'>Fermé</div>");

                        out.print("<div class='text-sm text-gray-700 mt-2'>" +
                                js.getNbCreneauxDisponibles() + " créneaux dispo<br>" +
                                js.getNbTotalPersonnes() + " personnes</div>");

                        out.print(closeTag);
                        out.print("</td>");

                        currentDay++;

                    } else {
                        out.print("<td class='border bg-gray-200'></td>");
                    }
                }
                out.print("</tr>");
            }
        %>
    </table>

</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jspf" %>
</body>
</html>