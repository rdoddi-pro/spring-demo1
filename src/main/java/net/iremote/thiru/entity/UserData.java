package net.iremote.thiru.entity;

import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UserData {
    private static List<User> users = new ArrayList<>();

    @PostConstruct
    private void addAdmin() {
        User admin = new User();
        admin.setId(UUID.randomUUID().toString());
        admin.setFirstName("Admin");
        admin.setUsername("admin");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        admin.setPassword(encoder.encode("admin"));
        Role role = new Role();
        role.setId("1");
        role.setName("Admin");
        admin.setRoles(Collections.singletonList(role));
        users.add(admin);
    }

    public void add(User user) {
        users.add(user);
    }

    public void deleteUser(String id) {
        users.parallelStream().filter(user -> user.getId().equals(id)).findFirst().ifPresent(obj -> obj.setDeleted(true));
    }

    public List<User> getUsers() {
        return this.users;
    }

    public List<User> getActiveUsers() {
        return users.parallelStream().filter(user -> !user.isDeleted()).collect(Collectors.toList());
    }
}
