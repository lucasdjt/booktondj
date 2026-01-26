INSERT INTO users(name, email, pwd, role) VALUES ('admin', 'dcinutile1@gmail.com', '21232f297a57a5a743894a0e4a801fc3', 'ADMIN');
INSERT INTO users(name, email, pwd, role) VALUES ('test',  'dcinutile2@gmail.com', '098f6bcd4621d373cade4e832627b4f6', 'USER');

-- -- TEST MEDECIN AVEC 3 USERS EN + ET 50 RESERVATIONS (activé avec medecin.properties)
-- INSERT INTO users (name, email, pwd, role, profile_image) VALUES
-- ('alice', 'alice@mail.fr', '6384e2b2184bcbf58eccf10ca7a6563c', 'USER', NULL), -- MDP alice
-- ('alex', 'alex@mail.fr', '534b44a19bf18d20b71ecc4eb77c572f', 'USER', NULL), -- MDP alex
-- ('bob', 'bob@mail.fr', '9f9d51bc70ef21ca5c14f307980a29d8', 'USER', NULL); -- MDP bob

-- INSERT INTO slots(slot_date, time_start, time_end, capacity)
-- SELECT
--   DATEADD('DAY', d.x, CURRENT_DATE) AS slot_date,
--   DATEADD('MINUTE', s.x * 5, TIME '09:00:00') AS time_start,
--   DATEADD('MINUTE', (s.x + 1) * 5, TIME '09:00:00') AS time_end,
--   1 AS capacity
-- FROM SYSTEM_RANGE(0, 4) d
-- CROSS JOIN SYSTEM_RANGE(0, 15) s; 

-- INSERT INTO reservations(sid, uid, nb_personnes, created_at)
-- SELECT
--   sl.sid,
--   u.uid,
--   1,
--   CURRENT_TIMESTAMP
-- FROM (
--   SELECT sid, ROW_NUMBER() OVER (ORDER BY slot_date, time_start) AS rn
--   FROM slots
-- ) sl
-- JOIN (
--   SELECT uid, ROW_NUMBER() OVER (
--     ORDER BY CASE name
--       WHEN 'admin' THEN 1
--       WHEN 'test'  THEN 2
--       WHEN 'alice' THEN 3
--       WHEN 'alex'  THEN 4
--       WHEN 'bob'   THEN 5
--     END
--   ) AS un
--   FROM users
--   WHERE name IN ('admin','test','alice','alex','bob')
-- ) u
-- ON u.un = FLOOR((sl.rn - 1) / 10) + 1
-- WHERE sl.rn <= 50;