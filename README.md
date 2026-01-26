# BookTaPlace

BookTaPlace est une application web de **réservation de créneaux** développée dans le cadre d’une **SAE – BUT Informatique**.  
Elle permet à des utilisateurs de consulter un calendrier, réserver des créneaux et gérer leurs rendez-vous, avec une partie administrateur dédiée.

L’application est pensée comme un **framework adaptable** : les règles métier (horaires, capacités, couleurs, délais, etc.) sont configurables selon le contexte (médecin, piscine, salle, …).

---

# GUIDE – Installation, configuration et tests complets

Ce guide explique **comment lancer et tester le projet BookTaPlace depuis un simple `git clone`**, sans connaissance préalable du code.

---

## 1. Cloner le projet

```bash
git clone git@gitlab-ssh.univ-lille.fr:lucas.de-jesus-teixeira.etu/sae5_booktaplace.git
cd booktaplace
```

---

## 2. Configuration des emails

> ⚠️ Si les emails échouent, l’application **continue de fonctionner normalement**.
> Les erreurs sont simplement affichées dans les logs.

### 2.1 Choisir un serveur mail

#### Gmail

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.debug=true

app.mail.from=${MAIL_EMAIL}
```

#### UnivLille

```properties
spring.mail.host=smtp.univ-lille.fr
spring.mail.port=587
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.debug=true

