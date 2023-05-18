package com.kkm.novelcanvasapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class View {
    private @Id String id;
    private String postId;
    private String uniqueId;
    private LocalDateTime createdAt;

    public View(String postId, String uniqueId, LocalDateTime createdAt) {
        this.postId = postId;
        this.uniqueId = uniqueId;
        this.createdAt = createdAt;
    }
}
