package infrastructure.adapter.out;

import application.port.out.BookRepositoryPort;
import domain.model.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class JPABookRepositoryAdapter implements BookRepositoryPort {

    @Inject
    EntityManager em;

    @Override
    public Optional<Book> findByKey(String key) {
        // key maps to title or a dedicated field — adjust query as needed
        return em.createQuery("SELECT b FROM Book b WHERE b.title = :key", Book.class)
                .setParameter("key", key)
                .getResultStream()
                .findFirst();
    }

    @Override
    public List<Book> findAll() {
        return em.createQuery("SELECT b FROM Book b", Book.class).getResultList();
    }

    @Override
    @Transactional
    public Book save(String key, Book book) {
        return em.merge(book);
    }

    @Override
    @Transactional
    public void delete(String key) {
        findByKey(key).ifPresent(em::remove);
    }

    @Override
    public boolean existsByKey(String key) {
        return findByKey(key).isPresent();
    }
}