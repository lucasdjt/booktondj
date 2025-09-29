# Site - BookTonDJ

## Sommaire 

1. Réfléchir à la structuration de l’application et à ses fonctionalités
2. Réfléchir aux paramètres génériques qui seront utilisés
3. Proposer un modèle conceptuel de données adapté (Power AMC ? ?)
4. Créer les tables correspondantes (et conserver le script)
5. Remplir de quelques données d’exemples pertinents (et conserver le script)
6. Tester quelques requêtes
— Nombre de réservations aujourd’hui ?
— Créneaux libres dans le mois ?
— Personnes impliquées dans le créneau ?
— etc ...

## Sujets

Systèmes de gestion de rendez-vous en ligne --> Gestion de rdv avec un artiste ou une organisation
Un site internet de gestion de rendez-vous multi-utilisateurs. --> Plusieurs utilisateurs peuvent booker des artistes
Le site doit permettre d’une part de montrer aux utilisateurs les créneaux libres --> Créneaux libres des artistes
Permettre aux utilisateurs de saisir et gérer leurs rendez-vous --> Réserver un artiste
Autoriser des rendez-vous que s’ils respectent les contraintes souhaitées pour ce site. --> Les artistes peuvent mettre leur propres conditions et autoriser leurs rdv ou refuser automatiquement
Le planning de réservation des créneaux de piscine avec la contrainte pas plus de 30 personnes par heure --> Pas le droit d'avoir des réservations sur une même journée, donc un artiste peut pas se déplacer
C’est un framework général : on ne cherche pas un site qui permet de créer plusieurs plannings. --> Seulement sur le planing des artistes


## Décortiquer le sujet

### 1. Réfléchir à la structuration de l’application et à ses fonctionalités
PROGRAMME
- Un utilisateur peut créer un booking avec 1 ou plusieurs DJs
- Un utilisateur peut modifier son booking avec 1 DJ
- Un utilisateur peut supprimer son booking avec 1 DJ
- Plusieurs utilisateurs peuvent booker 1 DJ
- Un DJ peut faire les mêmes actions qu'un utilisateur
- Un DJ peut mettre des conditions sur les demandes de booking (Cachet minimum/Horaires et Dates/Installation technique [backline, sono, régisseur]/Lieu/Types d'événements [concert/showcase/festival/privé avec infos sur l'événements nombre de prsn/autres djs/matériels...])
- Les DJs peuvent demander des modifications sur les bookings pour valider
- Les bookings peuvent se faire refuser automatiquement avec les conditions, ou être automatiquement validée
- Un utilisateur peuvent voir le planning des DJs (sans les informations privées)
- Un DJ peut voir son planning et le planning des autres DJs (sans les informations privées)
- Un DJ ne peut être réserver un nombre x de fois sur une même journée
- Un DJ ne peut être réserver sur plusieurs endroits si la distance de ces endroits est trop court.
- Le site se concentre sur le booking des DJs
- Il ne peut avoir plusieurs bookings sur un même horaire
- Un DJ ne peut être booker dans ces temps ou il est non disponible (selon leur fuseau horaire aussi, besoin de repos, selon transport, repos, studio, tournée déjà prévue)
- Les Managers des Djs peuvent faire les mêmes actions que le DJ
- Un délai d'annulation est imposé (ex : pas possible 24h avant pour revenir) avec une politique de remboursement ou pénalité
- Il y a une durée minimale d’un créneau et une durée maximale, le créneau comprends un temps avant et après son booking et son temps pour arrivée au booking.
- Nombre de réservations limitées sur une semaine
ROLES
- ADMIN --> Gère le site
- ORGANISATION --> Gère les bookings
- DJs / MANAGER --> Gère les bookings de leur DJ
- Utilisateurs --> Crée des bookings
FONCTIONNALITE
- Comptes Utilisateurs avec profil, avis, historique de réservation
- Compte Artistes avec profil, bouton de réservation, avis, calendrier, tableau de bord avec nb de réservations, créneaux libres, revenus
- Recherche d'artistes (Par genre, disponibilité, cachet...)
- Paiement en ligne
- Notifications (envoi de email, SMS aux artistes/utilisateurs)

### 2. Réfléchir aux paramètres génériques qui seront utilisés
### 3. Proposer un modèle conceptuel de données adapté (Power AMC ? ?)
### 4. Créer les tables correspondantes (et conserver le script)
### 5. Remplir de quelques données d’exemples pertinents (et conserver le script)
### 6. Tester quelques requêtes
— Nombre de réservations aujourd’hui ?
— Créneaux libres dans le mois ?
— Personnes impliquées dans le créneau ?
— etc ...
