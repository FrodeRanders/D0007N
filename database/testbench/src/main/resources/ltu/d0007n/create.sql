---------------------------------------------------------------
-- Creates objects (tables etc) corresponding to the database
-- schema for the Testbench application.
---------------------------------------------------------------

---------------------------------------------------------------
-- Database history
--
-- Administration operations, or actions, on the database are
-- logged to a history table.
--
-- Actions are:
--
--   0 - the initial creation of the database
--   1 - an upgrade of the database
--
--
-- Write initial entry in history table containing data
-- model version.
--
-- ============================================================
-- = OBSERVE: When modifying the schema, increase either the  =
-- =          major or minor version. This version does not   =
-- =          necessarily have to be in harmony with any      =
-- =          application using the database.                 =
-- ============================================================
--
CREATE TABLE db_history (
  occasion    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  action      INTEGER       NOT NULL, -- 0=init, 1=upgrade
  description VARCHAR(255)  NOT NULL,
  major       INTEGER       NOT NULL, -- major version
  minor       INTEGER       NOT NULL  -- minor version
);

INSERT INTO db_history (
  action,
  description,
  major,
  minor
) VALUES (
  0,                  -- 'init'
  'Initiating database with schema version 1.0', -- description
  1,                  -- major version
  0                   -- minor version
);


---------------------------------------------------------------
-- Events
--
CREATE TABLE events(
  eventid INTEGER NOT NULL, -- id of event 

  CONSTRAINT events_pk
    PRIMARY KEY (eventid),

  eventname VARCHAR(255) NOT NULL, -- name of event
  eventdate DATE NOT NULL, -- date of event

  modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


---------------------------------------------------------------
-- Persons
--
CREATE TABLE persons (
  personid INTEGER NOT NULL, -- id of person (could even be social security id)
  personname VARCHAR(255) NOT NULL, -- name of person registered for an event

  CONSTRAINT persons_pk
  PRIMARY KEY (personid), -- several persons can have same name!

  modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

---------------------------------------------------------------
-- Registrations
--
CREATE TABLE registrations (
  eventid INTEGER NOT NULL,  -- id of event
  personid INTEGER NOT NULL, -- id of person registered for an event

  CONSTRAINT registrations_pk
    PRIMARY KEY (eventid, personid),
	
  CONSTRAINT registrations_event_exists
    FOREIGN KEY (eventid) REFERENCES events(eventid),

  CONSTRAINT registrations_person_exists
    FOREIGN KEY (personid) REFERENCES persons(personid),
	
  modified TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
