package com.games.web.app.auth;

import com.games.web.app.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
@AllArgsConstructor
public class EmailValidator implements Predicate<String> {
    private final UserRepo userRepo;

    @Override
    public boolean test(String email) {
        // reger para validar el email
        return true;
    }
}
