package application.port.in;

import application.dto.BookDTO;

import java.util.List;
import java.util.Optional;

public interface GetBookUseCase {
    Optional<BookDTO> getBook(String key);
    List<BookDTO> getAllBooks();
}
