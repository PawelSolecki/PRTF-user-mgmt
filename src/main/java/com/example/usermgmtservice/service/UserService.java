package com.example.usermgmtservice.service;

import com.example.usermgmtservice.model.UserDTO;
import com.example.usermgmtservice.model.auth.RegisterRequest;
import com.example.usermgmtservice.model.auth.UserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserService {
    Mono<UserResponse> register(RegisterRequest request);
    Flux<UserDTO> listUsers();

    Mono<UserDTO> getUserById(UUID userId);

    Mono<UserDTO> saveNewUser(Mono<UserDTO> userDTO);

    Mono<UserDTO> updateUser(UUID userId, UserDTO userDTO);

    Mono<Void> deleteUserById(UUID userId);
}
