INSERT INTO AUTHOR (LASTNAME, FIRSTNAME) VALUES ('Пушкин', 'Александр Сергеевич');
INSERT INTO AUTHOR (LASTNAME, FIRSTNAME) VALUES ('Толстой', 'Лев Николаевич');
INSERT INTO AUTHOR (LASTNAME, FIRSTNAME) VALUES ('Лермонтов', 'Михаил Юрьевич');
INSERT INTO AUTHOR (LASTNAME, FIRSTNAME) VALUES ('Ильф', 'Илья Арнольдович');
INSERT INTO AUTHOR (LASTNAME, FIRSTNAME) VALUES ('Петров', 'Евгений');

INSERT INTO GENRE (TITLE) VALUES ('Роман');
INSERT INTO GENRE (TITLE) VALUES ('Поэма');
INSERT INTO GENRE (TITLE) VALUES ('Эпопея');

INSERT INTO BOOK (TITLE) VALUES ('Евгений Онегин');
INSERT INTO BOOK_AUTHOR (BOOK_ID, AUTHOR_ID) VALUES (1, 1);
INSERT INTO BOOK_GENRE (BOOK_ID, GENRE_ID) VALUES (1, 1);
INSERT INTO BOOK_REVIEW (BOOK_ID, REVIEWER_NAME, RATING, COMMENT) VALUES (1, 'Дантес', 2, 'Так себе');

INSERT INTO BOOK (TITLE) VALUES ('Война и мир');
INSERT INTO BOOK_AUTHOR (BOOK_ID, AUTHOR_ID) VALUES (2, 2);
INSERT INTO BOOK_GENRE (BOOK_ID, GENRE_ID) VALUES (2, 1);
INSERT INTO BOOK_GENRE (BOOK_ID, GENRE_ID) VALUES (2, 3);
INSERT INTO BOOK_REVIEW (BOOK_ID, REVIEWER_NAME, RATING, COMMENT) VALUES (2, 'Вася Пупкин', 5, 'Не осилил');

INSERT INTO BOOK (TITLE) VALUES ('Герой нашего времени');
INSERT INTO BOOK_AUTHOR (BOOK_ID, AUTHOR_ID) VALUES (3, 3);
INSERT INTO BOOK_GENRE (BOOK_ID, GENRE_ID) VALUES (3, 2);

INSERT INTO BOOK (TITLE) VALUES ('Двенадцать стульев');
INSERT INTO BOOK_AUTHOR (BOOK_ID, AUTHOR_ID) VALUES (4, 4);
INSERT INTO BOOK_AUTHOR (BOOK_ID, AUTHOR_ID) VALUES (4, 5);
INSERT INTO BOOK_GENRE (BOOK_ID, GENRE_ID) VALUES (4, 1);

