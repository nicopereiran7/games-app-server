package com.games.web.app.controllers;

import com.games.web.app.auth.LoginRequest;
import com.games.web.app.auth.RegistrationRequest;
import com.games.web.app.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET,RequestMethod.POST})
@RequestMapping("/api")
@AllArgsConstructor
public class AuthRestController {

    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
        Map<String, Object> response = new HashMap<String, Object>();

        try {
            String token = authService.register(request);
            response.put("success", "Usuario creado correctamente");
            response.put("confirmation_token", token);
        }catch (IllegalStateException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/register/confirm")
    public ResponseEntity<?> confirm(@RequestParam("token") String token) {
        Map<String, Object> response = new HashMap<>();

        try{
            String message = authService.confirmToken(token);
            response.put("message", message);
        }catch (IllegalStateException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Map<String, Object> response = new HashMap<String, Object>();

        try {
            Map<String, Object> login = authService.login(request);

            for(Map.Entry<String, Object> entry : login.entrySet()) {
                response.put(entry.getKey(), entry.getValue());
            }
        }catch (IllegalStateException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
