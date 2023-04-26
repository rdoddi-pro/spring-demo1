package net.iremote.thiru.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDto {
    private String id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private List<String> roles;
}
