package com.jaz.movie.booking.dto;

import com.jaz.movie.booking.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HomePageResponseDto {

    private List<ScreenDto> screens;
}
