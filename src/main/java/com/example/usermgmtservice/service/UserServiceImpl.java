package com.example.usermgmtservice.service;

import com.example.usermgmtservice.model.UserDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class UserServiceImpl implements UserService {
    @Override
    public Flux<UserDTO> listUsers() {
        return null;
    }

    @Override
    public Mono<UserDTO> getUserById(UUID userId) {
        return null;
    }

    @Override
    public Mono<UserDTO> saveNewUser(Mono<UserDTO> userDTO) {
        return null;
    }

    @Override
    public Mono<UserDTO> updateUser(UUID userId, UserDTO userDTO) {
        return null;
    }

    @Override
    public Mono<Void> deleteUserById(UUID userId) {
        return null;
    }
}
