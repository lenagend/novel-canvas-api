package com.kkm.novelcanvasapi.service;

import com.kkm.novelcanvasapi.domain.Like;
import com.kkm.novelcanvasapi.domain.Post;
import com.kkm.novelcanvasapi.dto.PostWithUserInfo;
import com.kkm.novelcanvasapi.repository.LikeRepository;
import com.kkm.novelcanvasapi.repository.PostRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class PostService {
    private final PostRepository postRepository;


    private final LikeRepository likeRepository;

    private final ReactiveMongoOperations mongoOperations;

    public PostService(PostRepository postRepository, LikeRepository likeRepository, ReactiveMongoOperations mongoOperations) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.mongoOperations = mongoOperations;
    }

    public Mono<Post> savePost(Post post){return this.postRepository.save(post);}

    public Flux<PostWithUserInfo> getPosts(Pageable pageable, String category, String username, String title, boolean published) {
        return this.postRepository.findAllByCategoryAndPublishedAndUsernameOrTitle(pageable, category, username, title, published)
                .concatMap(post -> {
                    PostWithUserInfo postWithUserInfo = PostWithUserInfo.fromPost(post);
                    return Mono.just(postWithUserInfo);
                });
    }


    public Mono<Long> countPosts(String category, String username, String title, boolean published){
        return postRepository.countByCategoryAndUsernameOrTitleAndPublished(category, username, title, published);
    }

    public Mono<PostWithUserInfo> getPost(String postId){
        return this.postRepository.findById(postId)
                .map(post -> {
                    PostWithUserInfo postWithUserInfo = PostWithUserInfo.fromPost(post);
                    return postWithUserInfo;
                });
    }

    public Mono<Void> incrementViewCount(String postId) {
        Query query = new Query(Criteria.where("id").is(postId));
        Update update = new Update().inc("viewCount", 1);
        return mongoOperations.updateFirst(query, update, Post.class).then();
    }

    public Mono<Void> toggleLike(String postId, String username) {
        return likeRepository.findByUsernameAndPostId(username, postId)
                .flatMap(like -> likeRepository.delete(like).then(decrementLikeCountInPost(postId)))
                .switchIfEmpty(likeRepository.save(new Like(postId, username, LocalDateTime.now()))
                        .then(incrementLikeCountInPost(postId)));
    }

    private Mono<Void> incrementLikeCountInPost(String postId) {
        Query query = new Query(Criteria.where("id").is(postId));
        Update update = new Update().inc("likeCount", 1);
        return mongoOperations.updateFirst(query, update, Post.class).then();
    }

    private Mono<Void> decrementLikeCountInPost(String postId) {
        Query query = new Query(Criteria.where("id").is(postId));
        Update update = new Update().inc("likeCount", -1);
        return mongoOperations.updateFirst(query, update, Post.class).then();
    }
}
