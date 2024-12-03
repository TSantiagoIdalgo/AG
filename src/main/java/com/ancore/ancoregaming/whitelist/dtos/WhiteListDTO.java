package com.ancore.ancoregaming.whitelist.dtos;

import java.util.List;
import java.util.UUID;

import com.ancore.ancoregaming.user.dtos.UserDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WhiteListDTO {

  private UUID id;
  private UserDTO user;
  private List<WhitelistItemDTO> whitelistItems;

  public WhiteListDTO() {
  }

  public WhiteListDTO(UUID id, UserDTO user, List<WhitelistItemDTO> whitelistItemDTO) {
    this.id = id;
    this.user = user;
    this.whitelistItems = whitelistItemDTO;
  }

  @Override
  public String toString() {
    return "WhiteListDTO{" + "id=" + id + ", user=" + user + ", whitelistItemDTO=" + whitelistItems + '}';
  }

}
