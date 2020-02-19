package com.three.d.tic.tac.toe.controller;

import static com.three.d.tic.tac.toe.controller.LoginController.SELF_LINK;

import javax.validation.Valid;

import com.three.d.tic.tac.toe.common.Constants.UriParts;
import com.three.d.tic.tac.toe.dto.AuthResponse;
import com.three.d.tic.tac.toe.dto.LoginRequest;
import com.three.d.tic.tac.toe.security.JwtTokenUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = SELF_LINK)
public class LoginController {

    public static final String SELF_LINK = UriParts.API_PREFIX + "/login";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public AuthResponse createAuthenticationToken(@Valid @RequestBody
            LoginRequest loginRequest) {
        authenticate(loginRequest.getUsername(), loginRequest.getPassword());

        final UserDetails userDetails = userService
                .loadUserByUsername(loginRequest.getUsername());

        return AuthResponse.builder()
                .tokenType("Bearer")
                .token(jwtTokenUtil.generateToken(userDetails))
                .build();
    }

    private void authenticate(String username, String password) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
        try {
            authenticationManager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
