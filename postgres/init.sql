CREATE SCHEMA ticket_service;
CREATE SCHEMA flight_service;
CREATE SCHEMA privilege_service;


/* flight service */
ALTER SCHEMA flight_service OWNER TO postgres;
CREATE TABLE flight_service.airport
(
    id      integer NOT NULL,
    city    character varying(255),
    country character varying(255),
    name    character varying(255)
);
ALTER TABLE flight_service.airport
    OWNER TO postgres;

CREATE TABLE flight_service.flight
(
    id              integer NOT NULL,
    date_time       character varying(255),
    flight_number   character varying(255),
    from_airport_id integer NOT NULL,
    price           integer NOT NULL,
    to_airport_id   integer NOT NULL
);
ALTER TABLE flight_service.flight
    OWNER TO postgres;

ALTER TABLE ONLY flight_service.airport
    ADD CONSTRAINT airport_pkey PRIMARY KEY (id);

ALTER TABLE ONLY flight_service.flight
    ADD CONSTRAINT flight_pkey PRIMARY KEY (id);

ALTER TABLE ONLY flight_service.flight
    ADD CONSTRAINT from_airport_id FOREIGN KEY (from_airport_id) REFERENCES flight_service.airport (id);

ALTER TABLE ONLY flight_service.flight
    ADD CONSTRAINT to_airport_id FOREIGN KEY (to_airport_id) REFERENCES flight_service.airport (id);

INSERT INTO flight_service.airport
VALUES (1, 'Москва', 'Россия', 'Шереметьево');
INSERT INTO flight_service.airport
VALUES (2, 'Санкт-Петербург', 'Россия', 'Пулково');
INSERT INTO flight_service.flight
VALUES (1, '2021-10-08 20:00:00', 'AFL031', 2, 1500, 1);


/* ticket service */
ALTER SCHEMA ticket_service OWNER TO postgres;
CREATE TABLE ticket_service.ticket
(
    id            integer NOT NULL,
    flight_number character varying(255),
    price         integer NOT NULL,
    status        character varying(255),
    ticket_uid    uuid,
    username      character varying(255)
);
ALTER TABLE ticket_service.ticket
    OWNER TO postgres;
ALTER TABLE ONLY ticket_service.ticket
    ADD CONSTRAINT ticket_pkey PRIMARY KEY (id);


/* bonus service */
ALTER SCHEMA privilege_service OWNER TO postgres;
CREATE TABLE privilege_service.privilege
(
    id       integer NOT NULL,
    balance  integer NOT NULL,
    status   character varying(255),
    username character varying(255)
);
ALTER TABLE privilege_service.privilege
    OWNER TO postgres;

CREATE TABLE privilege_service.privilege_history
(
    id             integer NOT NULL,
    balance_diff   integer NOT NULL,
    datetime       character varying(255),
    operation_type character varying(255),
    ticket_uid     uuid,
    privilege_id   integer
);
ALTER TABLE privilege_service.privilege_history
    OWNER TO postgres;

ALTER TABLE ONLY privilege_service.privilege_history
    ADD CONSTRAINT privilege_history_pkey PRIMARY KEY (id);
ALTER TABLE ONLY privilege_service.privilege
    ADD CONSTRAINT privilege_pkey PRIMARY KEY (id);
ALTER TABLE ONLY privilege_service.privilege_history
    ADD CONSTRAINT fkrqyar142oomkttl1ggfmtkdyc FOREIGN KEY (privilege_id) REFERENCES privilege_service.privilege (id);

INSERT INTO privilege_service.privilege
VALUES (1, 0, 'GOLD', 'Test Max');
