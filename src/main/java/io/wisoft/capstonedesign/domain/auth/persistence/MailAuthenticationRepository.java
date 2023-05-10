package io.wisoft.capstonedesign.domain.auth.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MailAuthenticationRepository extends JpaRepository<DBMailAuthentication, Long> {
    Optional<DBMailAuthentication> findByEmail(final String email);
    boolean existsByEmail(final String email);
}
