package net.iremote.thiru.service;

import lombok.extern.slf4j.Slf4j;
import net.iremote.thiru.dto.LoginDto;
import net.iremote.thiru.entity.User;
import net.iremote.thiru.entity.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AuthenticationService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserData userData;

    public void authenticate(LoginDto loginDto) throws AuthenticationException {
        Optional<User> user = userData.getActiveUsers().parallelStream().filter(userObj -> userObj.getUsername().equalsIgnoreCase(loginDto.getUsername())).findAny();
        if (!user.isPresent() || !this.passwordEncoder.matches(loginDto.getPassword(), user.get().getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }
    }
}
