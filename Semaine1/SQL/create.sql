DROP TABLE IF EXISTS avis;
DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS logs;
DROP TABLE IF EXISTS blacklists;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS plannings;
DROP TABLE IF EXISTS services;
DROP TABLE IF EXISTS localisations;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    uid SERIAL PRIMARY KEY,
    pseudo VARCHAR(151) NOT NULL,
    nom VARCHAR(100) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    role VARCHAR(8) NOT NULL CHECK (role IN ('User','Admin','Service')),
    verified BOOLEAN NOT NULL DEFAULT FALSE,
    creation_date DATE NOT NULL,
    tel VARCHAR(50),
    notif CHAR(5) CHECK (notif IN ('Email','Phone')),
    description VARCHAR(255)
);

CREATE TABLE localisations (
    lid SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    address VARCHAR(255) NOT NULL
);

CREATE TABLE services (
    sid SERIAL PRIMARY KEY,
    uid INTEGER NOT NULL REFERENCES users(uid) ON DELETE CASCADE,
    lid INTEGER REFERENCES localisations(lid) ON DELETE SET NULL,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    capacity INTEGER NOT NULL DEFAULT 0,
    description VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    price DECIMAL(10,2)
);

CREATE TABLE plannings (
    pid SERIAL PRIMARY KEY,
    sid INTEGER NOT NULL REFERENCES services(sid) ON DELETE CASCADE,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    is_available BOOLEAN NOT NULL DEFAULT FALSE,
    is_note_public BOOLEAN NOT NULL DEFAULT FALSE,
    planning_note VARCHAR(255),
    special_price DECIMAL(10,2)
);

CREATE TABLE bookings (
    bid SERIAL PRIMARY KEY,
    uid INTEGER NOT NULL REFERENCES users(uid) ON DELETE CASCADE,
    sid INTEGER NOT NULL REFERENCES services(sid) ON DELETE CASCADE,
    pid INTEGER NOT NULL REFERENCES plannings(pid) ON DELETE CASCADE,
    lid INTEGER REFERENCES localisations(lid) ON DELETE SET NULL,
    status VARCHAR(50) NOT NULL CHECK (status IN ('Completed','Pending','Rejected','Cancelled')),
    created_date TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_date TIMESTAMP NOT NULL DEFAULT NOW(),
    nb_participants INTEGER NOT NULL DEFAULT 0,
    amount DECIMAL(10,2) NOT NULL DEFAULT 0,
    amount_required DECIMAL(10,2) NOT NULL DEFAULT 0,
    note VARCHAR(255)
);

CREATE TABLE blacklists (
    ban_id SERIAL PRIMARY KEY,
    sid INTEGER NOT NULL REFERENCES services(sid) ON DELETE CASCADE,
    type VARCHAR(30) NOT NULL CHECK (type IN ('Users','Localisations','Others')),
    reason VARCHAR(255) NOT NULL,
    identifier VARCHAR(255) NOT NULL,
    ban_date TIMESTAMP NOT NULL DEFAULT NOW(),
    end_date TIMESTAMP
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