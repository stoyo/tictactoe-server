package com.three.d.tic.tac.toe.service;

import java.util.Optional;

import com.three.d.tic.tac.toe.domain.UserEntity;
import com.three.d.tic.tac.toe.dto.User;
import com.three.d.tic.tac.toe.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> oUserEntity = userRepository.findByUsername(username);
        if (!oUserEntity.isPresent()) {
            throw new UsernameNotFoundException("User not found with username: '" + username + "'");
        }

        UserEntity userEntity = oUserEntity.get();

        return User.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .build();
    }

    @Override
    public Optional<UserEntity> findByUsername(String email) {
        return userRepository.findByUsername(email);
    }

    @Override
    public Boolean existsByUsername(String email) {
        return userRepository.existsByUsername(email);
    }

    @Override
    public User createUser(User user) {
        UserEntity userEntity = UserEntity.fromDto(user);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));

        return Optional.of(userRepository.save(userEntity))
                .map(UserEntity::toDto)
                .get();
    }
}
