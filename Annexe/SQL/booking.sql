DROP TABLE IF EXISTS bookings;

CREATE TABLE creneaux (
    cid SERIAL PRIMARY KEY,
    date DATE NOT NULL,
    time_start TIME NOT NULL,
    time_end TIME NOT NULL,
);

CREATE TABLE users (
    uid SERIAL PRIMARY KEY
)


CREATE TABLE bookings (
    cid INTEGER NOT NULL REFERENCES creneaux(uid) ON DELETE CASCADE,
    uid INTEGER NOT NULL REFERENCES users(uid) ON DELETE CASCADE,
    nb_personnes INT NOT NULL
);
