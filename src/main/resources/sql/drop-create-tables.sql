DROP TABLE card_table_card;
DROP TABLE card_table;
DROP TABLE pack_card;
DROP TABLE pack;
DROP TABLE card;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE card (
    id integer NOT NULL,
    value character varying(2) NOT NULL,
    front_image character varying(128) NOT NULL
);

CREATE TABLE pack (
    id integer NOT NULL,
    name character varying(128) NOT NULL,
    back_image character varying(128) NOT NULL
);

CREATE TABLE pack_card (
    pack_id integer NOT NULL,
    card_id integer NOT NULL
);

CREATE TABLE card_table (
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    created timestamp NOT NULL
);

CREATE TABLE card_table_card (
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    card_table_id uuid NOT NULL,
    card_id integer NOT NULL,
    pack_id integer NOT NULL,   
    x_position integer NOT NULL,
    y_position integer NOT NULL,
    z_position integer NOT NULL,
    face_down boolean DEFAULT true NOT NULL,
    player_id uuid
);

ALTER TABLE card ADD CONSTRAINT card_pkey PRIMARY KEY (id);

ALTER TABLE pack ADD CONSTRAINT pack_pkey PRIMARY KEY (id);
ALTER TABLE pack ADD CONSTRAINT pack_name_key UNIQUE (name);

ALTER TABLE pack_card ADD CONSTRAINT pack_card_pkey PRIMARY KEY (pack_id, card_id);
ALTER TABLE pack_card ADD CONSTRAINT pack_card_pack_id_fkey FOREIGN KEY (pack_id) REFERENCES pack(id);
ALTER TABLE pack_card ADD CONSTRAINT pack_card_card_id_fkey FOREIGN KEY (card_id) REFERENCES card(id);

ALTER TABLE card_table ADD CONSTRAINT card_table_pkey PRIMARY KEY (id);

ALTER TABLE card_table_card ADD CONSTRAINT card_table_card_pkey PRIMARY KEY (id);
ALTER TABLE card_table_card ADD CONSTRAINT card_table_card_card_table_id_fkey FOREIGN KEY (card_table_id) REFERENCES card_table(id);
ALTER TABLE card_table_card ADD CONSTRAINT card_table_card_card_id_fkey FOREIGN KEY (card_id) REFERENCES card(id);
ALTER TABLE card_table_card ADD CONSTRAINT card_table_card_pack_id_fkey FOREIGN KEY (pack_id) REFERENCES pack(id);
CREATE INDEX card_table_card_z_position_idx ON card_table_card USING btree (z_position);

-- Standard 52 card deck

-- pack
INSERT INTO pack (id, back_image, name) VALUES (1, 'standard/blackback.svg', 'Standard 52 card deck (Black)');
INSERT INTO pack (id, back_image, name) VALUES (2, 'standard/redback.svg', 'Standard 52 card deck (Red)');

