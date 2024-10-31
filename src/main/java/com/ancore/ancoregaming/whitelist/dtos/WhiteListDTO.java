package com.ancore.ancoregaming.whitelist.dtos;

import com.ancore.ancoregaming.product.dtos.ProductDTO;
import com.ancore.ancoregaming.user.dtos.UserDTO;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WhiteListDTO {

  private UUID id;
  private UserDTO user;
  private List<ProductDTO> products;

  public WhiteListDTO() {
  }

  public WhiteListDTO(UUID id, UserDTO user, List<ProductDTO> products) {
    this.id = id;
    this.user = user;
    this.products = products;
  }

  @Override
  public String toString() {
    return "WhiteListDTO{" + "id=" + id + ", user=" + user + ", products=" + products + '}';
  }

}
