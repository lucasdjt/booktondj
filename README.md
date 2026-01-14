# Site - BookTonDJ

Informations disponible prochainement.

# GUIDE – Installation, configuration et tests complets

Ce guide explique **comment lancer et tester le projet BookTonDJ depuis un simple `git clone`**, sans connaissance préalable du code.

---

## 1. Cloner le projet

```bash
git clone git@gitlab-ssh.univ-lille.fr:lucas.de-jesus-teixeira.etu/sae5_booktondj.git
cd booktondj
```

---

## 2. Configuration des emails

> Si les emails échouent, l’application continue de fonctionner

### 2.1 Serveur à définir

#### Sous GMAIL

```bash
# SERVEUR MAIL
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.debug=true

app.mail.from=${MAIL_EMAIL}
```

#### Sous UnivLille

```bash
# SERVEUR MAIL
spring.mail.host=smtp.univ-lille.fr 
spring.mail.port=587 
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
spring.mail.properties.mail.smtp.starttls.enable=true 
spring.mail.properties.mail.debug=true

app.mail.from=${MAIL_EMAIL}
```

### 2.2 Variables d’environnement à définir

#### Sous Linux / macOS

```bash
export MAIL_USERNAME=mail(gmail)/username(univlille)
export MAIL_PASSWORD=mdp(univlille)/mdpdapplication(gmail)
export MAIL_EMAIL=mail
```

#### Sous Windows (PowerShell)

```powershell
setx MAIL_USERNAME "mail(gmail)/username(univlille)"
setx MAIL_PASSWORD "mdp(univlille)/mdpdapplication(gmail)"
setx MAIL_EMAIL "mail"
```
---

## 3. Configuration du site (choisir la framework)

Allez sur le fichier **src/main/resources/config.properties**

Choisir une des properties de vos choix :
* `medecin.properties`
* `coiffeur.properties`
* `dj.properties`
* `piscine.properties`
* `salle.properties`

Les fichiers définit différents critères (titre/couleurs/horaires/capacité/règles de réservation)
---

## 4. Lancer l’application

```bash
# Aller sur le répertoire du projet à sa racine
mvn clean spring-boot:run
```

## 5. Accéder à l’application

### 5.1 Debug DB (Console H2)
  --> [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

**Connexion H2**
* JDBC URL : `jdbc:h2:mem:booktondj`
* User : `sa`
* Password : *(vide)*

### 5.2 Accès au site
  --> [http://localhost:8080/calendar](http://localhost:8080/calendar)

---

## 6. Comptes existants (data.sql)

| Rôle  | Username | Mot de passe (en MD5) |
| ----- | -------- | ----------------------|
| ADMIN | admin    | admin                 |
| USER  | test     | test                  |

---

## 7. Parcours utilisateur – Tests complets

### 7.1 Navigation publique

* Accéder au calendrier
* Changer de mois
* Cliquer sur un jour
* Voir les créneaux

### 7.2 Création de compte

* `/register`
* Vérifier :
  * validation des champs
  * email envoyé (ou log si échec)
  * connexion automatique après inscription

### 7.3 Connexion / Déconnexion

* `/login`
* `/logout`
* Vérifier la persistance de session

## 8. Réservations (USER)

### 8.1 Créer une réservation

1. Se connecter avec `test`
2. Choisir un jour disponible
3. Cliquer sur un créneau
4. Confirmer la réservation

Vérifications :
* capacité respectée
* pas de double réservation
* email de confirmation

### 8.2 Mes réservations

* `/my-reservations`

Filtres disponibles :
* Toutes
* Futures
* Passées

Tester :
* annulation
* redirection correcte
* email d’annulation

## 9. Profil utilisateur

* `/profile`
* Upload photo de profil (PNG / JPG / WEBP)
* Vérifier :
  * stockage dans `/uploads`
  * affichage dans le header
  * persistance après refresh

## 10. Partie Admin

### 10.1 Accès

Se connecter avec `admin`

### 10.2 Dashboard

* `/admin/dashboard`
* Vérifier :
  * nombre d’utilisateurs
  * nombre de créneaux
  * nombre de réservations

### 10.3 Gestion DB

* `/admin/db`
* Vue brute des tables (debug)

### 10.4 Utilisateurs

* `/admin/users`
* Changer rôle
* Supprimer utilisateur

### 10.5 Créneaux

* `/admin/slots`
* Supprimer :
  * un créneau
  * une journée entière

### 10.6 Réservations

* `/admin/reservations`
* Voir toutes les réservations
* Supprimer une réservation (force admin)

## 11. API REST (JSON / XML)

### 11.1 Liste des RDV d’un jour (organisateur)

```http
curl -X GET "http://localhost:8080/todayslist/2025-01-15" -H "Accept: application/json"
```

```http
curl -X GET "http://localhost:8080/todayslist/2025-01-15" -H "Accept: application/xml"
```

### 11.2 RDV futurs d’un client

```http
curl -X GET "http://localhost:8080/myappointments/test" -H "Accept: application/json"
```