SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO roles (name)
VALUES
('ROLE_EMPLOYEE'), ('ROLE_MANAGER'), ('ROLE_ADMIN');

INSERT INTO users (username, password, first_name, last_name, email, phone)
VALUES
/* mydickisbig */
('ogel', '$2y$12$sUJeGO5EVA6wdpmle3Q1ZOrXXXBhS8uT50pVxK4moUOdaNf3q4I2C', 'Oleg', 'Shokin', 'un_mapache_rabioso@hotmail.com', '911'),
/* myturbanisdirty */
('basilio', '$2y$12$g2SBkfX/L8axsnjARlDABOy1rPNs0f20f5WLnrjgh7Qod09DzHM86', 'Vasya', 'Lozhkin', 'vasya_lozhkin@sobaka.ru', '03'),
/* urmomlovesme */
('totsamiy', '$2y$12$XCidq4WiBrc/SDkmYv9CW.puMwrDftHo4H5SOd0exwmsUj9j7y3AW', 'Tot', 'Samiy', 'tot_samiy_syn_maminoy@podrugi.ru', '6-0-6-0-6');