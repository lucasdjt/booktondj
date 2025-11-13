<%@ page import="java.time.*" %>
<%@ page import="java.util.List" %>

<%
    LocalDate date = (LocalDate) request.getAttribute("date");
    List<LocalTime> hours = (List<LocalTime>) request.getAttribute("hours");
    LocalTime tStart = (LocalTime) request.getAttribute("tStart");
    LocalTime tEnd = (LocalTime) request.getAttribute("tEnd");
%>

<html>
<head>
    <title>Planning du <%= date %></title>
    <link rel="stylesheet" href="css/style.css">
</head>

<body>

<h2>Planning du <%= date %></h2>

<div class="day-container">
    <ul class="slots">
        <%
            for (LocalTime h : hours) {
                boolean disabled;
                if (tEnd.isBefore(tStart)) {
                    disabled = h.isAfter(tEnd) && h.isBefore(tStart);
                } else {
                    disabled = h.isBefore(tStart) || h.isAfter(tEnd);
                }

                String css = disabled ? "slot-disabled" : "slot-active";

                out.println("<li class='" + css + "'>");
                out.println(h.toString());
                
                if (!disabled) {
                    out.println(" <a href='reserve?date=" + date + "&hour=" + h + "'>Réserver</a>");
                }

                out.println("</li>");
            }
        %>
    </ul>
</div>

</body>
</html>