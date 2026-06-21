package application.port.in;

import application.dto.BookDTO;
import application.dto.BookSearchResultDTO;

import java.util.List;

public interface SearchBookUseCase {
    BookSearchResultDTO searchByTitle(String title);
    BookSearchResultDTO searchByAuthor(String firstname, String surname);
    BookSearchResultDTO searchByDescription(String keyword);
    BookSearchResultDTO searchByPriceRange(float min, float max);
    List<BookDTO> getAllSortedByAuthor();
}