package com.ancore.ancoregaming.user.dtos;

import java.util.Optional;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {

  private Optional<String> username = Optional.empty();
  private Optional<String> email = Optional.empty();
}
