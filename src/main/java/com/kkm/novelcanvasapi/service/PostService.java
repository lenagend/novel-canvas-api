package com.kkm.novelcanvasapi.service;

import com.kkm.novelcanvasapi.domain.Post;
import com.kkm.novelcanvasapi.dto.PostWithDetails;
import com.kkm.novelcanvasapi.repository.PostRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Mono<Post> savePost(Post post){return this.postRepository.save(post);}

    public Flux<PostWithDetails> getPosts(Pageable pageable, boolean published) {
        return this.postRepository.findAllByPublishedOrderByCreatedAtDesc(pageable, published)
                .concatMap(post -> {
                    PostWithDetails postWithDetails = PostWithDetails.fromPost(post);
                    postWithDetails.setViewCount(0L);
                    postWithDetails.setLikeCount(0L);
                    postWithDetails.setCommentCount(0L);
                    return Mono.just(postWithDetails);
                });
    }


    public Mono<Long> countPosts(boolean published){
        return postRepository.countByPublished(published);
    }

}
