package com.games.web.app.auth;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@ToString
public class LoginRequest {
    private final String username;
    private final String password;
}
