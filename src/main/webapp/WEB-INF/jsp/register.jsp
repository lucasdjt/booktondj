<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<html>
<head>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100">

<div class="max-w-md mx-auto mt-20 bg-white p-6 rounded-xl shadow">

    <h2 class="text-2xl font-bold mb-4">Créer un compte</h2>

    <% 
        String err = (String) request.getAttribute("error");
        if ("missing".equals(err)) { %>
            <div class="p-3 mb-4 bg-red-100 text-red-700 rounded">Champs manquants.</div>
    <% } else if ("nomatch".equals(err)) { %>
            <div class="p-3 mb-4 bg-red-100 text-red-700 rounded">Les mots de passe ne correspondent pas.</div>
    <% } else if ("exists".equals(err)) { %>
            <div class="p-3 mb-4 bg-red-100 text-red-700 rounded">Nom ou email déjà utilisé.</div>
    <% } %>

    <form method="post">

        <label class="block">Nom d'utilisateur</label>
        <input name="name" class="w-full p-2 border rounded mb-4" required>

        <label class="block">Email</label>
        <input type="email" name="email" class="w-full p-2 border rounded mb-4" required>

        <label class="block">Mot de passe</label>
        <input type="password" name="password" class="w-full p-2 border rounded mb-4" required>

        <label class="block">Confirmer le mot de passe</label>
        <input type="password" name="confirm" class="w-full p-2 border rounded mb-4" required>

        <button class="w-full text-white py-2 rounded" style="background:#10b981;">
            Créer mon compte
        </button>
    </form>

    <a href="login" class="text-blue-600 mt-4 inline-block">
        Déjà un compte ? Connexion
    </a>

</div>

</body>
</html>