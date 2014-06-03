---------------------------------------------------------------
-- Creates objects (tables etc) corresponding to the database
-- schema for the database access demo application.
---------------------------------------------------------------


---------------------------------------------------------------
-- Courses
--
CREATE TABLE courses (
  id INTEGER NOT NULL,
  CONSTRAINT course_pk
    PRIMARY KEY (id),

  name VARCHAR(255) NOT NULL
);

---------------------------------------------------------------
-- Students
--
CREATE TABLE students (
  id INTEGER NOT NULL,
  CONSTRAINT student_pk
    PRIMARY KEY (id),

  name VARCHAR(255) NOT NULL
);

---------------------------------------------------------------
-- Students admitted to courses
--
CREATE TABLE admittances (
  courseid INTEGER NOT NULL,
  studentid INTEGER NOT NULL,

  CONSTRAINT admittance_pk
    PRIMARY KEY (courseid, studentid),

  CONSTRAINT course_ex
    FOREIGN KEY (courseid) REFERENCES courses(id),

  CONSTRAINT student_ex
    FOREIGN KEY (studentid) REFERENCES students(id)
);
