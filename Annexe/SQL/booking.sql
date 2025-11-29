DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS slots;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    uid SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE slots (
    sid SERIAL PRIMARY KEY,
    slot_date DATE NOT NULL,
    time_start TIME NOT NULL,
    time_end TIME NOT NULL,
    capacity INT NOT NULL DEFAULT 1
);

CREATE UNIQUE INDEX unique_slot ON slots(slot_date, time_start);

CREATE TABLE reservations (
    rid SERIAL PRIMARY KEY,
    sid INT NOT NULL REFERENCES slots(sid) ON DELETE CASCADE,
    uid INT NOT NULL REFERENCES users(uid) ON DELETE CASCADE,
    nb_personnes INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);