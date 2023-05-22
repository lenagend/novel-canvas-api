package com.kkm.novelcanvasapi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    @Id
    private String id;
    @Indexed(unique = true) // username에 유일성 제약 조건 추가
    private String username;
    private String nickname;
    private String profileImage;
    private Long postCount;
    private Long commentCount;
    private Long likeCount;
    private LocalDateTime modifiedAt;

    public UserInfo(String nickname, String profileImage) {
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}