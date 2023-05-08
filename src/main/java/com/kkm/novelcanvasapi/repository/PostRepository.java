package com.kkm.novelcanvasapi.repository;

import com.kkm.novelcanvasapi.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PostRepository extends ReactiveCrudRepository<Post, String> {
    Flux<Post> findAllByPublishedOrderByCreatedAtDesc(Pageable pageable, boolean published);
    Mono<Long> countByPublished(boolean published);
}
