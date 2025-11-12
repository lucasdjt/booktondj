# BookTon... Cahier des charges

# 1. Fonctionnalités principales du Framework

## Côté Utilisateur / Client

- Consulter un catalogue de services/prestataires (DJs, coiffeurs, piscines, médecins…).
- Filtrer selon les paramètres définis par le métier :
> type de service, prix, demande
> Accéder à un **profil public** du prestataire.
> Voir les **plages disponibles** (calendrier + heures).
> Créer une réservation.
> Modifier / annuler sa réservation selon les règles du métier.
> Voir ses réservations passées / futures.
> Réception de notifications (Fausses notifications)
> Paiement en ligne (Faux paiement)
> Avis et notes

---

## Côté Services (DJ, Coiffeur, Médecin…)

- Gérer son planning (dispo / indispo / horaires / vacances).
- Ajouter des indisponibilités (publiques/privées).
- Fixer ses règles métier
- Voir/modifier les bookings.
- Gérer ses clients et son historique.
- Activer le “mode discret” (invisible mais toujours réservable par les anciens clients).
- Tableau de bord : statistiques, revenus, historique.

---

## Côté Admin

* Les rôles supérieurs héritent toujours des permissions des inférieurs.
* Supervision des prestations, gestion des litiges.
* Gestion des comptes et vérification.
* Configuration globale du site (langues, couleurs, paramètres planning…).
* Accès aux statistiques globales.

---

# 2. Paramètres GÉNÉRIQUES

Le framework est personnalisable via :
`...`.properties

Avec ses différents paramètres génériques :
(Site)
- Titre
- Logo
- Langues (*)
- Couleur Principale
- Couleur Secondaire
(Planning)
- Premier jour de la semaine
- Jours disponible de la semaine
- Jours considéré fériés
- Horaire du début
- Horaire de fin
- Créneau minimal ou maximale / Durée d'un créneau (Si créneau fixe)
- Temps de pause entre 2 créneaux minimal
- Max réservation sur un jour ou une semaine
- File d'attente pour un créneau fixe