app.mail.from=${MAIL_EMAIL}
```

### 2.2 Définir les variables d’environnement

#### Linux / macOS

```bash
export MAIL_USERNAME=mail(gmail)/username(univlille)
export MAIL_PASSWORD=mdp(univlille)/mdp_application(gmail)
export MAIL_EMAIL=mail
```

#### Windows (PowerShell)

```powershell
setx MAIL_USERNAME "mail(gmail)/username(univlille)"
setx MAIL_PASSWORD "mdp(univlille)/mdp_application(gmail)"
setx MAIL_EMAIL "mail"
```

(Si vous voulez esquiver les emails, vous le pouvez en configurant juste les variables d'environnement sur des variables fausses tant qu'elles sont déclarées, l'envoi d'email sera pas disponible mais l'application fonctionnera)
---

## 3. Configuration du site (choix du contexte métier)

Ouvrir le fichier :

```
src/main/resources/config.properties
```

Sélectionner **un seul fichier métier** parmi :

* `medecin.properties` (Avec ce properties, Vous pouvez décommenttez les commentaires dans data.sql pour avoir un jeux de donnée avec 50 réservations par 5 utilisateurs dans le site)
* `coiffeur.properties`
* `piscine.properties`
* `salle.properties`

Chaque fichier définit :

* le titre du site
* les couleurs du planning
* les horaires
* la capacité des créneaux
* les règles de réservation

---

## 4. Lancer l’application

```bash
mvn clean spring-boot:run
```

---

## 5. Accéder à l’application

### 5.1 Console H2 (debug base de données)

👉 [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

**Connexion H2**

* JDBC URL : `jdbc:h2:mem:booktaplace`
* User : `sa`
* Password : *(vide)*

### 5.2 Accès au site

👉 [http://localhost:8080/calendar](http://localhost:8080/calendar)

---

## 6. Comptes existants (data.sql)

| Rôle  | Username | Mot de passe |
| ----- | -------- | ------------ |
| ADMIN | admin    | admin        |
| USER  | test     | test         |

> Les mots de passe sont stockés en **MD5** dans la base.

---

## 7. Parcours utilisateur – Tests fonctionnels

### 7.1 Navigation publique

* Accéder au calendrier
* Changer de mois
* Cliquer sur un jour
* Visualiser les créneaux

---

### 7.2 Création de compte

* `/register`

---

### 7.3 Connexion / Déconnexion

* `/login`
* `/logout`

---

## 8. Réservations (Utilisateur)

### 8.1 Créer une réservation

1. Se connecter avec l’utilisateur `test`
2. Choisir un jour disponible
3. Cliquer sur un créneau
4. Confirmer la réservation

---

### 8.2 Mes réservations

URL : `/my-reservations`

Filtres disponibles :

* Toutes
* Futures
* Passées

---

## 9. Profil utilisateur

URL : `/profile`

Fonctionnalités :

* upload d’une photo de profil (PNG / JPG / WEBP)
* stockage dans le dossier `/uploads`
* affichage dans le header
* persistance après rechargement de la page

---

## 10. Partie Administrateur

### 10.1 Accès

Se connecter avec le compte `admin`

---

### 10.2 Dashboard

URL : `/admin/dashboard`

Vérifier :

* nombre d’utilisateurs
* nombre de créneaux
* nombre de réservations

---

### 10.3 Gestion base de données (debug)

URL : `/admin/db`

* Vue brute des tables
* Outil de debug interne

---

### 10.4 Gestion des utilisateurs

URL : `/admin/users`

* Changement de rôle
* Suppression d’utilisateurs

---

### 10.5 Gestion des créneaux

URL : `/admin/slots`

* Suppression d’un créneau
* Suppression d’une journée complète

---

### 10.6 Gestion des réservations

URL : `/admin/reservations`

* Visualiser toutes les réservations
* Supprimer une réservation (force admin)

---

## 11. API REST (JSON / XML)

### 11.1 Rendez-vous d’un jour

```http
GET /todayslist/{date}
```

Exemples :

```bash
curl -H "Accept: application/json" http://localhost:8080/todayslist/2025-01-15
curl -H "Accept: application/xml"  http://localhost:8080/todayslist/2025-01-15
```

---

### 11.2 Rendez-vous d’un utilisateur

```http
GET /myappointments/{username}
```

Exemple :

```bash
curl -H "Accept: application/json" http://localhost:8080/myappointments/test
```

---

## 12. Endpoints principaux

* `/calendar`
* `/day`
* `/login` / `/logout`
* `/register`
* `/reserve`
* `/my-reservations`
* `/profile`

### Admin

* `/admin/dashboard`
* `/admin/db`
* `/admin/users`
* `/admin/slots`
* `/admin/reservations`

### REST

* `/todayslist/{date}`
* `/myappointments/{name}`

---

## 13. Contexte pédagogique

Projet réalisé dans le cadre d’une **SAE – BUT Informatique**.
Objectifs :

* application web MVC avec Spring
* persistance JPA
* sécurité et rôles
* API REST
* séparation des responsabilités
* configuration métier centralisée

Voici une **section prête à intégrer telle quelle** dans ton rapport / README long / dossier SAE.
Le ton est **technique, clair, justifié pédagogiquement**, exactement ce qu’un correcteur attend.

---

## 14. Aspects techniques

### Modélisation des données – MCD / Base de données

La modélisation repose sur trois entités principales, issues du besoin fonctionnel de réservation de créneaux :

* **User**
  Représente un utilisateur de l’application (client ou administrateur).
  Il contient les informations d’authentification (username, mot de passe hashé), le rôle (USER / ADMIN) et des informations de profil (photo).

* **Slot**
  Représente un créneau horaire réservable pour une date donnée.
  Un créneau est caractérisé par :

  * une date (`slot_date`)
  * une heure de début et de fin
  * une capacité maximale
    Cette entité permet de modéliser des journées découpées en créneaux fixes.

* **Reservation**
  Représente la réservation d’un créneau par un utilisateur.
  Elle relie un **User** à un **Slot**, avec le nombre de personnes réservées et la date de création.

Relations principales :

* Un **User** peut avoir plusieurs **Reservation**
* Un **Slot** peut avoir plusieurs **Reservation**
* Une **Reservation** est liée à exactement un **User** et un **Slot**

---

### Objets métier (Business Objects)

Les principaux objets métier créés sont :

* **User**
  Gère l’identité et les droits d’accès.
  Utilisé par le système d’authentification et par la partie administrative.

* **Slot**
  Porte la logique des créneaux (date, horaires, capacité).
  Il constitue le cœur du planning.

* **Reservation**
  Implémente la logique de réservation :

  * lien utilisateur / créneau
  * gestion de la capacité
  * base pour les règles métier (annulation, doublons, etc.)

* **Config / fichiers de configuration métier**
  Centralisent les règles globales :

  * jours ouvrés
  * jours fériés
  * délais maximum de réservation
  * couleurs et paramètres visuels
    Ce mécanisme permet de transformer l’application en **framework configurable** selon le contexte (médecin, piscine…).

---

### Contrôleurs (Spring MVC)

L’application est organisée selon une architecture **MVC claire**.

#### Contrôleurs principaux (utilisateur)

* **CalendarController**
  Gère l’affichage du calendrier mensuel et la navigation entre les mois.

* **DayController**
  Affiche le détail d’une journée et les créneaux disponibles.

* **ReserveController**
  Gère la création d’une réservation avec les contrôles métier associés.

* **MyReservationsController**
  Permet à un utilisateur de consulter et d’annuler ses réservations.

* **ProfileController**
  Gère le profil utilisateur et l’upload de la photo.

#### Authentification

* **AuthController**
  Gère l’inscription et la connexion des utilisateurs.

#### Administration

* **AdminDashboardController**
  Vue globale des statistiques.

* **AdminUsersController / AdminSlotsController / AdminReservationsController**
  Gestion complète des données par un administrateur.

#### API REST

* Endpoints REST exposés pour :

  * les rendez-vous d’un jour
  * les rendez-vous d’un utilisateur
    avec support **JSON et XML**.

---

### Fonctionnement du système d’authentification

L’authentification repose sur **Spring Security**.

* Les utilisateurs s’authentifient via un formulaire `/login`
* Les mots de passe sont stockés sous forme de **hash MD5** (choix imposé par le cadre du projet)
* Les rôles **USER** et **ADMIN** permettent de restreindre l’accès aux fonctionnalités sensibles
* Les routes `/admin/**` sont protégées et accessibles uniquement aux administrateurs
* La session utilisateur est gérée automatiquement par Spring Security

---

## Détails techniques et charge de travail (Pourquoi 6 semaines ?)

Le projet a nécessité un temps conséquent pour plusieurs raisons :

1. **Nombreux bugs apparus lors de la SAE**

2. **Compréhension et mise en place de Spring**

2. **Respect des consignes pédagogiques au maximum possible**

4. **Gestion des règles métier**

5. **Interface utilisateur** (Apprendre l'utilisation de Tailwindcss)

5. **Tester le projet à chaque implémentation**

---

## Conclusion

Ce projet a été particulièrement formateur.

Les principales difficultés ont été :

* la compréhension globale de l’écosystème Spring
* la gestion propre des règles métier
* la cohérence entre back-end, front-end et base de données

Ce qui a pris le plus de temps :

* la structuration de l’application
* la correction des cas limites
* la mise en place d’un système réellement configurable

Ce projet nous a permis de :

* mieux comprendre le modèle MVC
* maîtriser Spring
* concevoir une application extensible et maintenable
* travailler comme sur un projet réel, et non un simple exercice

---

## Perspectives et évolutions possibles

Plusieurs améliorations pourraient être envisagées :

* Internationalisation complète de l’interface
* Système de notification plus avancé (emails planifiés, rappels)
* Gestion de créneaux dynamiques
* Amélioration de la sécurité (hash plus robuste, remember-me)
* Statistiques avancées pour les administrateurs

## Auteur

Lucas DE JESUS TEIXEIRA, Ambroise DONNET
BUT Informatique – Université de Lille

```