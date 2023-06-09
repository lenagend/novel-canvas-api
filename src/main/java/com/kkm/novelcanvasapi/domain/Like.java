package com.kkm.novelcanvasapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Like {
    private @Id String id;
    private String postId;
    private String commentId;
    private String username;
    private LocalDateTime createdAt;

    public Like(String postId, String commentId, String username, LocalDateTime createdAt) {
        this.postId = postId;
        this.commentId = commentId;
        this.username = username;
        this.createdAt = createdAt;
    }
}
