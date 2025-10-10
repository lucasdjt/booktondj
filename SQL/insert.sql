INSERT INTO users (pseudo, nom, prenom, email, role, verified, creation_date, tel, notif, popularity, description)
VALUES
-- Main
('Lucas', 'De Jesus Teixeira', 'Lucas', 'lucas@example.com', 'User', TRUE, '2025-01-01', '+33762709049', 'Email', 50, 'User exemplaire'),
('Draggas', 'DJ', 'Draggas', 'draggas@example.com', 'Artist', TRUE, '2025-01-02', '+33000000001', 'Phone', 300, 'DJ Musical'),
('Admin', 'Root', 'Admin', 'admin@example.com', 'Admin', TRUE, '2025-01-04', NULL, NULL, NULL, 'Compte administrateur'),
-- Test
('ManagerX', 'Manager', 'Xavier', 'manager@example.com', 'Manager', TRUE, '2025-01-03', '+33000000002', 'Email', 10, 'Manager officiel'),
('UserTest', 'Test', 'User', 'usertest@example.com', 'User', FALSE, '2025-01-05', NULL, NULL, NULL, NULL),
('ArtisteTest', 'Test', 'Artist', 'artistetest@example.com', 'Artist', FALSE, '2025-01-06', NULL, NULL, NULL, NULL);

INSERT INTO artists (user_id, manager_id, genre, valid_auto, is_fee_prv, is_minor, rest_hours_needed, max_hours_playings, min_fee, km_max, min_participants, max_reservations)
VALUES
(2, 4, 'Techno', TRUE, TRUE, FALSE, 2, 4, 150, 200, 50, 10), -- Draggas (Manager X)
(6, NULL, 'Rap', FALSE, FALSE, FALSE, 0, 0, NULL, NULL, NULL, NULL); -- ArtisteTest (no manager)

INSERT INTO plannings (aid, start_date, end_date, is_occupied, is_note_public, special_fee, planning_note)
VALUES
-- Jours feries
(1, '2025-12-25', '2025-12-25', FALSE, TRUE, 200, 'D Jour F Noel'), -- Draggas
(2, '2025-12-25', '2025-12-25', FALSE, TRUE, NULL, 'AT Jour F Noel'), -- ArtisteTest
(1, '2026-01-01', '2026-01-01', FALSE, TRUE, 250, 'D Jour de l an'), -- Draggas
(2, '2026-01-01', '2026-01-01', FALSE, TRUE, NULL, 'AT Jour de l an'), -- ArtisteTest
(1, '2025-12-13', '2025-12-13', FALSE, TRUE, 500, 'D Date premium'), -- Draggas
(2, '2025-12-13', '2025-12-13', FALSE, TRUE, NULL, 'AT Date simple'), -- ArtisteTest
-- Occupe
(1, '2026-01-12', '2026-01-12', TRUE, FALSE, NULL, 'Draggas reservation'), -- Draggas
(2, '2026-01-13', '2026-01-13', TRUE, FALSE, NULL, 'AT reservation'); -- ArtisteTest

INSERT INTO localisations (name, localisation_type, address, latitude, longitude)
VALUES
('Paris Night Club', 'Nightclub', '123 Rue de Paris, 75000 Paris', 48.8566, 2.3522),
('Brussels Fest Arena', 'Festival', '12 Grand Place, Bruxelles', 50.8503, 4.3517),
('LocalisationTest', 'Other', 'Adresse inconnue', NULL, NULL);

INSERT INTO bookings (uid, aid, pid, lid, status, created_date, updated_date, nb_participants, annulation_delay, include_technicals, actual_amount, amount_required, booking_note, dj_list, amount_cancel, public_reveal_date)
VALUES
(1, 1, 7, 1, 'Confirmed', NOW(), NOW(), 300, '2026-01-10 00:00:00', TRUE, 500.00, 700.00, 'Booking complet Paris', 'Draggas', 150.00, '2026-01-12 18:00:00'),
(5, 2, 8, 3, 'Pending', NOW(), NOW(), 0, '2026-01-11 00:00:00', FALSE, 0, 0, NULL, NULL, NULL, NULL);

INSERT INTO blacklists (artist_id, blacklist_type, blacklist_reason, blacklist_name)
VALUES 
(1, 'User', 'Mauvais comportement', 'UserTest'), -- Draggas blackliste UserTest
(2, 'Event', 'Conflit anterieur', 'Lucas'), -- ArtisteTest blackliste Lucas
(1, 'Location', 'Ville pas ouf', 'Isbergues'); -- Draggas blackliste Isbergues

INSERT INTO logs (uid, action_name, log_description)
VALUES
(1, 'Connexion', 'Connexion reussie'), -- Lucas
(2, 'MAJ Profil', 'Modification description artiste'), -- Draggas
(5, 'Tentative', 'Mauvaise operation utilisateur'); -- UserTest

INSERT INTO messages (sender_id, receiver_id, message)
VALUES
(2, 1, 'Wsh Lucas, ready pour le booking ?'),
(1, 2, 'Toujours ! On gere ca !'),
(6, 5, 'Bonjour, dispo pour votre event.'),
(5, 6, 'Merci, je vous tiens au courant.');

INSERT INTO avis (reviewer_id, evaluated_id, score, is_public, reason)
VALUES (1, 2, 10, TRUE, 'Prestation incroyable, energie monstrueuse !');