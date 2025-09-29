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

## Décortiquer le sujet

### 1. Réfléchir à la structuration de l’application et à ses fonctionalités

#### INTERFACE ET FONCTIONNALITE

- User peut cliquer sur un le profil d'un DJ
- User peut voir les informations d'un DJ
- User peut créer un bookings sur un ou plusieurs DJ
- User peut modifier le booking qu'il a réalisé pour un DJ
- User peut supprimer le booking qu'il a réalisé pour un DJ
- DJ peut recevoir plusieurs bookings de différents users
- Toutes les actions qu'un User peut faire, les rôles supérieurs peut le faire

#### PARAMETRES GENERIQUES

-

#### SQL-MCD

- USER peut créer un BOOKING ou plusieurs BOOKING pour un DJ ou plusieurs DJ
- USER peut modifier un BOOKING ou plusieurs BOOKING pour un DJ ou plusieurs DJ
- USER peut supprimer un BOOKING ou plusieurs BOOKING pour un DJ ou plusieurs DJ
- DJ peut avoir plusieurs BOOKINGS de différents USER
- DJ/MANAGER/ORGANISATION/ADMINS ont les actions de USER



#### AVANT
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
- On peut changer la vue du calendrier (Mois/Semaine/Jour <--> Liste de bookings (confirmées / en attente))
- Les bookings du calendrier peuvent être en discret et révéler qu'à partir d'un certain jour J
- Export du calendrier (Google Calendar, Outlook, iCal)
- On peut réserver plusieurs dates sur un seul bookings
- Les jours fériés / semaines / certaines périodes sont automatiquement bloqués pour les artistes qui le demandes chaque semaine
- Si un booking est déjà pris à une certaine date, une personne peut réalisé une file d'attente et se booker sur cet date si la date s'annule
- Un utilisateur peut booker un artiste et lui demander d'ajouter des artistes
- Un artiste peut demander d'inviter des guests DJs avec lui au planning
- Un utilisateur peut réserver l'équipe technique du DJ
- Toute l'organisation peut être réserver
- Un manager peut gérer plusieurs DJs à la fois
- On a un historique d'un utilisateur avec tous les bookings qui ont déjà faits
- Pour créer un compte, il faut une vérification par l'organisation
- Un DJ peut créer une blacklist de salles/organisateurs/villes/types d'événements
- Un DJ peut refuser une presta trop petite en terme de capacité
- Certains jours du bookings du DJ coûtent + cher (soirée nouvel an / Noël / été)
- Le prix dépends de la durée du set du DJs
- Un DJs ne souhaitent peut-être ne pas réaliser une certaine distance max de trajet
- On peut payer en plusieurs fois un DJ, acompte du début, solde après la presta
- Une caution est garantie pour réserver en cas d'annulation etc...
- Facture automatique avec génération de PDF
- Envois de contrats de bookings avec signature électronique
- Un chat est disponible sur le site pour communiquer entre utilisateurs (Pièces jointes / Images / Vidéos / Vocals possibles)
- On peut mettre une note sur un utilisateur / un DJ, et un avis aussi
- FAQ possible
- Vérification des comptes (Sécurité, A2F etc...)
- Certification d'un organisateurs
- Signalisation possible des utilisateurs
- Recherche DJs avec filtres : Genre musical, disponibilité, cachet, popularité, distance..., Suggestion grâce à des IAs, Pack "Festival" avec un booking de plusieurs Djs en même temps
- Tableau de bord (statistiques) : DJs/managers : nb de bookings acceptés/refusées, revenus sur une période, temps libre moyen entre différentes dates - Organisateurs : Montant dépensé par période, nombre d'événements organisées, satisfactions moyennes des artistes
- L'application doit-être mobile first
- L'appli doit être multilingue
- Mode booking express possible (booker un artiste en 3 clics)
- Envois de notifs en temps réels
- un DJ ou manager doit entrer son SIRET/numéro d’artiste pour générer factures légales.
- seuls les DJs majeurs peuvent accepter leurs propres bookings, les mineurs sont acceptés par leurs managers.
- RGPD → gestion du consentement (cookies, collecte de données).
- Confidentialité : certaines infos (cachet exact, numéro de téléphone) sont visibles seulement après validation du booking.
- A2F obligatoire pour DJ/Managers.
- ex. pas plus de 3 sets en 7 jours pour respecter santé/artiste.
- On peut réserver un projet artistique d'un DJ
- certains DJs peuvent définir une “zone géographique” (rayon max de 300 km).
- Interface sans compte : catalogue DJs (profil simplifié), moteur de recherche de base.
- Interface organisateur calendrier + gestion des bookings, factures, messagerie.
- Interface DJ/Manager : Calendrier interactif (glisser-déposer pour bloquer des dates), tableau de bord avec graphiques (revenus, heures jouées, villes couvertes)., gestion des conditions de booking (cachet, matériel, blacklist, disponibilité).
- Interface Admin : supervision générale, gestion litiges, stats globales.
- Validation automatique différée : une réservation est “provisoirement acceptée” et devient confirmée seulement après paiement.
- Remboursement automatique si annulation par le DJ avant un délai de 24h.
- Règle de priorité si plusieurs demandes sur le même créneau, priorité au plus ancien booking confirmé.
- Règle de substitution : si un DJ annule, un autre DJ de la même agence peut être proposé automatiquement.
- DJ avec + de buzz mis en avant
- DJ avec trop d’annulations → profil moins mis en avant.
- Organisateur avec des avis négatifs --> Limitations du compte dans les choix d'artistes ou quoi
- mettre des avis non visibles par les organisateurs
- Réservation Dernière minute (<48h) → +20% sur le cachet / Jour férié ou haute saison → tarif spécial.
- DJs peuvent activer un “mode discret” → ils apparaissent comme indisponibles mais gardent leurs bookings privés.
- Choisir la durée minimale et maximale d'un set
- Au bout d'une certaine durée de set, le contrat change
- Un organisateur peut réserver un B2B
- Nombre max de bookings par semaine, Annulation --> la caution saute si elle dépasse un certain temps, si le DJ n'est pas présent l'organisation est remboursée et une pénalité est donnée au DJ + un DJ est booké à sa place
- La plateforme prend une commision sur chaque booking (5%)
- Calendrier intelligent : Calcul des distances avec le maps et les villes données, avec adresse exacte etc...
- Seul un organisateur qui a déjà booké un DJ peut lui mettre un avis
- L'argent n'est pas envoyée directement au DJ avant la fin de l'événéments
- Protection des annulations et litiges
- Moteur de suggestion IA : propose des DJs similaires à ceux recherchés (style, budget, dispo).
- Filtrage par popularité (nb de bookings, avis positifs, followers réseaux sociaux).
- Support litige : chat admin disponible pour gérer les conflits.

### 2. Réfléchir aux paramètres génériques qui seront utilisés
### 3. Proposer un modèle conceptuel de données adapté (Power AMC ? ?)
### 4. Créer les tables correspondantes (et conserver le script)
### 5. Remplir de quelques données d’exemples pertinents (et conserver le script)
### 6. Tester quelques requêtes
— Nombre de réservations aujourd’hui ?
— Créneaux libres dans le mois ?
— Personnes impliquées dans le créneau ?
— etc ...
