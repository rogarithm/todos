DROP TABLE  if exists user;
DROP TABLE  if exists todo;

CREATE TABLE `user` (
  `id` bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `account` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nickname` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `crn` varchar(255) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT (now())
);

CREATE TABLE `todo` (
  `id` bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(2000) NOT NULL,
  `state` varchar(255) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT (now())
);