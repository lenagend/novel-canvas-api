package com.kkm.novelcanvasapi.web;

import com.kkm.novelcanvasapi.dto.PostWithUserInfo;
import com.kkm.novelcanvasapi.service.PostService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public Flux<PostWithUserInfo> getPosts(@RequestParam int page, @RequestParam int size,
                                           @RequestParam String category,
                                           @RequestParam(required = false, defaultValue = ".*") String searchQuery,
                                           @RequestParam(required = false) String startDate,
                                           @RequestParam(required = false) String endDate,
                                           @RequestParam String sortType) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postService.getPosts(pageable, category, searchQuery, searchQuery, true);

    }


    @GetMapping("/count")
    public Mono<Long> getPostCount(@RequestParam String category,
                                   @RequestParam(required = false, defaultValue = ".*") String searchQuery,
                                   @RequestParam(required = false) String startDate,
                                   @RequestParam(required = false) String endDate,
                                   @RequestParam String sortType) {
            return postService.countPosts(category, searchQuery, searchQuery,true);
    }

    @GetMapping("/{postId}")
    public Mono<PostWithUserInfo> getPost(@PathVariable String postId){
        return postService.getPost(postId);
    }

    @PostMapping("/{postId}/view")
    public Mono<Void> incrementViewCount(@PathVariable String postId) {
        return postService.incrementViewCount(postId);
    }
}
