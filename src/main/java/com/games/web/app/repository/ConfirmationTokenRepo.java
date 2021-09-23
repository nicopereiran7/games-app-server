package com.games.web.app.repository;

import com.games.web.app.auth.token.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepo extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);

    @Modifying
    @Query(value = "UPDATE confirmation_token " +
            "SET confirmed_At = ?2 " +
            "WHERE token = ?1" , nativeQuery = true)
    void updateConfirmedAt(String token,
                          LocalDateTime confirmedAt);
}
