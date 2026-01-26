INSERT INTO users (pseudo, nom, surname, email, role, verified, creation_date, tel, notif, description) VALUES
('ADMIN', 'ROOT', 'Admin', 'admin@bookton.com', 'Admin', TRUE, '2025-01-01', NULL, NULL, 'Administrateur'),
('Lucas DJT', 'De Jesus Teixeira', 'Lucas', 'lucas.de-jesus-teixeira.etu@univ-lille.com', 'Service', TRUE, '2025-01-01', '+330762709049', 'Email', 'Service Lucas'),
('Ambroise', 'DONNET', 'Ambroise', 'ambroise.donnet@univ-lille.fr', 'User', TRUE, '2025-01-03', '+33000000001', 'Phone', 'Utilisateur du site'),
('Test User', 'TEST', 'User', 'user@test.com', 'User', FALSE, '2025-09-01', NULL, NULL, NULL);

INSERT INTO localisations (name, type, address) VALUES
('Paris Centre', 'Ville', '10 Rue de Rivoli, 75001 Paris'),
('Lille Gare', 'Ville', '1 Place des Buisses, 59000 Lille'),
('Bruxelles Nord', 'Ville', 'Boulevard Emile Jacqmain, Bruxelles');

INSERT INTO services (uid, lid, name, type, capacity, description, is_active, price) VALUES
(2, 1, 'Service Premium', 'General', 1, 'Service principale polyvalente', TRUE, 120.00),
(2, 2, 'Service Secondaire', 'General', 1, 'Service secondaire', TRUE, 80.00),
(2, null, 'Service Mobile', 'General', 1, 'Service disponible sur demande', TRUE, 100.00);

INSERT INTO plannings (sid, start_date, end_date, is_available, is_note_public, planning_note, special_price)
VALUES
(1, '2025-12-25', '2025-12-25', TRUE, TRUE, 'Disponible jour férié', 200.00),
(2, '2025-12-31', '2025-12-31', TRUE, TRUE, 'Disponibilité spéciale Nouvel An', 250.00),
(1, '2026-01-10', '2026-01-10', FALSE, FALSE, 'Déjà réservé', NULL);

INSERT INTO bookings (uid, sid, pid, lid, status, nb_participants, amount, amount_required, note) VALUES
(3, 1, 1, 1, 'Completed', 100, 200.00, 200.00, 'Booking finalisé'),
(3, 2, 2, 2, 'Pending', 50, 0, 100.00, 'En attente'),
(4, 3, 2, 2, 'Cancelled', 0, 0, 0, 'Annulé par le client'),
(4, 1, 3, 1, 'Rejected', 0, 0, 0, 'Non disponible');

INSERT INTO blacklists (sid, type, reason, identifier) VALUES
(1, 'Users', 'Impolitesse lors d’une réservation', 'user@test.com'),
(1, 'Localisations', 'Lieu dangereux', 'Bruxelles Nord');

INSERT INTO logs (uid, action_name, log_description) VALUES
(2, 'Connexion', 'Connexion réussie'),
(3, 'Reservation', 'Nouvelle demande de réservation');

INSERT INTO messages (sender_id, receiver_id, message) VALUES
(3, 2, 'Bonjour, cette ressource est-elle disponible ?'),
(2, 3, 'Oui, dites-moi votre besoin.');

INSERT INTO avis (reviewer_id, evaluated_id, score, is_public, reason) VALUES
(3, 2, 9, TRUE, 'Très bonne expérience.');