package com.example.usermgmtservice.service;

import com.example.usermgmtservice.mapper.UserMapper;
import com.example.usermgmtservice.model.UserDTO;
import com.example.usermgmtservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public Flux<UserDTO> listUsers() {
        return userRepository.findAll().map(userMapper::toDto);
    }

    @Override
    public Mono<UserDTO> getUserById(UUID userId) {
        return userRepository.findById(userId).map(userMapper::toDto);
    }


    @Override
    public Mono<UserDTO> updateUser(UUID userId, UserDTO userDTO) {
        return userRepository.findById(userId)
            .map(foundUser -> {
                //update properties
                foundUser.setName(userDTO.getName());
                foundUser.setEmail(userDTO.getEmail());

                return foundUser;
                })
            .flatMap(userRepository::save)
            .map(userMapper::toDto);
    }

    @Override
    public Mono<Void> deleteUserById(UUID userId) {
        return userRepository.deleteById(userId);
    }
}
