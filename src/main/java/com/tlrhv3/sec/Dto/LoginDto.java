package com.tlrhv3.sec.Dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

}