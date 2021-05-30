package com.jaz.movie.booking.repository;

import com.jaz.movie.booking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<User, Long> {

    User findByLoginId(String username);
}
