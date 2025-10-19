DROP TABLE IF EXISTS avis;
DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS logs;
DROP TABLE IF EXISTS blacklists;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS localisations;
DROP TABLE IF EXISTS plannings;
DROP TABLE IF EXISTS artists;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    uid SERIAL PRIMARY KEY,
    pseudo VARCHAR(30) NOT NULL,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    role VARCHAR(7) NOT NULL CHECK (role IN ('User','Manager','Artist','Admin')),
    verified BOOLEAN NOT NULL DEFAULT FALSE,
    creation_date DATE NOT NULL,
    tel VARCHAR(50),
    notif CHAR(5) CHECK (notif IN ('Email','Phone')),
    popularity INTEGER,
    description VARCHAR(255)
);

CREATE TABLE artists (
    aid SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL UNIQUE REFERENCES users(uid) ON DELETE CASCADE,
    manager_id INTEGER REFERENCES users(uid) ON DELETE SET NULL,
    genre VARCHAR(50) NOT NULL,
    valid_auto BOOLEAN NOT NULL DEFAULT FALSE,
    is_fee_prv BOOLEAN NOT NULL DEFAULT FALSE,
    is_minor BOOLEAN NOT NULL DEFAULT FALSE,
    rest_hours_needed INTEGER NOT NULL DEFAULT 0,
    max_hours_playings INTEGER NOT NULL DEFAULT 0,
    min_fee INTEGER,
    km_max INTEGER,
    min_participants INTEGER,
    max_reservations INTEGER
);

CREATE TABLE plannings (
    pid SERIAL PRIMARY KEY,
    aid INTEGER NOT NULL REFERENCES artists(aid) ON DELETE CASCADE,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    is_occupied BOOLEAN NOT NULL DEFAULT FALSE,
    is_note_public BOOLEAN NOT NULL DEFAULT FALSE,
    special_fee DECIMAL(10,2),
    planning_note VARCHAR(255)
);

CREATE TABLE localisations (
    lid SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    localisation_type VARCHAR(20) NOT NULL CHECK (localisation_type IN ('Room','Outside','Bar','Nightclub','Festival','Other')),
    address VARCHAR(255) NOT NULL,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION
);

CREATE TABLE bookings (
    bid SERIAL PRIMARY KEY,
    uid INTEGER NOT NULL REFERENCES users(uid) ON DELETE CASCADE,
    aid INTEGER NOT NULL REFERENCES artists(aid) ON DELETE CASCADE,
    pid INTEGER NOT NULL REFERENCES plannings(pid) ON DELETE CASCADE,
    lid INTEGER REFERENCES localisations(lid) ON DELETE SET NULL,
    status VARCHAR(50) NOT NULL CHECK (status IN ('Pending','Confirmed','Confirmed without payment','Rejected','Returned','Cancelled','Ended')),
    created_date TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_date TIMESTAMP NOT NULL DEFAULT NOW(),
    nb_participants INTEGER NOT NULL DEFAULT 0,
    annulation_delay TIMESTAMP NOT NULL,
    include_technicals BOOLEAN NOT NULL DEFAULT FALSE,
    actual_amount DECIMAL(10,2) NOT NULL DEFAULT 0,
    amount_required DECIMAL(10,2) NOT NULL DEFAULT 0,
    booking_note VARCHAR(255),
    dj_list VARCHAR(255),
    amount_cancel DECIMAL(10,2),
    public_reveal_date TIMESTAMP
);

CREATE TABLE blacklists (
    ban_id SERIAL PRIMARY KEY,
    artist_id INTEGER NOT NULL REFERENCES artists(aid) ON DELETE CASCADE,
    blacklist_type VARCHAR(30) NOT NULL CHECK (blacklist_type IN ('Event','User','Location','Organisation','Other')),
    blacklist_reason VARCHAR(255) NOT NULL,
    blacklist_name VARCHAR(100) NOT NULL,
    blacklist_date TIMESTAMP NOT NULL DEFAULT NOW(),
    end_date_blacklist TIMESTAMP
);

CREATE TABLE logs (
    log_id SERIAL PRIMARY KEY,
    uid INTEGER NOT NULL REFERENCES users(uid) ON DELETE CASCADE,
    action_name VARCHAR(100) NOT NULL,
    log_date TIMESTAMP NOT NULL DEFAULT NOW(),
    log_description VARCHAR(255)
);

CREATE TABLE messages (
    mid SERIAL PRIMARY KEY,
    sender_id INTEGER NOT NULL REFERENCES users(uid) ON DELETE CASCADE,
    receiver_id INTEGER NOT NULL REFERENCES users(uid) ON DELETE CASCADE,
    message VARCHAR(255) NOT NULL,
    sent_date TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE avis (
    reviewer_id INTEGER NOT NULL REFERENCES users(uid) ON DELETE CASCADE,
    evaluated_id INTEGER NOT NULL REFERENCES users(uid) ON DELETE CASCADE,
    score INTEGER NOT NULL CHECK (score BETWEEN 0 AND 10),
    is_public BOOLEAN NOT NULL DEFAULT FALSE,
    posted_date TIMESTAMP NOT NULL DEFAULT NOW(),
    reason VARCHAR(255),
    PRIMARY KEY (reviewer_id, evaluated_id),
    CHECK (reviewer_id <> evaluated_id)
);