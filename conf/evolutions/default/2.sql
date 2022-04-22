# --- !Ups
CREATE TABLE `schedules` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NOT NULL,
  `starts_at` DATETIME NOT NULL,
  `ends_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
);

# --- !Downs
DROP TABLE `schedules`;
