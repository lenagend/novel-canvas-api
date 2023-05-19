package com.kkm.novelcanvasapi.service;

import com.kkm.novelcanvasapi.domain.Like;
import com.kkm.novelcanvasapi.domain.Post;
import com.kkm.novelcanvasapi.domain.View;
import com.kkm.novelcanvasapi.dto.PostWithDetails;
import com.kkm.novelcanvasapi.repository.LikeRepository;
import com.kkm.novelcanvasapi.repository.PostRepository;
import com.kkm.novelcanvasapi.repository.ViewRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class PostService {
    private final PostRepository postRepository;

    private final LikeRepository likeRepository;

    private final ViewRepository viewRepository;

    public PostService(PostRepository postRepository, LikeRepository likeRepository, ViewRepository viewRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.viewRepository = viewRepository;
    }

    public Mono<Post> savePost(Post post){return this.postRepository.save(post);}

    public Flux<PostWithDetails> getPosts(Pageable pageable, String category, String username, String title, boolean published) {
        return this.postRepository.findAllByCategoryAndPublishedAndUsernameOrTitle(pageable, category, username, title, published)
                .concatMap(post ->
                        Mono.zip(
                                        this.viewRepository.countByPostId(post.getId()),
                                        this.viewRepository.countByPostId(post.getId()),
                                        this.viewRepository.countByPostId(post.getId())
                                )
                                .map(tuple -> {
                                    PostWithDetails postWithDetails = PostWithDetails.fromPost(post);
                                    postWithDetails.setViewCount(tuple.getT1());
                                    postWithDetails.setLikeCount(tuple.getT2());
                                    postWithDetails.setCommentCount(tuple.getT3());
                                    return postWithDetails;
                                })
                );
    }



    public Mono<Long> countPosts(String category, String username, String title, boolean published){
        return postRepository.countByCategoryAndUsernameOrTitleAndPublished(category, username, title, published);
    }

    public Mono<PostWithDetails> getPost(String postId){
        return this.postRepository.findById(postId)
                .flatMap(post ->
                        Mono.zip(
                                        this.viewRepository.countByPostId(post.getId()),
                                        this.viewRepository.countByPostId(post.getId()),
                                        this.viewRepository.countByPostId(post.getId())
                                )
                                .map(tuple -> {
                                    PostWithDetails postWithDetails = PostWithDetails.fromPost(post);
                                    postWithDetails.setViewCount(tuple.getT1());
                                    postWithDetails.setLikeCount(tuple.getT2());
                                    postWithDetails.setCommentCount(tuple.getT3());
                                    return postWithDetails;
                                })
                );
    }



    public Mono<View> incrementViewCount(String postId, String uniqueId) {
        return this.viewRepository.findByPostIdAndUniqueId(postId, uniqueId)
                .switchIfEmpty(viewRepository.save(new View(postId, uniqueId, LocalDateTime.now())));
    }


    public Mono<Boolean> toggleLike(String postId, String username) {
        return likeRepository.findByUsernameAndPostId(username, postId)
                .flatMap(like -> likeRepository.delete(like).thenReturn(false))
                .switchIfEmpty(likeRepository.save(new Like(postId, username, LocalDateTime.now())).thenReturn(true));
    }

    public Mono<Long> countByUsername(String username, boolean published){return this.postRepository.countByUsernameAndPublished(username, published);}
    public Mono<Long> countCommentByUsername(String username, boolean published){return likeRepository.countByUsername(username);}
    public Mono<Long> countPostsLikedByUsername(String username) {
        return likeRepository.countByUsername(username);
    }

}
