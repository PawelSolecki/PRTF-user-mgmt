package com.example.usermgmtservice.repository;

import com.example.usermgmtservice.domain.User;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, UUID> {
    Mono<User> findByEmail(String email);

    Mono<User> findByKeycloakId(UUID id);
}
