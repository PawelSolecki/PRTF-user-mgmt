package com.example.usermgmtservice.service;

import com.example.usermgmtservice.model.UserDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserService {
    Flux<UserDTO> listUsers();

    Mono<UserDTO> getUserById(UUID userId);
    Mono<UserDTO> getUserByKeycloakId(UUID id);

    Mono<UserDTO> updateUser(UUID userId, UserDTO userDTO);

    Mono<Void> deleteUserById(UUID userId);

}
