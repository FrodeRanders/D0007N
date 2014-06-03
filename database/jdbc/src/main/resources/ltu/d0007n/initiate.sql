---------------------------------------------------------------
-- This is default data - expected to be present in a newly
-- created database access demo database
---------------------------------------------------------------

---------------------------------------------------------------
-- Initiate database
--
INSERT INTO courses (id, name) VALUES (0, 'D0007N');
INSERT INTO students (id, name) VALUES (0, 'Frode'); -- Frode är en nolla :)
INSERT INTO students (id, name) VALUES (1, 'Ingemar');

INSERT INTO admittances (courseid, studentid) VALUES (0, 0); -- Frode is admitted to course
INSERT INTO admittances (courseid, studentid) VALUES (0, 1); -- Ingemar is admitted to course

