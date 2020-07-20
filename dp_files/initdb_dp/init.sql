CREATE TABLE tbl_user (
  user_id BIGINT NOT NULL AUTO_INCREMENT,
  user_name VARCHAR(45) NULL,
  user_username VARCHAR(45) NULL,
  user_password VARCHAR(120) NULL,
  PRIMARY KEY (user_id));