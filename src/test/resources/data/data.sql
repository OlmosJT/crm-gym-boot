-- 15 users added to entity;
INSERT INTO user_entity (firstname, lastname, username, address, date_of_birth, password, is_active)
VALUES
    ('John', 'Doe', 'john.doe', '123 Main St', '1990-01-01', 'password1', true),
    ('Jane', 'Doe', 'jane.doe', '456 Oak St', '1992-03-15', 'password2', true),
    ('Alice', 'Smith', 'alice.smith', '789 Elm St', '1985-05-20', 'password3', true),
    ('Bob', 'Johnson', 'bob.johnson', '101 Pine St', '1988-08-10', 'password4', true),
    ('Eva', 'Brown', 'eva.brown', '555 Maple St', '1995-12-05', 'password5', true),
    ('Michael', 'Williams', 'michael.williams', '777 Cedar St', '1982-09-22', 'password6', true),
    ('Sophia', 'Jones', 'sophia.jones', '888 Birch St', '1998-07-18', 'password7', true),
    ('David', 'Davis', 'david.davis', '222 Pine St', '1993-04-30', 'password8', true),
    ('Olivia', 'Miller', 'olivia.miller', '333 Oak St', '1987-11-12', 'password9', true),
    ('William', 'Anderson', 'william.anderson', '444 Elm St', '1996-06-08', 'password10', true),
    ('Emma', 'Garcia', 'emma.garcia', '666 Maple St', '1984-02-14', 'password11', true),
    ('James', 'Smith', 'james.smith', '777 Pine St', '1991-10-28', 'password12', true),
    ('Sophie', 'Clark', 'sophie.clark', '888 Oak St', '1989-07-01', 'password13', true),
    ('Daniel', 'Johnson', 'daniel.johnson', '999 Elm St', '1994-05-09', 'password14', true),
    ('Ava', 'Taylor', 'ava.taylor', '111 Maple St', '1986-03-25', 'password15', true);

-- 10 training types added to entity;
INSERT INTO training_type (name)
VALUES
    ('Cardio'),
    ('Strength Training'),
    ('Yoga'),
    ('Pilates'),
    ('CrossFit'),
    ('Zumba'),
    ('Spinning'),
    ('Martial Arts'),
    ('HIIT'),
    ('Functional Training');

-- 12 users are added as trainee
INSERT INTO trainee (user_entity_id)
VALUES
    (1), -- john.doe --
    (2), -- jane.doe --
    (3), -- alice.smith --
    (4), -- bob.johnson --
    (5), -- eva.brown --
    (6), -- michael.williams --
    (7), -- sophia.jones --
    (8), -- david.davis --
    (9), -- olivia.miller --
    (10), -- william.anderson --
    (11), -- emma.garcia --
    (12); -- james.smith --

-- 3 users are added as trainer
INSERT INTO trainer (specialization_id, user_entity_id)
VALUES
    (1, 13), -- trainingType: 'Cardio' spec 'sophie.clark' --
    (2, 14), -- trainingType: 'Strength Training' spec 'daniel.johnson' --
    (3, 15); -- trainingType: 'Yoga' spec 'ava.taylor'

-- 20 trainings added to the Training table
INSERT INTO training (duration, trainee_id, trainer_id, training_date, training_name)
VALUES
    (60, 1, 1, '2023-12-01 10:00:00', 'Cardio Training 1'),
    (45, 2, 2, '2023-12-02 14:30:00', 'Strength Training 1'),
    (60, 3, 3, '2023-12-03 11:15:00', 'Yoga Class 1'),
    (30, 4, 1, '2023-12-04 16:45:00', 'Cardio Training 2'),
    (60, 5, 2, '2023-12-05 09:30:00', 'Strength Training 2'),
    (45, 6, 3, '2023-12-06 13:00:00', 'Yoga Class 2'),
    (30, 7, 1, '2023-12-07 15:45:00', 'Cardio Training 3'),
    (60, 8, 2, '2023-12-08 10:30:00', 'Strength Training 3'),
    (45, 9, 3, '2023-12-09 14:00:00', 'Yoga Class 3'),
    (30, 10, 1, '2023-12-10 17:15:00', 'Cardio Training 4'),
    (60, 11, 2, '2023-12-11 12:45:00', 'Strength Training 4'),
    (45, 12, 3, '2023-12-12 10:30:00', 'Yoga Class 4'),
    (30, 1, 1, '2023-12-13 16:00:00', 'Cardio Training 5'),
    (60, 2, 2, '2023-12-14 11:15:00', 'Strength Training 5'),
    (45, 3, 3, '2023-12-15 14:30:00', 'Yoga Class 5'),
    (30, 4, 1, '2023-12-16 17:45:00', 'Cardio Training 6'),
    (60, 5, 2, '2023-12-17 09:00:00', 'Strength Training 6'),
    (45, 6, 3, '2023-12-18 13:30:00', 'Yoga Class 6'),
    (30, 7, 1, '2023-12-19 15:15:00', 'Cardio Training 7'),
    (60, 8, 2, '2023-12-20 10:45:00', 'Strength Training 7');

