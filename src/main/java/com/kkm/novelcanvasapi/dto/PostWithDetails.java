package com.kkm.novelcanvasapi.dto;

import com.kkm.novelcanvasapi.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostWithDetails {
    private String id;
    private String novelId;
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

    public static PostWithDetails fromPost(Post post) {
        PostWithDetails postWithDetails = new PostWithDetails();
        postWithDetails.setId(post.getId());
        postWithDetails.setNovelId(post.getNovelId());
        postWithDetails.setTitle(post.getTitle());
        postWithDetails.setContent(post.getContent());
        postWithDetails.setUsername(post.getUsername());
        postWithDetails.setImageSrc(post.getImageSrc());
        postWithDetails.setCreatedAt(post.getCreatedAt());
        postWithDetails.setModifiedAt(post.getModifiedAt());
        postWithDetails.setPublished(post.isPublished());
        return postWithDetails;
    }
}
