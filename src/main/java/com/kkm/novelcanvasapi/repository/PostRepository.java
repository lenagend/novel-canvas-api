package com.kkm.novelcanvasapi.repository;

import com.kkm.novelcanvasapi.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PostRepository extends ReactiveCrudRepository<Post, String> {

    @Query("{ 'category': ?0, 'published': ?3, $or: [ { 'username': { $regex: ?1 } }, { 'title': { $regex: ?2 } } ] }")
    Flux<Post> findAllByCategoryAndPublishedAndUsernameOrTitle(Pageable pageable, String category, String username, String title, boolean published);

    @Query(value = "{ 'category': ?0, 'published': ?3, $or: [ { 'username': { $regex: ?1 } }, { 'title': { $regex: ?2 } } ] }", count = true)
    Mono<Long> countByCategoryAndUsernameOrTitleAndPublished(String category, String username, String title, boolean published);

}
