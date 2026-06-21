package infrastructure.adapter.out;

import application.port.out.BookCachePort;
import domain.model.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.infinispan.Cache;
import org.infinispan.commons.api.query.Query;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.query.Search;
import org.infinispan.query.dsl.QueryFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class InfinispanBookCacheAdapter implements BookCachePort {

    private static final String CACHE_NAME = "book";

    @Inject
    EmbeddedCacheManager cacheManager;

    private Cache<String, Book> cache() {
        return cacheManager.getCache(CACHE_NAME);
    }

    @Override
    public void put(String key, Book book) {
        cache().put(key, book);
    }

    @Override
    public Optional<Book> get(String key) {
        return Optional.ofNullable(cache().get(key));
    }

    @Override
    public List<Book> getAll() {
        return new ArrayList<>(cache().values());
    }

    @Override
    public void remove(String key) {
        cache().remove(key);
    }

    @Override
    public void removeAll() {
        cache().clear();
    }

    @Override
    public boolean containsKey(String key) {
        return cache().containsKey(key);
    }

    @Override
    public List<Book> searchByTitle(String title) {

        Query<Book> query =cache().query( "FROM insights.book b WHERE b.title = :title");
        return query
                .setParameter("title", title)
                .execute()
                .list();
    }

    @Override
    public List<Book> searchByAuthorFirstname(String firstname) {

        Query<Book> query =cache().query( "FROM insights.Book b WHERE b.author.firstname = :firstname");
        return query
                .setParameter("firstname", firstname)
                .execute()
                .list();
    }

    @Override
    public List<Book> searchByDescription(String keyword) {

        Query<Book> query =cache().query( "FROM domain.model.Book b WHERE b.description : :keyword");
        return query
                .setParameter("keyword", keyword)
                .execute()
                .list();
    }

    @Override
    public List<Book> searchByPriceRange(float min, float max) {

        Query<Book> query =cache().query( "FROM domain.model.Book b WHERE b.price >= :min AND b.price <= :max");
        return query
                .setParameter("min", min)
                .setParameter("max", max)
                .execute()
                .list();
    }

    @Override
    public List<Book> getAllSortedByAuthorFirstname() {

        Query<Book> query =cache().query("FROM domain.model.Book b ORDER BY b.author.firstname ASC");
        return query.execute().list();
    }

    @Override
    public long countByTitle(String title) {

        Query<Book> query =cache().query( "SELECT COUNT(b) FROM domain.model.Book b WHERE b.title = :title");
        List<?> result = query
                .setParameter("title", title)
                .execute()
                .list();
        return result.isEmpty() ? 0L : ((Number) result.get(0)).longValue();
    }
}
