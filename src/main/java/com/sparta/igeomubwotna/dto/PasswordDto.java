package com.sparta.igeomubwotna.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class PasswordDto {
    @JsonProperty
    private String password;

    @JsonCreator
    public PasswordDto(String password) {
        this.password = password;
    }
}
