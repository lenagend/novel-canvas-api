package com.kkm.novelcanvasapi.repository;

import com.kkm.novelcanvasapi.domain.Like;
import com.kkm.novelcanvasapi.domain.View;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ViewRepository extends ReactiveCrudRepository<View, String> {
    Mono<Long> countByPostId(String postId);
    Mono<View> findByPostIdAndUniqueId(String postId, String uniqueId);
}
