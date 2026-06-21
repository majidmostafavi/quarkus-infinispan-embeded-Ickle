package infrastructure.adapter.out;

import application.port.out.BookCachePort;
import application.port.out.BookRepositoryPort;
import domain.model.Book;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.enterprise.inject.Alternative;
import jakarta.annotation.Priority;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Alternative
@Priority(10)
public class TransparentBookCacheProxy implements BookCachePort {

    @Inject
    InfinispanBookCacheAdapter cache;   // the infinispan cache

    @Inject
    BookRepositoryPort repository;      // the database

    // ── READ-THROUGH ──────────────────────────────────────────────────────────

    @Override
    public Optional<Book> get(String key) {
        Optional<Book> cached = cache.get(key);
        if (cached.isPresent()) {
            Log.debug("Cache HIT for key: " + key);
            return cached;
        }

        Log.debug("Cache MISS for key: " + key + " — fetching from DB");
        Optional<Book> fromDb = repository.findByKey(key);
        fromDb.ifPresent(book -> {
            cache.put(key, book);   // populate cache on miss
            Log.debug("Loaded key into cache from DB: " + key);
        });
        return fromDb;
    }

    @Override
    public List<Book> getAll() {
        List<Book> cached = cache.getAll();
        if (!cached.isEmpty()) {
            Log.debug("Cache HIT for getAll()");
            return cached;
        }

        Log.debug("Cache MISS for getAll() — fetching from DB");
        List<Book> fromDb = repository.findAll();
        fromDb.forEach(book -> cache.put(book.getTitle(), book)); // warm up cache
        return fromDb;
    }

    @Override
    public boolean containsKey(String key) {
        if (cache.containsKey(key)) return true;
        // optionally check DB too
        return repository.existsByKey(key);
    }

    // ── WRITE-THROUGH ─────────────────────────────────────────────────────────

    @Override
    public void put(String key, Book book) {
        repository.save(key, book);  // write to DB first
        cache.put(key, book);        // then update cache
    }

    @Override
    public void remove(String key) {
        repository.delete(key);      // remove from DB
        cache.remove(key);           // invalidate cache
    }

    @Override
    public void removeAll() {
        cache.removeAll();
        // add repository.deleteAll() if needed
    }

    // ── SEARCH (delegate to cache, cache must be warmed) ──────────────────────

    @Override
    public List<Book> searchByTitle(String title) {
        return cache.searchByTitle(title);
    }

    @Override
    public List<Book> searchByAuthorFirstname(String firstname) {
        return cache.searchByAuthorFirstname(firstname);
    }

    @Override
    public List<Book> searchByDescription(String keyword) {
        return cache.searchByDescription(keyword);
    }

    @Override
    public List<Book> searchByPriceRange(float min, float max) {
        return cache.searchByPriceRange(min, max);
    }

    @Override
    public List<Book> getAllSortedByAuthorFirstname() {
        return cache.getAllSortedByAuthorFirstname();
    }

    @Override
    public long countByTitle(String title) {
        return cache.countByTitle(title);
    }
}