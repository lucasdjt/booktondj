<html>
<head>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100">

<div class="max-w-md mx-auto mt-20 bg-white p-6 rounded-xl shadow">

    <h2 class="text-2xl font-bold mb-4">Connexion</h2>

    <% if (request.getParameter("error") != null) { %>
        <div class="p-3 mb-4 bg-red-100 text-red-700 rounded">
            Identifiants incorrects.
        </div>
    <% } %>

    <form method="post">

        <label class="block">Nom d'utilisateur</label>
        <input name="name" class="w-full p-2 border rounded mb-4" required>

        <label class="block">Mot de passe</label>
        <input name="password" type="password" class="w-full p-2 border rounded mb-4" required>

        <button class="w-full text-white py-2 rounded" style="background:#3b82f6;">
            Se connecter
        </button>
    </form>

    <a href="register" class="text-blue-600 mt-4 inline-block">
        Créer un compte
    </a>

</div>

</body>
</html>