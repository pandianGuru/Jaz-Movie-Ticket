package com.jaz.movie.booking.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "screen")
public class Screen {

    @Id
    @GeneratedValue
    private Long id;
    private String screenName;
    private int maxSeat;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name ="FK_show_id")
    private MovieAvailableShows availableShow;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name ="FK_movie_id")
    private Movie movie;
}