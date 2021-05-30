package com.jaz.movie.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MovieDto {

    private Long movieId;
    @NotEmpty(message = "Movie name is missing.")
    private String movieName;
    @NotEmpty(message = "Movie type field is missing.")
    private String type;
    @NotEmpty(message = "Movie description is missing.")
    private String description;
    @NotEmpty(message = "Movie language field is missing.")
    private String language;
    @NotEmpty(message = "Movie certificate field is missing.")
    private String certificate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private java.sql.Date releaseDate;
}
