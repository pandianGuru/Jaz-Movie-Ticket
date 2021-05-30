package com.jaz.movie.booking.service;

import com.jaz.movie.booking.entity.User;
import com.jaz.movie.booking.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserInfoRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByLoginId(username);
        return new org.springframework.security.core.userdetails.User(user.getLoginId(), user.getPassword(), new ArrayList<>());
    }
}
