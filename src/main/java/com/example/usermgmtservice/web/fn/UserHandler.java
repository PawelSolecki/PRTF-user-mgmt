package com.example.usermgmtservice.web.fn;

import com.example.usermgmtservice.model.UserDTO;
import com.example.usermgmtservice.model.auth.RegisterRequest;
import com.example.usermgmtservice.model.exception.AuthException;
import com.example.usermgmtservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserHandler {
    private final UserService userService;
    private final Validator validator;

    private void validate(UserDTO userDTO) {
        Errors errors = new BeanPropertyBindingResult(userDTO, "userDTO");
        validator.validate(userDTO, errors);

        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }

    public Mono<ServerResponse> register(ServerRequest request) {
        log.info("register called");
        return request.bodyToMono(RegisterRequest.class)
            .flatMap(userService::register)
            .flatMap(userResponse -> ServerResponse.status(HttpStatus.CREATED).bodyValue(userResponse))
            .onErrorResume(AuthException.class, e -> ServerResponse.status(e.getStatus()).build());
    }

    public Mono<ServerResponse> listUsers(ServerRequest request) {
        return ServerResponse.ok().body(userService.listUsers(), UserDTO.class);
    }

    public Mono<ServerResponse> getUserById(ServerRequest request) {
        return ServerResponse.ok().body(
            userService.getUserById(UUID.fromString(request.pathVariable("userId")))
                .switchIfEmpty(Mono.error(new ServerWebInputException("User not found"))),
            UserDTO.class);
    }

    public Mono<ServerResponse> createNewUser(ServerRequest request) {
        return userService.saveNewUser(request.bodyToMono(UserDTO.class).doOnNext(this::validate))
            .flatMap(userDTO -> ServerResponse
                .created(UriComponentsBuilder
                    .fromPath(UserRouterConfig.USER_PATH_ID)
                    .build(userDTO.getId()))
                .build());

    }

    public Mono<ServerResponse> updateUserById(ServerRequest request) {
        return request.bodyToMono(UserDTO.class)
            .doOnNext(this::validate)
            .flatMap(userDTO -> userService
                .updateUser(UUID.fromString(request.pathVariable("userId")), userDTO))
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
            .flatMap(savedDto -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> deleteBeerById(ServerRequest request) {
        return userService.getUserById(UUID.fromString(request.pathVariable("userId")))
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
            .flatMap(userDTO -> userService.deleteUserById(userDTO.getId()))
            .then(ServerResponse.noContent().build());
    }
}
