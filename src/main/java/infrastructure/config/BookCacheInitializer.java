package infrastructure.config;

import application.port.out.BookCachePort;
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
    BookCachePort bookCachePort;

    void onStart(@Observes StartupEvent event) {
        Log.info("Initializing book cache with sample data...");
        Map<String, Book> books = ModelGenerator.generateBooks();
        books.forEach((key, book) -> {
            bookCachePort.put(key, book);
            Log.info("Loaded book into cache: " + key);
        });
        Log.info("Book cache initialized with " + books.size() + " entries.");
    }
}
