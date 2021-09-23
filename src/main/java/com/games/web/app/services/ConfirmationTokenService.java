package com.games.web.app.services;

import com.games.web.app.auth.token.ConfirmationToken;
import com.games.web.app.repository.ConfirmationTokenRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepo confirmationTokenRepo;

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepo.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepo.findByToken(token);
    }

    public void setConfirmedAt(String token) {
        confirmationTokenRepo.updateConfirmedAt(token, LocalDateTime.now());
    }
}
