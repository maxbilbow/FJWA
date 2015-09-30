DROP TABLE IF EXISTS `authorities`;
DROP TABLE IF EXISTS `users`;

CREATE TABLE users(
  username VARCHAR(50) NOT NULL PRIMARY KEY,
  password VARCHAR(50) NOT NULL,
  enabled BOOLEAN NOT NULL
);

CREATE TABLE authorities (
  username VARCHAR(50) NOT NULL ,
  authority VARCHAR(50) NOT NULL ,
  CONSTRAINT fk_authorities_users
  FOREIGN KEY(username) REFERENCES users(username)
);

CREATE UNIQUE INDEX ix_auth_username on
  authorities (username, authority);

INSERT INTO users (username, password, enabled)
VALUES ("user", "password", true);

INSERT INTO authorities (username, authority)
    VALUES ("user", "USER");

INSERT INTO users (username, password, enabled)
VALUES ("admin", "secret", true);

INSERT INTO authorities (username, authority)
VALUES ("admin", "USER"), ("admin", "ADMIN");