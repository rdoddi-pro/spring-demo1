package net.iremote.thiru.controller;

import lombok.extern.slf4j.Slf4j;
import net.iremote.thiru.dto.UserDto;
import net.iremote.thiru.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/users/{userUid}")
    public UserDto getUserDetails(@PathVariable String userUid) {
        return userService.getUserDetails(userUid);
    }

    @PostMapping("/player/register")
    public ResponseEntity<String> createUser(@RequestBody UserDto userDto) {
        if (!userService.isUsernameValid(userDto.getUsername())) {
            return new ResponseEntity<>("Username already in use", HttpStatus.BAD_REQUEST);
        }
        userService.createUser(userDto);
        return new ResponseEntity<>("Successfully created", HttpStatus.OK);
    }

    @PutMapping("/users/{userUid}")
    public void updateUserDetails(@PathVariable String userUid, @RequestBody UserDto userDto) {
        userService.updateUserDetails(userUid, userDto);
    }

    @DeleteMapping("/users/{userUid}")
    public ResponseEntity<String> deleteUser(@PathVariable String userUid) {
        userService.deleteUser(userUid);
        return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
    }
}
