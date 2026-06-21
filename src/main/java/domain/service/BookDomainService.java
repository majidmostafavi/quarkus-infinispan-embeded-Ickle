package domain.service;

import domain.exception.DomainException;
import domain.model.Book;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BookDomainService {
    /**
     * Validates that a Book entity is consistent with business rules.
     */
    public void validate(Book book) {
        if (book == null) {
            throw new DomainException("Book must not be null");
        }
        if (book.getTitle() == null || book.getTitle().isBlank()) {
            throw new DomainException("Book title must not be empty");
        }
        if (book.getPrice() == null || book.getPrice() < 0) {
            throw new DomainException("Book price must be zero or positive");
        }
        if (book.getAuthor() == null) {
            throw new DomainException("Book must have an author");
        }
        if (book.getYearOfPublication() == null || book.getYearOfPublication() < 1000) {
            throw new DomainException("Book year of publication is invalid");
        }
    }
}
