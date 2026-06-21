package application.port.out;

import domain.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookCachePort {
    void put(String key, Book book);
    Optional<Book> get(String key);
    List<Book> getAll();
    void remove(String key);
    void removeAll();
    boolean containsKey(String key);

    // Ickle query methods
    List<Book> searchByTitle(String title);
    List<Book> searchByAuthorFirstname(String firstname);
    List<Book> searchByDescription(String keyword);
    List<Book> searchByPriceRange(float min, float max);
    List<Book> getAllSortedByAuthorFirstname();
    long countByTitle(String title);
}
