package com.kkm.novelcanvasapi.repository;

import com.kkm.novelcanvasapi.domain.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, String> {
    Mono<User> findByUsername(String username);
    Mono<Void> deleteByUsername(String username);
}
