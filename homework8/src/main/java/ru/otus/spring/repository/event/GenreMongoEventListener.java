package ru.otus.spring.repository.event;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;
import ru.otus.spring.domain.Genre;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

@RequiredArgsConstructor
@Component
public class GenreMongoEventListener extends AbstractMongoEventListener<Genre> {
    @Autowired
    private final MongoOperations mongoOperations;

    @Override
    public void onAfterSave(AfterSaveEvent<Genre> event) {
        mongoOperations.updateMulti(
                query(where("genres._id").is(event.getSource().getGenreId())),
                update("genres.$", event.getSource()),
                Book.class);
    }
}