DROP TABLE IF EXISTS bookings;

CREATE TABLE bookings (
    bid SERIAL PRIMARY KEY,
    date DATE NOT NULL,
    time_start TIME NOT NULL,
    time_end TIME NOT NULL,
    customer_name VARCHAR(255) NOT NULL,
    nb_personnes INT NOT NULL
);
