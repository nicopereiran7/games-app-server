package com.games.web.app.controllers;

import com.games.web.app.models.AppUser;
import com.games.web.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET})
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;
    /*
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        User newUser = null;
        Map<String, Object> response = new HashMap<String, Object>();

        try {
            newUser = userService.addUser(user);
        }catch (DataAccessException e) {
            response.put("message", "Error al guardar usuario en la base de datos");
            response.put("error", e.getMostSpecificCause().getMessage());
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
    }
     */

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        AppUser user = null;
        try {
            user = userService.findUserById(id);
        }catch (DataAccessException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
