-- // create users
-- Migration SQL that makes the change goes here.

CREATE TABLE users (
  id bigint NOT NULL AUTO_INCREMENT,
  name VARCHAR(20) NOT NULL ,
  age int NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- //@UNDO
-- SQL to undo the change goes here.
DROP TABLE users;


