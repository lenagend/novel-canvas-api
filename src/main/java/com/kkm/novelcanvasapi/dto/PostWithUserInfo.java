package com.kkm.novelcanvasapi.dto;

import com.kkm.novelcanvasapi.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostWithUserInfo {
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

    private String nickname;
    private String profileImage;

    public static PostWithUserInfo fromPost(Post post) {
        PostWithUserInfo postWithUserInfo = new PostWithUserInfo();
        postWithUserInfo.setId(post.getId());
        postWithUserInfo.setNovelId(post.getNovelId());
        postWithUserInfo.setTitle(post.getTitle());
        postWithUserInfo.setContent(post.getContent());
        postWithUserInfo.setUsername(post.getUsername());
        postWithUserInfo.setImageSrc(post.getImageSrc());
        postWithUserInfo.setCreatedAt(post.getCreatedAt());
        postWithUserInfo.setModifiedAt(post.getModifiedAt());
        postWithUserInfo.setPublished(post.isPublished());
        postWithUserInfo.setViewCount(post.getViewCount());
        postWithUserInfo.setLikeCount(post.getLikeCount());
        postWithUserInfo.setCommentCount(post.getCommentCount());
        return postWithUserInfo;
    }
}
