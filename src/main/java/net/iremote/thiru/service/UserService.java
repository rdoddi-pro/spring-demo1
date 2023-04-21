package net.iremote.thiru.service;

import lombok.extern.slf4j.Slf4j;
import net.iremote.thiru.dto.UserDto;
import net.iremote.thiru.entity.Role;
import net.iremote.thiru.entity.User;
import net.iremote.thiru.entity.UserData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserData userData;

    public List<UserDto> getUsers() {
        return userData.getActiveUsers().parallelStream().map(this::getUserDto).collect(Collectors.toList());
    }

    private UserDto getUserDto(User user) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    public UserDto getUserDetails(String userUid) {
        UserDto userDto = null;
        Optional<User> user = userData.getActiveUsers().parallelStream().filter(obj -> obj.getId().equals(userUid)).findFirst();
        if (user.isPresent()) {
            userDto = getUserDto(user.get());
        }
        return userDto;
    }

    public UserDto createUser(UserDto userDto) {
        try {
            User user = new User();
            BeanUtils.copyProperties(userDto, user);
            user.setId(UUID.randomUUID().toString());
            Role role = new Role();
            role.setId("2");
            role.setName("Player");
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(userDto.getPassword()));
            user.setRoles(Collections.singletonList(role));
            userData.add(user);
        } catch (Exception e) {
            log.error("Error creating user", e);
            return null;
        }
        return userDto;
    }

    public boolean isUsernameValid(String username) {
        return !userData.getUsers().parallelStream().filter(user -> user.getUsername().equalsIgnoreCase(username)).findAny().isPresent();
    }

    public void deleteUser(String userUid) {
        userData.getActiveUsers().parallelStream().filter(user -> user.getId().equalsIgnoreCase(userUid)).findAny().ifPresent(user -> user.setDeleted(true));
    }

    public void updateUserDetails(String userUid, UserDto userDto) {
        Optional<User> userOptional = userData.getActiveUsers().parallelStream().filter(obj -> obj.getId().equals(userUid)).findFirst();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
        }
    }
}
