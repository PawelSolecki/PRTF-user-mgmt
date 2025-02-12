package com.example.usermgmtservice.web.fn;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class UserRouterConfig {

    public static final String USER_PATH = "/api/v1/user";
    public static final String USER_PATH_ID = USER_PATH + "/{userId}";

    private final UserHandler handler;


    @Bean
    public RouterFunction<ServerResponse> customRoutes() {
        return route()
            .GET(USER_PATH, accept(APPLICATION_JSON), handler::listUsers)
            .GET(USER_PATH_ID, accept(APPLICATION_JSON), handler::getUserById)
            .POST(USER_PATH, accept(APPLICATION_JSON), handler::createNewUser)
            .PUT(USER_PATH_ID, accept(APPLICATION_JSON), handler::updateUserById)
            .build();
    }

}
