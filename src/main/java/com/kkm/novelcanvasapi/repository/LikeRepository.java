package com.kkm.novelcanvasapi.repository;

import com.kkm.novelcanvasapi.domain.Like;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface LikeRepository extends ReactiveCrudRepository<Like, String> {
    Mono<Like> findByUsernameAndPostId(String username, String postId);
    Mono<Like> findByUsernameAndCommentId(String username, String commentId);
    Mono<Long> countByUsername(String username);
    Mono<Boolean> existsByPostIdAndUsername(String postId, String username);
    Mono<Long> countByPostId(String postId);
}
