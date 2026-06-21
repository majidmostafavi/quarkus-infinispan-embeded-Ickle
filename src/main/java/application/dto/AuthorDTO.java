package application.dto;

public record AuthorDTO(
        String firstname,
        String surname,
        Integer numberOfPublishedBooks
) {}
