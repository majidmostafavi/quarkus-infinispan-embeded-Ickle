package application.port.out;

import domain.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepositoryPort {
    Optional<Book> findByKey(String key);
    List<Book> findAll();
    Book save(String key, Book book);
    void delete(String key);
    boolean existsByKey(String key);
}
