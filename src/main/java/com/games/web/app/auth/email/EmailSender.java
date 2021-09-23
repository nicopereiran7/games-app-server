package com.games.web.app.auth.email;

public interface EmailSender {
    void send(String to, String email);
}
