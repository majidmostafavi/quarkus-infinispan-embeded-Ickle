package application.port.in;

import application.dto.BookDTO;

public interface UpdateBookUseCase {
    BookDTO updateBook(String key, BookDTO bookDTO);
}
