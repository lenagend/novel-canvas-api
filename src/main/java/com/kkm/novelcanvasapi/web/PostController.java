package com.kkm.novelcanvasapi.web;

import com.kkm.novelcanvasapi.domain.Post;
import com.kkm.novelcanvasapi.dto.PostWithDetails;
import com.kkm.novelcanvasapi.service.PostService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public Flux<PostWithDetails> getPosts(@RequestParam int page, @RequestParam int size,
                                          @RequestParam String category,
                                          @RequestParam(required = false, defaultValue = ".*") String searchQuery,
                                          @RequestParam(required = false) String startDate,
                                          @RequestParam(required = false) String endDate,
                                          @RequestParam String sortType) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postService.getPosts(pageable, category, searchQuery, searchQuery, true);

    }


    @GetMapping("/posts/count")
    public Mono<Long> getPostCount(@RequestParam String category,
                                   @RequestParam(required = false, defaultValue = ".*") String searchQuery,
                                   @RequestParam(required = false) String startDate,
                                   @RequestParam(required = false) String endDate,
                                   @RequestParam String sortType) {
            return postService.countPosts(category, searchQuery, searchQuery,true).doOnNext(System.out::println);
    }
}
