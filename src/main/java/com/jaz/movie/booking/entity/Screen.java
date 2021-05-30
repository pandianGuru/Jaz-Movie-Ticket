package com.jaz.movie.booking.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "screen")
public class Screen {

    @Id
    @GeneratedValue
    private Long id;
    private Long movieId;
    private int maxSeat;
    private int screenId;
    private String screenName;
    private BigDecimal amount;
    private String seatType;
    @OneToMany(targetEntity = MovieAvailableShowsData.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "mas_fk", referencedColumnName = "id")
    private List<MovieAvailableShowsData> availableShows;
}