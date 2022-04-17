package ru.otus.spring.mongock;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;

@ChangeLog
public class DatabaseChangeLog {
    @ChangeSet(order = "001", id = "dropDb", author = "anonymous", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "anonymous", runAlways = true)
    public void insertAuthors(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection("authors");
        myCollection.insertMany(List.of(
                new Document()
                        .append("_id", new Document("authorId", "A001"))
                        .append("lastname", "Пушкин")
                        .append("firstname", "Александр Сергеевич"),
                new Document()
                        .append("_id", new Document("authorId", "A002"))
                        .append("lastname", "Толстой")
                        .append("firstname", "Лев Николаевич"),
                new Document()
                        .append("_id", new Document("authorId", "A003"))
                        .append("lastname", "Лермонтов")
                        .append("firstname", "Михаил Юрьевич"),
                new Document()
                        .append("_id", new Document("authorId", "A004"))
                        .append("lastname", "Ильф")
                        .append("firstname", "Илья Арнольдович"),
                new Document()
                        .append("_id", new Document("authorId", "A005"))
                        .append("lastname", "Петров")
                        .append("firstname", "Евгений")));
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "anonymous", runAlways = true)
    public void insertGenres(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection("genres");
        myCollection.insertMany(List.of(
                new Document()
                        .append("_id", new Document("genreId", "G001"))
                        .append("title", "Роман"),
                new Document()
                        .append("_id", new Document("genreId", "G002"))
                        .append("title", "Поэма"),
                new Document()
                        .append("_id", new Document("genreId", "G003"))
                        .append("title", "Эпопея")));
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "anonymous", runAlways = true)
    public void insertBooks(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection("books");
        myCollection.insertMany(List.of(
                new Document()
                        .append("_id", new Document("bookId", "B001"))
                        .append("title", "Евгений Онегин")
                        .append("authors", List.of(
                                new Document()
                                        .append("_id", new Document("authorId", "A001"))
                                        .append("lastname", "Пушкин")
                                        .append("firstname", "Александр Сергеевич")))
                        .append("genres", List.of(
                                new Document()
                                        .append("_id", new Document("genreId", "G001"))
                                        .append("title", "Роман"))),
                new Document()
                        .append("_id", new Document("bookId", "B002"))
                        .append("title", "Война и мир")
                        .append("authors", List.of(
                                new Document()
                                        .append("_id", new Document("authorId", "A002"))
                                        .append("lastname", "Толстой")
                                        .append("firstname", "Лев Николаевич")))
                        .append("genres", List.of(
                                new Document()
                                        .append("_id", new Document("genreId", "G001"))
                                        .append("title", "Роман"),
                                new Document()
                                        .append("_id", new Document("genreId", "G003"))
                                        .append("title", "Эпопея"))),
                new Document()
                        .append("_id", new Document("bookId", "B003"))
                        .append("title", "Герой нашего времени")
                        .append("authors", List.of(
                                new Document()
                                        .append("_id", new Document("authorId", "A003"))
                                        .append("lastname", "Лермонтов")
                                        .append("firstname", "Михаил Юрьевич")))
                        .append("genres", List.of(
                                new Document()
                                        .append("_id", new Document("genreId", "G002"))
                                        .append("title", "Поэма"))),
                new Document()
                        .append("_id", new Document("bookId", "B004"))
                        .append("title", "Двенадцать стульев")
                        .append("authors", List.of(
                                new Document()
                                        .append("_id", new Document("authorId", "A004"))
                                        .append("lastname", "Ильф")
                                        .append("firstname", "Илья Арнольдович"),
                                new Document()
                                        .append("_id", new Document("authorId", "A005"))
                                        .append("lastname", "Петров")
                                        .append("firstname", "Евгений")))
                        .append("genres", List.of(
                                new Document()
                                        .append("_id", new Document("genreId", "G001"))
                                        .append("title", "Роман")))));
    }

    @ChangeSet(order = "005", id = "insertBookReviews", author = "anonymous", runAlways = true)
    public void insertBookReviews(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection("bookReviews");
        myCollection.insertMany(List.of(
                new Document()
                        .append("_id", new Document("bookReviewId", "BR0001"))
                        .append("book", new Document()
                                .append("_id", new Document("bookId", "B001")))
                        .append("reviewerName", "Дантес")
                        .append("rating", 2)
                        .append("comment", "Так себе"),
                new Document()
                        .append("_id", new Document("bookReviewId", "BR0002"))
                        .append("book", new Document()
                                .append("_id", new Document("bookId", "B002")))
                        .append("reviewerName", "Вася Пупкин")
                        .append("rating", 5)
                        .append("comment", "Не осилил")));
    }
}