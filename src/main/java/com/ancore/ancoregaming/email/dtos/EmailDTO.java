package com.ancore.ancoregaming.email.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDTO {
    private String addressee;
    private String username;
    private String token;

}
