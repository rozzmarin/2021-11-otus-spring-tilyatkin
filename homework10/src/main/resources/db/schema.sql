DROP TABLE IF EXISTS BOOK_GENRE;

DROP TABLE IF EXISTS BOOK_AUTHOR;

DROP TABLE IF EXISTS BOOK_REVIEW;

DROP TABLE IF EXISTS BOOK;

DROP TABLE IF EXISTS GENRE;

DROP TABLE IF EXISTS AUTHOR;

CREATE TABLE AUTHOR (
    AUTHOR_ID BIGINT NOT NULL AUTO_INCREMENT,
    LASTNAME VARCHAR(255) NOT NULL,
    FIRSTNAME VARCHAR(255) NOT NULL,
    CONSTRAINT PK__AUTHOR PRIMARY KEY (AUTHOR_ID)
);

CREATE TABLE GENRE (
    GENRE_ID BIGINT NOT NULL AUTO_INCREMENT,
    TITLE VARCHAR(255) NOT NULL,
    CONSTRAINT PK__GENRE PRIMARY KEY (GENRE_ID)
);

CREATE TABLE BOOK (
    BOOK_ID BIGINT NOT NULL AUTO_INCREMENT,
    TITLE VARCHAR(255) NOT NULL,
    CONSTRAINT PK__BOOK PRIMARY KEY (BOOK_ID)
);

CREATE TABLE BOOK_AUTHOR (
    BOOK_ID BIGINT NOT NULL,
    AUTHOR_ID BIGINT NOT NULL,
    CONSTRAINT PK__BOOK_AUTHOR PRIMARY KEY (BOOK_ID, AUTHOR_ID),
    CONSTRAINT FK__BOOK_AUTHOR__BOOK FOREIGN KEY (BOOK_ID) REFERENCES BOOK (BOOK_ID) ON DELETE CASCADE,
    CONSTRAINT FK__BOOK_AUTHOR__AUTHOR FOREIGN KEY (AUTHOR_ID) REFERENCES AUTHOR (AUTHOR_ID)
);

CREATE TABLE BOOK_GENRE (
    BOOK_ID BIGINT NOT NULL,
    GENRE_ID BIGINT NOT NULL,
    CONSTRAINT PK__BOOK_GENRE PRIMARY KEY (BOOK_ID, GENRE_ID),
    CONSTRAINT FK__BOOK_GENRE__BOOK FOREIGN KEY (BOOK_ID) REFERENCES BOOK (BOOK_ID) ON DELETE CASCADE,
    CONSTRAINT FK__BOOK_GENRE__GENRE FOREIGN KEY (GENRE_ID) REFERENCES GENRE (GENRE_ID)
);

CREATE TABLE BOOK_REVIEW (
    BOOK_REVIEW_ID BIGINT NOT NULL AUTO_INCREMENT,
    BOOK_ID BIGINT NOT NULL,
    REVIEWER_NAME VARCHAR(255) NOT NULL,
    RATING INT NOT NULL,
    COMMENT VARCHAR(4000) NOT NULL,
    CONSTRAINT PK__BOOK_REVIEW PRIMARY KEY (BOOK_REVIEW_ID),
    CONSTRAINT FK__BOOK_REVIEW__BOOK FOREIGN KEY (BOOK_ID) REFERENCES BOOK (BOOK_ID) ON DELETE CASCADE
);