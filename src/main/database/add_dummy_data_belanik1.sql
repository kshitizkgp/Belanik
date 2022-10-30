# Insert Categories
INSERT INTO `Belanik1`.`Category`
(`category_id`,
`name`,
`parent`)
VALUES
("c1","length",""), ("c2","type",""),
("c3","short","c1"), ("c4","medium","c1"), ("c5","long","c1"),
("c6","straight","c2"), ("c7","wavy","c2"), ("c8","curly","c2");

# Insert Post 1
INSERT INTO `Belanik1`.`Post`
(`post_id`,
`author_id`,
`title`,
`created_timestamp`,
`last_modified_timestamp`,
`categories`,
`custom_categories`,
`media`,
`description`,
`likes`)
VALUES
("p1","u1","How to control frizzy hair?","2022-10-26 12:45:56", "2022-10-27 10:46:02", '{"category_id": ["c5", "c3"]}', '{"category_name": ["wavy"]}',
'{"video": "https://www.youtube.com/embed/kgUpRwMeRr4"}', "This is how you should do it....",
'{"count": 1, "user_activity": [{"user_id": "u2", "modified_timestamp": "2022-10-27 08:27:00"}]}');

# Insert Post 2
INSERT INTO `Belanik1`.`Post`
(`post_id`,
`author_id`,
`title`,
`created_timestamp`,
`last_modified_timestamp`,
`categories`,
`custom_categories`,
`media`,
`description`,
`likes`)
VALUES
("p2","u1","How to control dandruff?","2022-10-12 00:45:56", "2022-10-17 19:46:02", '{"category_id": ["c2", "c4"]}', '{"category_name": ["dandruff", "shampoo"]}',
'{"images": ["https://i.ibb.co/SymxjyS/pablo-merchan-montes-O-s-NTav-Xbm-E-unsplash.jpg", "https://i.ibb.co/7V7Twvq/kyle-smith-4q-YHq-Qqlw-M4-unsplash.jpg"]}',
"This is how you control your dandruff with me :)", '{}');

# Insert Post 3
INSERT INTO `Belanik1`.`Post`
(`post_id`,
`author_id`,
`title`,
`created_timestamp`,
`last_modified_timestamp`,
`categories`,
`custom_categories`,
`media`,
`description`,
`likes`)
VALUES
("p3","u2","How to prevent hairfall?","2022-09-12 00:45:56", "2022-10-17 19:46:02", '{"category_id": ["c1", "c6", "c4"]}', '{"category_name": ["hairfall"]}',
'{"images": ["https://i.ibb.co/SymxjyS/pablo-merchan-montes-O-s-NTav-Xbm-E-unsplash.jpg"]}',
"This is how you prevent hairfall)", '{"count": 1, "user_activity": [{"user_id": "u1", "modified_timestamp": "2022-09-28 18:27:40"}]}');


SELECT * FROM `Belanik1`.`Post`;

# Insert Users
INSERT INTO `Belanik1`.`User`
(`user_id`,
`name`,
`email_id`,
`created_timestamp`,
`contact_number`,
`profile_picture`,
`bio`,
`birthday`,
`gender`,
`liked_posts`)
VALUES
("u1","Sayoni Dutta Roy","sayonidroy@gmail.com", "2022-08-01 00:28:22", "+16505370344", "", "Hi! This is SDR", "1995-11-10", "female", '{"post_id": ["p3"]}'),
("u2","Kshitiz Kumar","kshitiz.kgp@gmail.com", "2022-08-05 05:28:22", "+16505370384", "", "Hi! This is KKR", "1995-10-08", "male", '{"post_id": ["p1"]}');
