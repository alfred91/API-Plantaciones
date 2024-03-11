package com.example.apijwt.service;

import com.example.apijwt.dto.UserRegisterDTO;
import com.example.apijwt.entity.UserAuthority;
import com.example.apijwt.entity.UserEntity;
import com.example.apijwt.repository.UserEntityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


    @Service
    public class UserEntityService {

        private final UserEntityRepository repository;
        private final PasswordEncoder passwordEncoder;

        public UserEntityService(UserEntityRepository repository, PasswordEncoder passwordEncoder) {
            this.repository = repository;
            this.passwordEncoder = passwordEncoder;
        }

        public Optional<UserEntity> findByUsername(String username) {
            return this.repository.findByUsername(username);
        }

        public UserEntity save(UserRegisterDTO userDTO) {
            UserEntity user = new UserEntity(
                    null,
                    userDTO.username(),
                    passwordEncoder.encode(userDTO.password()),
                    userDTO.email(),
                    List.of(UserAuthority.READ, UserAuthority.WRITE) // Asigna el rol WRITE
            );
            return this.repository.save(user);
        }


    }

