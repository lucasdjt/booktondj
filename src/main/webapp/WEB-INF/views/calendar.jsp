<%@ page import="java.time.*" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    Map<Integer,Integer> counters = (Map<Integer,Integer>) request.getAttribute("counters");

    int month = (int) request.getAttribute("month");
    int year = (int) request.getAttribute("year");
    int nbDays = (int) request.getAttribute("nbDays");
    int firstDay = (int) request.getAttribute("firstDay");

    LocalDate now = LocalDate.of(year, month, 1);
    LocalDate precedent = now.minusMonths(1);
    LocalDate next = now.plusMonths(1);
%>

<html>
<head>
    <title>Planning Mensuel</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="calendar-container">
        <header class="calendar-header">
            <a class="button" href="calendar?month=<%= precedent.getMonthValue() %>&year=<%= precedent.getYear() %>">Précédent</a>
            <h2><%= now.getMonth().getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.FRENCH) + " " + year %></h2>
            <a class="button" href="calendar?month=<%= next.getMonthValue() %>&year=<%= next.getYear() %>">Suivant</a>
        </header>

        <table class="calendar-table">
            <tr>
                <th>Lun</th><th>Mar</th><th>Mer</th><th>Jeu</th><th>Ven</th><th>Sam</th><th>Dim</th>
            </tr>

            <%
                int currentDay = 1;
                boolean started = false;

                for (int row = 0; row < 6; row++) {
                    out.print("<tr>");
                    for (int col = 1; col <= 7; col++) {
                        if (!started && col == firstDay) {
                            started = true;
                        }
                        if (started && currentDay <= nbDays) {
                            String link = "day?date=" + year + "-" 
                            + String.format("%02d", month) + "-" 
                            + String.format("%02d", currentDay);
                            out.print("<td class='day'>");
                            out.print("<a class='slot' href='" + link + "'>");
                            out.print("<div class='date'>" + currentDay + "</div>");
                            out.print("<div class='count'>" + counters.get(currentDay) + "</div>");
                            out.print("</a></td>");
                            currentDay++;
                        } else {
                            out.print("<td class='empty'></td>");
                        }
                    }
                    out.print("</tr>");
                }
            %>

        </table>
    </div>
</body>
</html>
