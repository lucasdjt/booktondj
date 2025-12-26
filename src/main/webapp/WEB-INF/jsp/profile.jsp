<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ page import="fr.but3.model.User" %>

<%
    User user = (User) request.getAttribute("user");

    String contextPath = request.getContextPath();

    String colorPri = (String) request.getAttribute("planningColorPrimary");
    String colorSec = (String) request.getAttribute("planningColorSecondary");
    if (colorPri == null) colorPri = "#2563eb";
    if (colorSec == null) colorSec = "#1e293b";

    String photoUrl = null;
    if (user != null && user.getProfileImage() != null && !user.getProfileImage().isBlank()) {
        photoUrl = contextPath + "/uploads/" + user.getProfileImage();
    }
%>

<html>
<head>
    <title>Profil</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>

<body class="bg-gray-100">
<%@ include file="/WEB-INF/jsp/includes/header.jspf" %>

<div class="max-w-xl mx-auto bg-white p-6 mt-10 shadow-lg rounded-xl">

    <h2 class="text-2xl font-bold mb-6">Mon profil</h2>

    <% if ("1".equals(request.getParameter("ok"))) { %>
        <div class="p-3 mb-4 bg-green-100 text-green-800 rounded">
            Photo mise à jour.
        </div>
    <% } %>

    <% if ("1".equals(request.getParameter("error"))) { %>
        <div class="p-3 mb-4 bg-red-100 text-red-800 rounded">
            Impossible d&apos;envoyer la photo (format ou taille invalide, ou erreur disque).
        </div>
    <% } %>

    <div class="flex items-center gap-6 mb-6">
        <div class="w-28 h-28 rounded-full overflow-hidden bg-gray-200 flex items-center justify-center">
            <% if (photoUrl != null) { %>
                <img src="<%= photoUrl %>" class="w-full h-full object-cover" alt="Photo de profil">
            <% } else { %>
                <span class="text-gray-500 font-semibold">N/A</span>
            <% } %>
        </div>

        <div>
            <div class="text-lg font-semibold"><%= user.getName() %></div>
            <div class="text-gray-600"><%= user.getEmail() %></div>
            <div class="text-sm mt-1 inline-block px-2 py-1 rounded" style="background:<%= colorPri %>; color:white;">
                <%= user.getRole() %>
            </div>
        </div>
    </div>

    <form method="post"
          action="<%= contextPath %>/profile/photo"
          enctype="multipart/form-data"
          class="space-y-3">

        <label class="block font-semibold">Photo de profil (JPG / PNG / WEBP)</label>
        <input type="file"
               name="file"
               accept="image/png,image/jpeg,image/webp"
               class="w-full p-2 border rounded"
               required>

        <button class="px-4 py-2 text-white rounded"
                style="background:<%= colorPri %>;">
            Mettre à jour
        </button>
    </form>

</div>

<%@ include file="/WEB-INF/jsp/includes/footer.jspf" %>
</body>
</html>