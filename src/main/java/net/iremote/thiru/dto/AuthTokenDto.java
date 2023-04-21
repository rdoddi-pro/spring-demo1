package net.iremote.thiru.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthTokenDto {
    private String status;
    private String message;
    private String accessToken;
}
