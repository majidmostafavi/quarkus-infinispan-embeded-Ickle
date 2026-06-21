package application.dto;

import java.util.List;

public record BookSearchResultDTO(
        int totalCount,
        List<BookDTO> books
) {}
