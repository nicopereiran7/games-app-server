package com.games.web.app.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.games.web.app.auth.token.ConfirmationToken;
import com.games.web.app.models.AppUser;
import com.games.web.app.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final static String USER_NOT_FOUND_MSG = "El usuario %s no fue encontrado";

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public AppUser findUserById(Long id) {
        AppUser userFind = userRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("Usuario " + id + "no encontado"));
        return userFind;
    }

    public String signUpUser(AppUser user) {
        boolean emailExists = userRepo.findByEmail(user.getEmail()).isPresent();
        boolean usernameExists = userRepo.findByUsername(user.getUsername()).isPresent();

        if(emailExists){
            throw new IllegalStateException("Email " + user.getEmail() + " ya esta registrado");
        }

        if(usernameExists) {
            throw new IllegalStateException("El username " + user.getUsername() + " ya esta registrado");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepo.save(user);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token, LocalDateTime.now()
                , LocalDateTime.now().plusMinutes(15),
                user
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);


        return token;
    }

    public Map<String, Object> loginUser(String username, String password) {
        Map<String, Object> response = new HashMap<>();
        AppUser user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username)));

        if(user == null) {
            throw new IllegalStateException("Email o contraseña incorrectos");
        }

        boolean passwordCorrect = bCryptPasswordEncoder.matches(password, user.getPassword());

        if(!passwordCorrect) {
            throw new IllegalStateException("Email o contraseña incorrectos");
        }

        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withClaim("id", user.getId())
                .withClaim("name", user.getFirstName())
                .withClaim("lastname", user.getLastname())
                .withClaim("email", user.getEmail())
                .withClaim("rol", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .withClaim("name", user.getFirstName())
                .withClaim("lastname", user.getLastname())
                .withClaim("email", user.getEmail())
                .sign(algorithm);


        response.put("access_token", access_token);
        response.put("refresh_token", refresh_token);
        AppUser sendUser = new AppUser(user.getId(), user.getFirstName(), user.getLastname(), user.getUsername(), user.getEmail(), user.getRol());
        response.put("user", sendUser);
        return response;

    }

    public void enableUser(String email) {
        userRepo.enableUser(email);
    }
}
