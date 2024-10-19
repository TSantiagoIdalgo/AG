package com.ancore.ancoregaming.auth.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record JwtResponse(
        @JsonProperty("access_token")
        String access_token,
        @JsonProperty("refresh_token")
        String refresh_token) {

}
