package com.jaz.movie.booking.controller;

import com.jaz.movie.booking.dto.AuthRequest;
import com.jaz.movie.booking.entity.User;
import com.jaz.movie.booking.service.UserService;
import com.jaz.movie.booking.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/sign-up")
    public User welcome(@RequestBody User user) {
        log.info("Sign up: " + user.toString());
        return userService.createUser(user);
    }

    @PostMapping("/sign-in")
    public Map signIn(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
        } catch (Exception ex) {
            throw new Exception("inavalid username/password");
        }
        StringBuffer jwtToken = new StringBuffer();
        jwtToken.append("Bearer ").append(jwtUtil.generateToken(authRequest.getUserName()));
        Map map = new HashMap<>();
        map.put("token", jwtToken.toString());
        return map;
    }
}
