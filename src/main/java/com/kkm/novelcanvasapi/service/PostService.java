package com.kkm.novelcanvasapi.service;

import com.kkm.novelcanvasapi.domain.Like;
import com.kkm.novelcanvasapi.domain.Post;
import com.kkm.novelcanvasapi.domain.UserInfo;
import com.kkm.novelcanvasapi.domain.View;
import com.kkm.novelcanvasapi.dto.PostWithUserInfo;
import com.kkm.novelcanvasapi.repository.*;
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

    private final UserInfoRepository userInfoRepository;

    public PostService(PostRepository postRepository, LikeRepository likeRepository, ViewRepository viewRepository, UserInfoRepository userInfoRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.viewRepository = viewRepository;
        this.userInfoRepository = userInfoRepository;
    }

    public Mono<Post> savePost(Post post) {
        return this.postRepository.save(post)
                .onErrorResume(e -> {
                    System.err.println("Error during save: " + e.getMessage());
                    return Mono.error(new RuntimeException("Could not save post", e));
                });
    }

    public Mono<PostWithUserInfo> createPostWithUserInfo(Post post) {
        return this.userInfoRepository.findByUsername(post.getUsername())
                .switchIfEmpty(Mono.just(new UserInfo( "Anonymous", "defaultProfileImage")))
                .map(user -> {
                    PostWithUserInfo postWithUserInfo = PostWithUserInfo.fromPost(post);
                    postWithUserInfo.setNickname(user.getNickname());
                    postWithUserInfo.setProfileImage(user.getProfileImage());
                    return postWithUserInfo;
                });
    }


    public Flux<PostWithUserInfo> getPosts(Pageable pageable, String category, String username, String title,  boolean published, LocalDateTime startDate, LocalDateTime endDate) {
        if(startDate == null || endDate == null) {
            return this.postRepository.findAllByCategoryAndPublishedAndUsernameOrTitle(pageable, category, username, title, published)
                    .onErrorResume(e -> {
                        System.err.println("Error during findAllByCategoryAndPublishedAndUsernameOrTitle: " + e.getMessage());
                        return Flux.error(new RuntimeException("Could not get posts", e));
                    })
                    .concatMap(this::createPostWithUserInfo);
        } else {
            return this.postRepository.findAllByCategoryAndPublishedAndUsernameOrTitle(pageable, category, username, title, published, startDate, endDate)
                    .onErrorResume(e -> {
                        System.err.println("Error during findAllByCategoryAndPublishedAndUsernameOrTitle: " + e.getMessage());
                        return Flux.error(new RuntimeException("Could not get posts", e));
                    })
                    .concatMap(this::createPostWithUserInfo);
        }
    }


    public Mono<PostWithUserInfo> getPost(String postId) {
        return this.postRepository.findById(postId)
                .onErrorResume(e -> {
                    System.err.println("Error during findById: " + e.getMessage());
                    return Mono.error(new RuntimeException("Could not find post", e));
                })
                .flatMap(this::createPostWithUserInfo);
    }


    public Mono<Long> countPosts(String category, String username, String title, boolean published) {
        return postRepository.countByCategoryAndUsernameOrTitleAndPublished(category, username, title, published)
                .onErrorResume(e -> {
                    System.err.println("Error during countByCategoryAndUsernameOrTitleAndPublished: " + e.getMessage());
                    return Mono.error(new RuntimeException("Could not count posts", e));
                });
    }


    public Mono<View> incrementViewCount(String postId, String uniqueId) {
        return this.viewRepository.findByPostIdAndUniqueId(postId, uniqueId)
                .switchIfEmpty(viewRepository.save(new View(postId, uniqueId, LocalDateTime.now()))
                        .flatMap(view -> {
                            return postRepository.findById(postId)
                                    .flatMap(post -> {
                                        post.incrementViewCount();
                                        return postRepository.save(post);
                                    })
                                    .thenReturn(view);
                        }))
                .onErrorResume(e -> {
                    System.err.println("Error during incrementing view count: " + e.getMessage());
                    return Mono.error(new RuntimeException("Could not increment view count", e));
                });
    }



    public Mono<Boolean> togglePostLike(String postId, String username) {
        return likeRepository.findByUsernameAndPostId(username, postId)
                .flatMap(like ->
                        likeRepository.delete(like)
                                .then(postRepository.findById(postId)
                                        .flatMap(post -> {
                                            post.decrementLikeCount();
                                            return postRepository.save(post);
                                        }))
                                .thenReturn(false)
                )
                .switchIfEmpty(
                        likeRepository.save(new Like(postId, null, username, LocalDateTime.now()))
                                .then(postRepository.findById(postId)
                                        .flatMap(post -> {
                                            post.incrementLikeCount();
                                            return postRepository.save(post);
                                        }))
                                .thenReturn(true)
                )
                .onErrorResume(e -> {
                    System.err.println("Error during toggling post like: " + e.getMessage());
                    return Mono.error(new RuntimeException("Could not toggle post like", e));
                });
    }



    public Mono<Boolean> toggleCommentLike(String commentId, String username) {
        return likeRepository.findByUsernameAndCommentId(username, commentId)
                .flatMap(like -> likeRepository.delete(like).thenReturn(false))
                .onErrorResume(e -> {

                    System.err.println("Error during deletion: " + e.getMessage());
                    return Mono.error(new RuntimeException("Could not delete like", e));
                })
                .switchIfEmpty(likeRepository.save(new Like(null, commentId, username, LocalDateTime.now())).thenReturn(true))
                .onErrorResume(e -> {

                    System.err.println("Error during save: " + e.getMessage());
                    return Mono.error(new RuntimeException("Could not save like", e));
                });
    }


    public Mono<Boolean> isPostLikedByUser(String postId, String username) {
        return likeRepository.existsByPostIdAndUsername(postId, username)
                .onErrorResume(e -> {
                            System.err.println("Error during isPostLikedByUser: " + e.getMessage());
                            return Mono.error(new RuntimeException("Could not get isPostLikedByUser", e));
                        }
                );
    }

    public Mono<Long> getPostLikeCount(String postId) {
        return likeRepository.countByPostId(postId)
                .onErrorResume(e-> {
                    System.err.println("Error during getPostLikeCount: " + e.getMessage());
                    return Mono.error(new RuntimeException("Could not get likeCount", e));
                });
    }

    public Mono<Long> countByUsername(String username, boolean published) {
        return this.postRepository.countByUsernameAndPublished(username, published);
    }

    public Mono<Long> countCommentByUsername(String username, boolean published) {
        return likeRepository.countByUsername(username);
    }

    public Mono<Long> countPostsLikedByUsername(String username) {
        return likeRepository.countByUsername(username);
    }

}
