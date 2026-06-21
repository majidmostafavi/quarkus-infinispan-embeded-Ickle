package application.port.in;

import application.dto.BookDTO;

public interface CreateBookUseCase {
    BookDTO createBook(String key, BookDTO bookDTO);
}
