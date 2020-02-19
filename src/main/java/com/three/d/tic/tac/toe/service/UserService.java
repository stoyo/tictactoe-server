package com.three.d.tic.tac.toe.service;

import java.util.Optional;

import com.three.d.tic.tac.toe.domain.UserEntity;
import com.three.d.tic.tac.toe.dto.User;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    Optional<UserEntity> findByUsername(String username);

    Boolean existsByUsername(String username);

    User createUser(User user);
}
