package com.kkm.novelcanvasapi.repository;

import com.kkm.novelcanvasapi.domain.UserInfo;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserInfoRepository extends ReactiveCrudRepository<UserInfo, Long> {
    Mono<UserInfo> findByUsername(String username);
    Mono<Boolean> existsByNickname(String nickname);
    Mono<Void> deleteByUsername(String username);

}