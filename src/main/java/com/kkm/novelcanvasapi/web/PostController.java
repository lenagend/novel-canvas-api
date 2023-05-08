package com.kkm.novelcanvasapi.web;

import com.kkm.novelcanvasapi.domain.Post;
import com.kkm.novelcanvasapi.dto.PostWithDetails;
import com.kkm.novelcanvasapi.service.PostService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public Flux<PostWithDetails> getPosts(@RequestParam int page, @RequestParam int size,
                                          @RequestParam(required = false) String startDate,
                                          @RequestParam(required = false) String endDate,
                                          @RequestParam String sortType) {

        Pageable pageable = PageRequest.of(page, size);
        return postService.getPosts(pageable, true);

    }
}
