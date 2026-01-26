-- Afficher un utilisateur par ID
SELECT *
FROM users
WHERE uid = 1;
-- Afficher un service par ID
SELECT s.*, u.*
FROM services s
JOIN users u ON u.uid = s.uid
WHERE s.uid = 2;
-- Afficher le profil public du propriétaire d'un service
SELECT u.pseudo, u.description
FROM services s
JOIN users u ON u.uid = s.uid
WHERE s.uid = 2;
-- Afficher les bookings actifs d’un utilisateur
SELECT *
FROM bookings
WHERE uid = 3
AND status IN ('Completed','Pending');
-- Afficher les bookings d'un service
SELECT *
FROM bookings
WHERE sid = 1;
-- Afficher les bookings en attente d'un service
SELECT *
FROM bookings
WHERE sid = 1
AND status = 'Pending';
-- Filtrer bookings par date
SELECT *
FROM bookings
ORDER BY created_date DESC;
-- Filtrer bookings par statut
SELECT *
FROM bookings
ORDER BY status ASC;
-- Afficher le planning d’un service
SELECT *
FROM plannings
WHERE sid = 1;
-- Afficher les créneaux disponibles d’un service sur ce mois
SELECT *
FROM plannings
WHERE sid = 1
AND is_available = TRUE
AND DATE_TRUNC('month', start_date) = DATE_TRUNC('month', end_date);
-- Afficher les créneaux indisponibles d’un service
SELECT *
FROM plannings
WHERE sid = 1
AND is_available = FALSE;
-- Afficher la localisation d’un booking
SELECT l.*
FROM bookings b
JOIN localisations l ON l.lid = b.lid
WHERE b.bid = 1;
-- Afficher les avis d’un User
SELECT *
FROM avis
WHERE evaluated_id = 2;
-- Afficher la note moyenne d’un service
SELECT AVG(score)
FROM avis
WHERE evaluated_id = 2;
-- Afficher les avis non visibles des users
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
WHERE (sender_id = 3 AND receiver_id = 4)
   OR (sender_id = 4 AND receiver_id = 3)
ORDER BY sent_date;
-- Afficher la blacklist d’un service
SELECT *
FROM blacklists
WHERE sid = 1;
-- Afficher les villes blacklistées
SELECT *
FROM blacklists
WHERE type = 'Localisations';
-- Afficher les users blacklistés
SELECT *
FROM blacklists
WHERE type = 'Users';
-- Trouver les utilisateurs qui ressemble au nom blacklisté
SELECT *
FROM users
WHERE nom LIKE '%De Je%';
-- Afficher l'historique des actions d'un user
SELECT *
FROM logs
WHERE uid = 2
ORDER BY log_date DESC;
-- Afficher le revenu total de tous ses bookings
SELECT SUM(amount)
FROM bookings
WHERE sid = 1
AND status IN ('Completed','Pending');
-- Afficher son nombre de bookings par mois
SELECT DATE_TRUNC('month', created_date) AS mois,
       COUNT(*)
FROM bookings
WHERE sid = 1
GROUP BY mois
ORDER BY mois;
-- Afficher le taux d’acceptation / refus
SELECT
    SUM(CASE WHEN status LIKE 'Completed' THEN 1 ELSE 0 END)::float / COUNT(*) AS acceptance_rate,
    SUM(CASE WHEN status IN ('Rejected','Cancelled') THEN 1 ELSE 0 END)::float / COUNT(*) AS refusal_rate
FROM bookings
WHERE sid = 1;
-- Afficher les services les plus bookés d'un user
SELECT s.sid, u.pseudo, COUNT(*) AS total
FROM bookings b
JOIN services s ON s.sid = b.sid
JOIN users u ON u.uid = s.uid
WHERE b.uid = 2
GROUP BY s.sid, u.pseudo
ORDER BY total DESC;