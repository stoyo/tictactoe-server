package com.three.d.tic.tac.toe.controller;

import static com.three.d.tic.tac.toe.controller.UserController.SELF_LINK;

import javax.validation.Valid;

import com.three.d.tic.tac.toe.common.Constants.UriParts;
import com.three.d.tic.tac.toe.dto.User;
import com.three.d.tic.tac.toe.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = SELF_LINK)
public class UserController {

    public static final String SELF_LINK = UriParts.API_PREFIX + "/users";

    private static final String USERNAME_ALREADY_TAKEN_MESSAGE = "username: '%s' is already taken";

    @Autowired
    private UserService userService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        if (userService.existsByUsername(user.getUsername())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format(USERNAME_ALREADY_TAKEN_MESSAGE, user.getUsername())
            );
        }

        return userService.createUser(user);
    }
}
