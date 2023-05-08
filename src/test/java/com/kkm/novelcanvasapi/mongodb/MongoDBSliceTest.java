package com.kkm.novelcanvasapi.mongodb;

import com.kkm.novelcanvasapi.domain.Post;
import com.kkm.novelcanvasapi.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class MongoDBSliceTest {
    @Autowired
    PostRepository postRepository;

    @Test
    void postRepositorySavesPosts(){
        Post samplePost = new Post();
        samplePost.setTitle("title1");

        postRepository.save(samplePost)
                .as(StepVerifier::create)
                .expectNextMatches(post -> {
                    assertThat(post.getId()).isNotNull();
                    assertThat(post.getTitle()).isEqualTo("title1");
                    System.out.println(post.toString());
                    return true;
                }).verifyComplete();
    }
}
