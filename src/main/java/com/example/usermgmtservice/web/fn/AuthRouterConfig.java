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
public class AuthRouterConfig {

    public static final String AUTH_PATH = "/api/v1/auth";

    private final AuthHandler handler;

    @Bean
    public RouterFunction<ServerResponse> customAuthRoutes(){
        return route()
            .POST(AUTH_PATH+"/register", accept(APPLICATION_JSON), handler::register)
            .POST(AUTH_PATH+"/login", accept(APPLICATION_JSON), handler::login)
            .POST(AUTH_PATH+"/logout", accept(APPLICATION_JSON), handler::logout)
            .POST(AUTH_PATH+"/refresh", accept(APPLICATION_JSON), handler::refreshToken)
            .build();
    }
}
