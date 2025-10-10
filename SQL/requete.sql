-- Afficher un utilisateur par ID
SELECT *
FROM users
WHERE uid = 1;
-- Afficher un DJ par ID
SELECT a.*, u.*
FROM artists a
JOIN users u ON u.uid = a.user_id
WHERE a.aid = 1;
-- Afficher le managers d’un DJ
SELECT u.*
FROM artists a
JOIN users u ON u.uid = a.manager_id
WHERE a.aid = 1;
-- Afficher les DJs gérés par un manager
SELECT a.*, u.*
FROM artists a
JOIN users u ON u.uid = a.user_id
WHERE a.manager_id = 4;
-- Afficher les DJs les plus populaires
SELECT a.*, u.pseudo, u.popularity
FROM artists a
JOIN users u ON u.uid = a.user_id
ORDER BY u.popularity DESC
LIMIT 3;
-- Afficher le profil public d’un DJ
SELECT u.pseudo, u.description, u.popularity, a.genre
FROM artists a
JOIN users u ON u.uid = a.user_id
WHERE a.aid = 1;
-- Afficher les bookings actifs d’un utilisateur
SELECT *
FROM bookings
WHERE uid = 1
AND status IN ('Pending','Confirmed','Confirmed without payment','Returned');
-- Afficher les bookings d’un DJ
SELECT *
FROM bookings
WHERE aid = 1;
-- Afficher les bookings en attente d'un dj
SELECT *
FROM bookings
WHERE aid = 1
AND status = 'Pending';
-- Filtrer bookings par date
SELECT *
FROM bookings
ORDER BY created_date DESC;
-- Filtrer bookings par statut
SELECT *
FROM bookings
ORDER BY status DESC;
-- Afficher le planning d’un DJ
SELECT *
FROM plannings
WHERE aid = 1;
-- Afficher les créneaux disponibles d’un DJ sur ce mois --*
SELECT *
FROM plannings
WHERE aid = 1
AND is_occupied = FALSE
AND DATE_TRUNC('month', start_date) = DATE_TRUNC('month', end_date);
-- Afficher les créneaux indisponibles d’un DJ
SELECT *
FROM plannings
WHERE aid = 1
AND is_occupied = TRUE;
-- Afficher la localisation d’un booking
SELECT l.*
FROM bookings b
JOIN localisations l ON l.lid = b.lid
WHERE b.bid = 1;
-- Afficher les avis d’un User
SELECT *
FROM avis
WHERE evaluated_id = 2;
-- Afficher la note moyenne d’un DJ
SELECT AVG(score)
FROM avis
WHERE evaluated_id = 2;
-- Afficher les avis non visibles des organisateurs
SELECT *
FROM avis
WHERE is_public = FALSE
AND evaluated_id = 2;
-- Filtrer avis par date
SELECT *
FROM avis
ORDER BY posted_date DESC;
-- Afficher la conversation entre deux utilisateurs
SELECT *
FROM messages
WHERE (sender_id = 2 AND receiver_id = 1)
   OR (sender_id = 1 AND receiver_id = 2)
ORDER BY sent_date;
-- Afficher la blacklist d’un DJ
SELECT *
FROM blacklists
WHERE artist_id = 1;
-- Afficher les villes blacklistées
SELECT *
FROM blacklists
WHERE blacklist_type = 'Location';
-- Afficher les users blacklistés
SELECT *
FROM blacklists
WHERE blacklist_type = 'User';
-- Afficher les users qui ont ce nom de blacklisté
SELECT *
FROM users
WHERE nom LIKE '%Draggas%';
-- Afficher l'historique des actions d'un user
SELECT *
FROM logs
WHERE uid = 1
ORDER BY log_date DESC;
-- Afficher le revenu total de tous ses bookings
SELECT SUM(actual_amount)
FROM bookings
WHERE aid = 1
AND status IN ('Confirmed','Confirmed without payment');
-- Afficher son nombre de bookings par mois
SELECT DATE_TRUNC('month', created_date) AS mois,
       COUNT(*)
FROM bookings
WHERE aid = 1
GROUP BY mois
ORDER BY mois;
-- Afficher le taux d’acceptation / refus
SELECT
    SUM(CASE WHEN status LIKE 'Confirmed%' THEN 1 ELSE 0 END)::float / COUNT(*) AS acceptance_rate,
    SUM(CASE WHEN status IN ('Rejected','Cancelled') THEN 1 ELSE 0 END)::float / COUNT(*) AS refusal_rate
FROM bookings
WHERE aid = 1;
-- Afficher le temps joué (heures totales de set)
SELECT SUM(max_hours_playings)
FROM artists
WHERE aid = 1;
-- Afficher les artistes les plus bookés d'un organisateur
SELECT a.aid, u.pseudo, COUNT(*) AS total
FROM bookings b
JOIN artists a ON a.aid = b.aid
JOIN users u ON u.uid = a.user_id
WHERE b.uid = 1
GROUP BY a.aid, u.pseudo
ORDER BY total DESC;