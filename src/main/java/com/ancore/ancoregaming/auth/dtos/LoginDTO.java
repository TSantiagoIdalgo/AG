package com.ancore.ancoregaming.auth.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginDTO(
        @JsonProperty("email")
        String email,
        @JsonProperty("password")
        String password) {

}
