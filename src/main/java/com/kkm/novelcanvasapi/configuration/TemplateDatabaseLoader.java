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
                mongo.save(new Post("" + i, "작품" + i, "humor", "유머게시판 게시글" + i,  "testContent" + i,  "testuser1@gmail.com", "imageSrc", createdAt, modifiedAt, true, 0L, 0L, 0L));
            }
            for (int i = 100; i < 200; i++){
                LocalDateTime createdAt = baseDate.plusMinutes(i * 10);
                LocalDateTime modifiedAt = createdAt;
                mongo.save(new Post("" + i, "작품" + i, "sports", "스포츠게시판 게시글" + i,  "testContent" + i,  "testuser1@gmail.com", "imageSrc", createdAt, modifiedAt, true, 0L, 0L, 0L));
            }
            for (int i = 200; i < 300; i++){
                LocalDateTime createdAt = baseDate.plusMinutes(i * 10);
                LocalDateTime modifiedAt = createdAt;
                mongo.save(new Post("" + i, "작품" + i, "politics", "정치게시판 게시글" + i,  "testContent" + i,  "testuser1@gmail.com", "imageSrc", createdAt, modifiedAt, true , 0L, 0L, 0L));
            }
            for (int i = 300; i < 400; i++){
                LocalDateTime createdAt = baseDate.plusMinutes(i * 10);
                LocalDateTime modifiedAt = createdAt;
                mongo.save(new Post("" + i, "작품" + i, "stock", "주식게시판 게시글" + i,  "testContent" + i,  "testuser1@gmail.com", "imageSrc", createdAt, modifiedAt, true , 0L, 0L, 0L));
            }
            for (int i = 400; i < 500; i++){
                LocalDateTime createdAt = baseDate.plusMinutes(i * 10);
                LocalDateTime modifiedAt = createdAt;
                mongo.save(new Post("" + i, "작품" + i, "hotDeal", "핫딜게시판 게시글" + i,  "testContent" + i,  "testuser1@gmail.com", "imageSrc", createdAt, modifiedAt, true , 0L, 0L, 0L));
            }
            for (int i = 500; i < 600; i++){
                LocalDateTime createdAt = baseDate.plusMinutes(i * 10);
                LocalDateTime modifiedAt = createdAt;
                mongo.save(new Post("" + i, "작품" + i, "19", "19게시판 게시글" + i,  "testContent" + i,  "testuser1@gmail.com", "imageSrc", createdAt, modifiedAt, true , 0L, 0L, 0L));
            }
        };
    }
}
