package com.jaz.movie.booking.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movie_available_shows")
public class MovieAvailableShows {
    @Id
    @GeneratedValue
    private Long id;
    private String showTimings;

}
