package application.dto;

import java.util.List;

public record BookDTO(
        String key,
        String title,
        Integer yearOfPublication,
        String description,
        Float price,
        AuthorDTO author,
        List<ReviewDTO> reviews
) {}
