INSERT INTO users(name, email, pwd, role) VALUES ('admin', 'dcinutile1@gmail.com', '21232f297a57a5a743894a0e4a801fc3', 'ADMIN');
INSERT INTO users(name, email, pwd, role) VALUES ('test',  'dcinutile2@gmail.com', '098f6bcd4621d373cade4e832627b4f6', 'USER');

--TEST MEDECIN AVEC 3 USERS EN + ET 50 RESERVATIONS
INSERT INTO users (name, email, pwd, role, profile_image) VALUES
('alice', 'alice@mail.fr', '6384e2b2184bcbf58eccf10ca7a6563c', 'USER', NULL), -- MDP alice
('alex', 'alex@mail.fr', '534b44a19bf18d20b71ecc4eb77c572f', 'USER', NULL), -- MDP alex
('bob', 'bob@mail.fr', '9f9d51bc70ef21ca5c14f307980a29d8', 'USER', NULL); -- MDP bob