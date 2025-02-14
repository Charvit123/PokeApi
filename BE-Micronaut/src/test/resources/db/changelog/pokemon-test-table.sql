--liquibase formatted sql

-- changeset Charvit:pokemon-test-table
CREATE TABLE IF NOT EXISTS pokemon (
    id BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(100) NOT NULL,
    power VARCHAR(100) NULL,
    imageUrl VARCHAR(100) NULL,
    CONSTRAINT pokemon_pk PRIMARY KEY (id),
    CONSTRAINT pokemon_name_unique UNIQUE KEY (name)
)
ENGINE=InnoDB
DEFAULT CHARSET=latin1
COLLATE=latin1_swedish_ci;

-- changeset Charvit:pokemon-test-table-changed-datatypesize
ALTER TABLE pokemon MODIFY COLUMN imageUrl VARCHAR(500) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL;

-- changeset Charvit:master-test-data-power-created
CREATE TABLE IF NOT EXISTS power (
    id BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(100) NOT NULL,
    CONSTRAINT power_pk PRIMARY KEY (id),
    CONSTRAINT power_un UNIQUE KEY (name)
)
ENGINE=InnoDB
DEFAULT CHARSET=latin1
COLLATE=latin1_swedish_ci;

-- changeset Charvit:pokemon-test-table-power-deleted
ALTER TABLE pokemon DROP COLUMN IF EXISTS power;

-- changeset Charvit:pokemon-test-table-power-added
ALTER TABLE pokemon ADD COLUMN power BIGINT NULL;

-- changeset Charvit:pokemon-test-table-fk-connect-master-power
ALTER TABLE pokemon ADD CONSTRAINT pokemon_FK FOREIGN KEY (power) REFERENCES power(id);

-- changeset Charvit:master-test-data-power-inserted
INSERT INTO power (name)
    VALUES ('Grass'), ('Fire'), ('Water'), ('Poison')
    ON DUPLICATE KEY UPDATE name = name;