package com.tlrhv3.sec.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class RegisterDto {

    @NonNull
    @Size(min = 3,max=15)
    private String username ;
    @NonNull
    @Size(min = 6,max=20)
    private String email ;
    @NonNull
    @Size(min = 6,max=15)
    private String password;





}
