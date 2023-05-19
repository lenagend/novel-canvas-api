package com.kkm.novelcanvasapi.web;

import com.kkm.novelcanvasapi.domain.View;
import com.kkm.novelcanvasapi.dto.PostWithDetails;
import com.kkm.novelcanvasapi.service.AuthenticationService;
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
    private final AuthenticationService authenticationService;

    public PostController(PostService postService, AuthenticationService authenticationService) {
        this.postService = postService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public Flux<PostWithDetails> getPosts(@RequestParam int page, @RequestParam int size,
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
    public Mono<PostWithDetails> getPost(@PathVariable String postId){
        return postService.getPost(postId);
    }

    @PostMapping("/{postId}/view")
    public Mono<View> incrementViewCount(@PathVariable String postId, @RequestParam String uniqueId) {
        return postService.incrementViewCount(postId, uniqueId);
    }

    @PutMapping("/{postId}/like")
    public Mono<Boolean> toggleLike(@PathVariable String postId, @RequestHeader("Authorization") String authHeader) {
       try {
           String token = authHeader.replace("Bearer ", "");
           String username = authenticationService.extractUsername(token);
           return postService.toggleLike(postId, username);

       } catch (Exception e){
            return Mono.error(e);
       }
    }
}
