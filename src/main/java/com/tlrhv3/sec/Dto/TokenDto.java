package com.tlrhv3.sec.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenDto {
    private Long userId;
    private String accessToken;
    private String refreshToken;


}
