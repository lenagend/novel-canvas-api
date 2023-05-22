package com.kkm.novelcanvasapi.web;

import com.kkm.novelcanvasapi.domain.View;
import com.kkm.novelcanvasapi.dto.PostWithUserInfo;
import com.kkm.novelcanvasapi.service.AuthenticationService;
import com.kkm.novelcanvasapi.service.PostService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;
    private final AuthenticationService authenticationService;

    public PostController(PostService postService, AuthenticationService authenticationService) {
        this.postService = postService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/posts")
    public Mono<ResponseEntity<Flux<PostWithUserInfo>>> getPosts(@RequestParam int page, @RequestParam int size,
                                                                 @RequestParam String category,
                                                                 @RequestParam(required = false, defaultValue = ".*") String searchQuery,
                                                                 @RequestParam(required = false) String startDate,
                                                                 @RequestParam(required = false) String endDate,
                                                                 @RequestParam String sortType) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startDateParsed = null, endDateParsed = null;

            if (startDate != null) {
                startDateParsed = LocalDateTime.parse(startDate, formatter);
            }
            if (endDate != null) {
                endDateParsed = LocalDateTime.parse(endDate, formatter);
            }

            Pageable pageable = PageRequest.of(page, size, Sort.by(sortType).descending());
            return Mono.just(ResponseEntity.ok(postService.getPosts(pageable, category, searchQuery, searchQuery, true, startDateParsed, endDateParsed)));
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        }
    }



    @GetMapping("/posts/count")
    public Mono<ResponseEntity<Long>> getPostCount(@RequestParam String category,
                                                   @RequestParam(required = false, defaultValue = ".*") String searchQuery,
                                                   @RequestParam(required = false) String startDate,
                                                   @RequestParam(required = false) String endDate,
                                                   @RequestParam String sortType) {
        try {
            return postService.countPosts(category, searchQuery, searchQuery, true)
                    .map(ResponseEntity::ok)
                    .defaultIfEmpty(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        }
    }

    @GetMapping("/posts/{postId}")
    public Mono<ResponseEntity<PostWithUserInfo>> getPost(@PathVariable String postId){
        try {
            return postService.getPost(postId)
                    .map(ResponseEntity::ok)
                    .defaultIfEmpty(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        }
    }

    @PostMapping("/posts/{postId}/view")
    public Mono<ResponseEntity<View>> incrementViewCount(@PathVariable String postId, @RequestParam String uniqueId) {
        try {
            return postService.incrementViewCount(postId, uniqueId)
                    .map(ResponseEntity::ok)
                    .defaultIfEmpty(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        }
    }


    @PutMapping("/posts/{postId}/like")
    public Mono<ResponseEntity<Void>> togglePostLike(@PathVariable String postId, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String username = authenticationService.extractUsername(token);
            return postService.togglePostLike(postId, username)
                    .thenReturn(ResponseEntity.ok().build());
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }
    }

    @PutMapping("/comments/{commentId}/like")
    public Mono<ResponseEntity<Void>> toggleCommentLike(@PathVariable String commentId, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String username = authenticationService.extractUsername(token);
            return postService.toggleCommentLike(commentId, username)
                    .thenReturn(ResponseEntity.ok().build());
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }
    }

    @GetMapping("/posts/{postId}/liked")
    public Mono<ResponseEntity<Boolean>> isPostLikedByUser(@PathVariable String postId, @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String username = authenticationService.extractUsername(token);
            return postService.isPostLikedByUser(postId, username)
                    .map(ResponseEntity::ok)
                    .defaultIfEmpty(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        }
    }

    @GetMapping("/posts/{postId}/likeCount")
    public Mono<ResponseEntity<Long>> getPostLikeCount(@PathVariable String postId) {
        try{
            return postService.getPostLikeCount(postId)
                    .map(ResponseEntity::ok)
                    .defaultIfEmpty(ResponseEntity.notFound().build());
        }catch (Exception e){
            return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        }
    }

}
