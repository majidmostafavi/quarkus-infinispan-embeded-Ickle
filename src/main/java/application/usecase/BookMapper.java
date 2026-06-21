package application.usecase;

import application.dto.AuthorDTO;
import application.dto.BookDTO;
import application.dto.ReviewDTO;
import domain.model.Author;
import domain.model.Book;
import domain.model.Review;

import java.util.Collections;
import java.util.List;

public class BookMapper {

    public static Book toDomain(String key, BookDTO dto) {
        Author author = new Author(
                dto.author().firstname(),
                dto.author().surname(),
                dto.author().numberOfPublishedBooks()
        );
        List<Review> reviews = dto.reviews() == null ? Collections.emptyList() :
                dto.reviews().stream()
                .map(r -> new Review(r.date(), r.content(), r.score()))
                .toList();
        return new Book(dto.title(), dto.yearOfPublication(), dto.description(),
                dto.price(), author, reviews);
    }

    public static BookDTO toDTO(String key, Book book) {
        AuthorDTO authorDTO = new AuthorDTO(
                book.getAuthor().getFirstname(),
                book.getAuthor().getSurname(),
                book.getAuthor().getNumberOfPublishedBooks()
        );
        List<ReviewDTO> reviewDTOs = book.getReviews() == null ? Collections.emptyList() :
                book.getReviews().stream()
                .map(r -> new ReviewDTO(r.getDate(), r.getContent(), r.getScore()))
                .toList();
        return new BookDTO(key, book.getTitle(), book.getYearOfPublication(),
                book.getDescription(), book.getPrice(), authorDTO, reviewDTOs);
    }
}
