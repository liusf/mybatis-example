-- // alter users add status
-- Migration SQL that makes the change goes here.

ALTER TABLE users ADD `status` tinyint NOT NULL;

-- //@UNDO
-- SQL to undo the change goes here.

ALTER TABLE users DROP `status`;


