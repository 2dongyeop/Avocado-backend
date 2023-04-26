package io.wisoft.capstonedesign.global.jwt;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {

    boolean existsByToken(final String token);
}
