package ru.otus.spring.repository.event;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;
import ru.otus.spring.domain.Author;
import ru.otus.spring.domain.Book;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

@RequiredArgsConstructor
@Component
public class AuthorMongoEventListener extends AbstractMongoEventListener<Author> {
    @Autowired
    private final MongoOperations mongoOperations;

    @Override
    public void onAfterSave(AfterSaveEvent<Author> event) {
        mongoOperations.updateMulti(
                query(where("authors._id").is(event.getSource().getAuthorId())),
                update("authors.$", event.getSource()),
                Book.class);
    }
}