package application.dto;

import java.util.Date;

public record ReviewDTO(
        Date date,
        String content,
        Float score
) {}
