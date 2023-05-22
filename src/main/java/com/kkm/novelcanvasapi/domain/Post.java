package com.kkm.novelcanvasapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    private @Id String id;
    private String novelId;
    private String category;
    private String title;
    private String content;
    private String username;
    private String imageSrc;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private boolean published;

    private Long viewCount;
    private Long likeCount;
    private Long commentCount;

    public void incrementViewCount() {
        this.viewCount++;
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }
}
