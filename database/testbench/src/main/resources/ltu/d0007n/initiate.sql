---------------------------------------------------------------
-- This is default data - expected to be present in a newly
-- created Testbench database (at least for testing purposes)
---------------------------------------------------------------

---------------------------------------------------------------
-- Initiate database with extremely important events...
--
INSERT INTO events (eventid, eventname, eventdate) VALUES (1, 'Sista datum för anmälan till tenta D0007N', '2014-05-16');
INSERT INTO events (eventid, eventname, eventdate) VALUES (2, 'Norges grundlag 200 år', '2014-05-17'); -- OBS Viktigt!
INSERT INTO events (eventid, eventname, eventdate) VALUES (3, 'Tenta D0007N', '2014-06-03');
INSERT INTO events (eventid, eventname, eventdate) VALUES (4, 'Frode 50 år', '2017-09-27'); -- Mycket viktigt! :)


INSERT INTO persons (personid, personname) VALUES (1, 'Ingemar Andersson');
INSERT INTO persons (personid, personname) VALUES (2, 'Frode Randers');
INSERT INTO persons (personid, personname) VALUES (3, 'Carl-Magnus Helgegren');


INSERT INTO registrations (eventid, personid) VALUES (3, 3);
--  ...

INSERT INTO registrations (eventid, personid) VALUES (4, 1);
INSERT INTO registrations (eventid, personid) VALUES (4, 2);

