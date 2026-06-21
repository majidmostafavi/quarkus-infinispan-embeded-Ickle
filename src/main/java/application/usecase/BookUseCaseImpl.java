package application.usecase;

import application.dto.BookDTO;
import application.dto.BookSearchResultDTO;
import application.port.in.*;
import application.port.out.BookCachePort;
import domain.exception.DomainException;
import domain.model.Book;
import domain.service.BookDomainService;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class BookUseCaseImpl implements
        CreateBookUseCase,
        GetBookUseCase,
        UpdateBookUseCase,
        DeleteBookUseCase,
        SearchBookUseCase {

    @Inject
    BookCachePort bookCachePort;

    @Inject
    BookDomainService bookDomainService;

    // ── CreateBookUseCase ─────────────────────────────────────────────────────

    @Override
    public BookDTO createBook(String key, BookDTO bookDTO) {
        if (bookCachePort.containsKey(key)) {
            throw new DomainException("Book with key '" + key + "' already exists");
        }
        Book book = BookMapper.toDomain(key, bookDTO);
        bookDomainService.validate(book);
        bookCachePort.put(key, book);
        Log.info("Book created: " + key);
        return BookMapper.toDTO(key, book);
    }

    // ── GetBookUseCase ────────────────────────────────────────────────────────

    @Override
    public Optional<BookDTO> getBook(String key) {
        return bookCachePort.get(key).map(book -> BookMapper.toDTO(key, book));
    }

    @Override
    public List<BookDTO> getAllBooks() {
        return bookCachePort.getAll().stream()
                .map(book -> BookMapper.toDTO(book.getTitle(), book))
                .toList();
    }

    // ── UpdateBookUseCase ─────────────────────────────────────────────────────

    @Override
    public BookDTO updateBook(String key, BookDTO bookDTO) {
        if (!bookCachePort.containsKey(key)) {
            throw new DomainException("Book with key '" + key + "' not found");
        }
        Book book = BookMapper.toDomain(key, bookDTO);
        bookDomainService.validate(book);
        bookCachePort.put(key, book);
        Log.info("Book updated: " + key);
        return BookMapper.toDTO(key, book);
    }

    // ── DeleteBookUseCase ─────────────────────────────────────────────────────

    @Override
    public void deleteBook(String key) {
        if (!bookCachePort.containsKey(key)) {
            throw new DomainException("Book with key '" + key + "' not found");
        }
        bookCachePort.remove(key);
        Log.info("Book deleted: " + key);
    }

    // ── SearchBookUseCase ─────────────────────────────────────────────────────

    @Override
    public BookSearchResultDTO searchByTitle(String title) {
        List<Book> books = bookCachePort.searchByTitle(title);
        List<BookDTO> dtos = books.stream().map(b -> BookMapper.toDTO(b.getTitle(), b)).toList();
        return new BookSearchResultDTO(dtos.size(), dtos);
    }

    @Override
    public BookSearchResultDTO searchByAuthor(String firstname, String surname) {
        List<Book> books = bookCachePort.searchByAuthorFirstname(firstname);
        List<BookDTO> dtos = books.stream()
                .filter(b -> surname == null || b.getAuthor().getSurname().equalsIgnoreCase(surname))
                .map(b -> BookMapper.toDTO(b.getTitle(), b))
                .toList();
        return new BookSearchResultDTO(dtos.size(), dtos);
    }

    @Override
    public BookSearchResultDTO searchByDescription(String keyword) {
        List<Book> books = bookCachePort.searchByDescription(keyword);
        List<BookDTO> dtos = books.stream().map(b -> BookMapper.toDTO(b.getTitle(), b)).toList();
        return new BookSearchResultDTO(dtos.size(), dtos);
    }

    @Override
    public BookSearchResultDTO searchByPriceRange(float min, float max) {
        List<Book> books = bookCachePort.searchByPriceRange(min, max);
        List<BookDTO> dtos = books.stream().map(b -> BookMapper.toDTO(b.getTitle(), b)).toList();
        return new BookSearchResultDTO(dtos.size(), dtos);
    }

    @Override
    public List<BookDTO> getAllSortedByAuthor() {
        return bookCachePort.getAllSortedByAuthorFirstname()
                .stream()
                .map(b -> BookMapper.toDTO(b.getTitle(), b))
                .toList();
    }
}
