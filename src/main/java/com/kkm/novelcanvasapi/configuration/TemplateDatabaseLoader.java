package com.kkm.novelcanvasapi.configuration;

import com.kkm.novelcanvasapi.domain.Post;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;

@Component
public class TemplateDatabaseLoader {
    @Bean
    CommandLineRunner initialize(MongoOperations mongo) {
        return args -> {
            LocalDateTime baseDate = LocalDateTime.of(2023, Month.APRIL, 24, 0, 0);
            for (int i = 0; i < 100; i++){
                LocalDateTime createdAt = baseDate.plusMinutes(i * 10);
                LocalDateTime modifiedAt = createdAt;
                mongo.save(new Post("" + i, "작품" + i, "제목" + i,  "testContent" + i,  "testuser1@gmail.com", "imageSrc", createdAt, modifiedAt, true));
            }
        };
    }
}
