package infrastructure.config;

import application.port.out.BookCachePort;
import application.port.out.BookRepositoryPort;
import domain.model.Book;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import shared.util.ModelGenerator;


import java.util.Map;

@ApplicationScoped
public class BookCacheInitializer {

    @Inject
    BookRepositoryPort bookRepositoryPort;

    void onStart(@Observes StartupEvent event) {
        Log.info("Initializing  && Insert data to DB");
        Map<String, Book> books = ModelGenerator.generateBooks();
        books.forEach((key, book) -> {
            bookRepositoryPort.save(key, book);
            Log.info("Loaded book into: " + key);
        });
        Log.info("Book  initialized with " + books.size() + " entries.");
    }
}
