package com.jaz.movie.booking.service;

import com.jaz.movie.booking.entity.User;
import com.jaz.movie.booking.exception.UserNotFoundException;
import com.jaz.movie.booking.repository.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserInfoRepository userInfoRepository;

    @Override
    public User createUser(User user) {
        try {
            return userInfoRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            log.info("Method: createUser: Email Duplicate " + user.getName());
            log.error("Exception for Creating User: ", ex.toString());
            throw new UserNotFoundException("This Email is " + user.getName() + " already exist. Please try some other email.");
        }
    }
}