-- clubs
INSERT INTO card (id, front_image, value) VALUES (1, 'standard/AC.svg', 'AC');
INSERT INTO card (id, front_image, value) VALUES (2, 'standard/2C.svg', '2C');
INSERT INTO card (id, front_image, value) VALUES (3, 'standard/3C.svg', '3C');
INSERT INTO card (id, front_image, value) VALUES (4, 'standard/4C.svg', '4C');
INSERT INTO card (id, front_image, value) VALUES (5, 'standard/5C.svg', '5C');
INSERT INTO card (id, front_image, value) VALUES (6, 'standard/6C.svg', '6C');
INSERT INTO card (id, front_image, value) VALUES (7, 'standard/7C.svg', '7C');
INSERT INTO card (id, front_image, value) VALUES (8, 'standard/8C.svg', '8C');
INSERT INTO card (id, front_image, value) VALUES (9, 'standard/9C.svg', '9C');
INSERT INTO card (id, front_image, value) VALUES (10, 'standard/TC.svg', 'TC');
INSERT INTO card (id, front_image, value) VALUES (11, 'standard/JC.svg', 'JC');
INSERT INTO card (id, front_image, value) VALUES (12, 'standard/QC.svg', 'QC');
INSERT INTO card (id, front_image, value) VALUES (13, 'standard/KC.svg', 'KC');
-- diamonds
INSERT INTO card (id, front_image, value) VALUES (14, 'standard/AD.svg', 'AD');
INSERT INTO card (id, front_image, value) VALUES (15, 'standard/2D.svg', '2D');
INSERT INTO card (id, front_image, value) VALUES (16, 'standard/3D.svg', '3D');
INSERT INTO card (id, front_image, value) VALUES (17, 'standard/4D.svg', '4D');
INSERT INTO card (id, front_image, value) VALUES (18, 'standard/5D.svg', '5D');
INSERT INTO card (id, front_image, value) VALUES (19, 'standard/6D.svg', '6D');
INSERT INTO card (id, front_image, value) VALUES (20, 'standard/7D.svg', '7D');
INSERT INTO card (id, front_image, value) VALUES (21, 'standard/8D.svg', '8D');
INSERT INTO card (id, front_image, value) VALUES (22, 'standard/9D.svg', '9D');
INSERT INTO card (id, front_image, value) VALUES (23, 'standard/TD.svg', 'TD');
INSERT INTO card (id, front_image, value) VALUES (24, 'standard/JD.svg', 'JD');
INSERT INTO card (id, front_image, value) VALUES (25, 'standard/QD.svg', 'QD');
INSERT INTO card (id, front_image, value) VALUES (26, 'standard/KD.svg', 'KD');
-- hearts
INSERT INTO card (id, front_image, value) VALUES (27, 'standard/AH.svg', 'AH');
INSERT INTO card (id, front_image, value) VALUES (28, 'standard/2H.svg', '2H');
INSERT INTO card (id, front_image, value) VALUES (29, 'standard/3H.svg', '3H');
INSERT INTO card (id, front_image, value) VALUES (30, 'standard/4H.svg', '4H');
INSERT INTO card (id, front_image, value) VALUES (31, 'standard/5H.svg', '5H');
INSERT INTO card (id, front_image, value) VALUES (32, 'standard/6H.svg', '6H');
INSERT INTO card (id, front_image, value) VALUES (33, 'standard/7H.svg', '7H');
INSERT INTO card (id, front_image, value) VALUES (34, 'standard/8H.svg', '8H');
INSERT INTO card (id, front_image, value) VALUES (35, 'standard/9H.svg', '9H');
INSERT INTO card (id, front_image, value) VALUES (36, 'standard/TH.svg', 'TH');
INSERT INTO card (id, front_image, value) VALUES (37, 'standard/JH.svg', 'JH');
INSERT INTO card (id, front_image, value) VALUES (38, 'standard/QH.svg', 'QH');
INSERT INTO card (id, front_image, value) VALUES (39, 'standard/KH.svg', 'KH');
-- spades
INSERT INTO card (id, front_image, value) VALUES (40, 'standard/AS.svg', 'AS');
INSERT INTO card (id, front_image, value) VALUES (41, 'standard/2S.svg', '2S');
INSERT INTO card (id, front_image, value) VALUES (42, 'standard/3S.svg', '3S');
INSERT INTO card (id, front_image, value) VALUES (43, 'standard/4S.svg', '4S');
INSERT INTO card (id, front_image, value) VALUES (44, 'standard/5S.svg', '5S');
INSERT INTO card (id, front_image, value) VALUES (45, 'standard/6S.svg', '6S');
INSERT INTO card (id, front_image, value) VALUES (46, 'standard/7S.svg', '7S');
INSERT INTO card (id, front_image, value) VALUES (47, 'standard/8S.svg', '8S');
INSERT INTO card (id, front_image, value) VALUES (48, 'standard/9S.svg', '9S');
INSERT INTO card (id, front_image, value) VALUES (49, 'standard/TS.svg', 'TS');
INSERT INTO card (id, front_image, value) VALUES (50, 'standard/JS.svg', 'JS');
INSERT INTO card (id, front_image, value) VALUES (51, 'standard/QS.svg', 'QS');
INSERT INTO card (id, front_image, value) VALUES (52, 'standard/KS.svg', 'KS');

-- black pack
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 1);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 2);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 3);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 4);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 5);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 6);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 7);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 8);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 9);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 10);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 11);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 12);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 13);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 14);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 15);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 16);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 17);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 18);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 19);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 20);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 21);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 22);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 23);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 24);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 25);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 26);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 27);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 28);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 29);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 30);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 31);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 32);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 33);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 34);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 35);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 36);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 37);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 38);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 39);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 40);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 41);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 42);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 43);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 44);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 45);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 46);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 47);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 48);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 49);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 50);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 51);
INSERT INTO pack_card (pack_id, card_id) VALUES (1, 52);

-- red pack
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 1);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 2);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 3);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 4);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 5);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 6);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 7);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 8);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 9);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 10);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 11);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 12);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 13);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 14);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 15);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 16);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 17);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 18);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 19);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 20);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 21);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 22);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 23);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 24);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 25);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 26);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 27);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 28);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 29);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 30);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 31);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 32);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 33);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 34);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 35);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 36);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 37);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 38);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 39);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 40);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 41);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 42);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 43);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 44);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 45);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 46);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 47);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 48);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 49);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 50);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 51);
INSERT INTO pack_card (pack_id, card_id) VALUES (2, 52);
