SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO categories (title)
VALUES
('Рабы'), ('Наркотики'), ('Оружие'), ('Народные забавы');

INSERT INTO orders_statuses (title)
VALUES
('Сформирован');

INSERT INTO products (category_id, vendor_code, title, short_description, full_description, price)
VALUES
(1, '00000001', 'Woman \"BIG TITTIES\"', 'Naked woman with big tits', '', 8.00),
(2, '00000002', 'ZhigBeer', 'Zhigulevskoe beer', '', 16.00),
(3, '00000003', 'Chocobar \"Alyonka\"', 'Alyonka from Novovoronezh', '', 32.00),
(3, '00000004', 'Condoms \"Dikiy kaban\"', 'Really nice condoms \"Dikiy kaban\"', '', 64.00),
(1, '00000005', 'Black african slave', 'Strong african male', '', 1.00),
(2, '00000006', 'Bird\'s milk', 'Favorite commie candies', '', 128.00),
(3, '00000007', 'COVIDator', 'Fatal disinfectant. Applied once and forever!', '', 256.00),
(3, '00000008', 'Helicopter over sea tour', 'Finish your life with fun!', '', 512.00),
(2, '00000009', 'IQOS', 'Discover your homosexuality', '', 1024.00),
(1, '00000010', 'Mail-order bride', 'Feel like a colonizer! Turn your home into a third world', '', 2048.00),
(3, '00000011', 'MacBook', 'Overpriced laptop', '', 4096.00),
(3, '00000012', 'iPhone', 'Mediocre cellphone', '', 8192.00),
(3, '00000013', 'iPad', 'Too expensive tablet PC', '', 16384.00),
(2, '00000014', 'The Best of \"Butyrka\"', 'Delight for true music lovers', '', 32768.00),
(2, '00000015', '\"Hop, Musorok\" DVD', 'Biopic about the Vorovayki band members', '', 65536.00),
(2, '00000016', 'Uspeshniy uspekh', 'Mommy\'s businessman courses', '', 200000.00),
(4, '00000017', '\"Face in the salad\" mask', 'Enjoy the life of a gangster', '', 2.00),
(4, '00000018', 'Medal for bravery in online discussions', 'Homeland will remember you', '', 4.00),
(4, '00000019', 'One way tour to Ethiopia', 'Black lives DON\'T matter', '', 1.00),
(4, '00000020', 'Relocation assistance to any-stan', 'Invent a time machine. Let\'s go back to the past.', '', 2048.00);

INSERT INTO products_images (product_id, path)
VALUES
(2, '2.jpg');

INSERT INTO delivery_addresses (user_id, address)
VALUES
(1, 'ul. Mohovaya, 15s1, Moscow, 125009'),
(1, 'Beverly Hills, 90210'),
(2, 'La Mar 302, San Miguel, Lima, Peru'),
(2, 'Av. Portugal, 554 - Urca, Rio de Janeiro, Brazil'),
(3, 'R. Joao Alfredo Muler, 274, Foz do Iguacu, Brazil'),
(3, 'Calcena 574, Buenos Aires, Argentina');