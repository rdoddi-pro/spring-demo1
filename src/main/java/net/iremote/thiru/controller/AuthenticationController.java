package net.iremote.thiru.controller;

import net.iremote.thiru.dto.AuthTokenDto;
import net.iremote.thiru.dto.LoginDto;
import net.iremote.thiru.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/signIn")
    public ResponseEntity<AuthTokenDto> signIn(@RequestBody LoginDto loginDto) {
        AuthTokenDto authTokenDto;
        if (!StringUtils.hasText(loginDto.getUsername()) || !StringUtils.hasText(loginDto.getPassword())) {
            authTokenDto = getAuthTokenDto("Fail", "Mandatory data missing");
            return new ResponseEntity<>(authTokenDto, HttpStatus.BAD_REQUEST);
        }
        try {
            authenticationService.authenticate(loginDto);
        } catch (AuthenticationException e) {
            authTokenDto = getAuthTokenDto("Fail", "Incorrect username or password");
            return new ResponseEntity<>(authTokenDto, HttpStatus.BAD_REQUEST);
        }
        authTokenDto = getAuthTokenDto("Success", "Successfully logged in");
        return new ResponseEntity<>(authTokenDto, HttpStatus.OK);
    }

    private static AuthTokenDto getAuthTokenDto(String status, String message) {
        AuthTokenDto authTokenDto = new AuthTokenDto();
        authTokenDto.setStatus(status);
        authTokenDto.setMessage(message);
        return authTokenDto;
    }
}
