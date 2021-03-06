SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO roles (name)
VALUES
('ROLE_EMPLOYEE'), ('ROLE_MANAGER'), ('ROLE_ADMIN');

INSERT INTO users (username, password, first_name, last_name, email, phone)
VALUES
/* 123 */
('ogel', '$2a$10$QJnxfYhKO/hgoMU1jl7q2Oi6JrmoLfP7cK5LtG9v4mheObEGq2jKS', 'Oleg', 'Shokin', 'un_mapache_rabioso@hotmail.com', '911'),
/* 123 */
('basilio', '$2a$10$QJnxfYhKO/hgoMU1jl7q2Oi6JrmoLfP7cK5LtG9v4mheObEGq2jKS', 'Vasya', 'Lozhkin', 'vasya_lozhkin@sobaka.ru', '03'),
/* 123 */
('totsamiy', '$2a$10$QJnxfYhKO/hgoMU1jl7q2Oi6JrmoLfP7cK5LtG9v4mheObEGq2jKS', 'Tot', 'Samiy', 'tot_samiy_syn_maminoy@podrugi.ru', '6-0-6-0-6');