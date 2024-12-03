package com.ancore.ancoregaming.whitelist.dtos;

import java.util.UUID;

import com.ancore.ancoregaming.product.dtos.ProductDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WhitelistItemDTO {
    private UUID id;
    private ProductDTO product;

    public WhitelistItemDTO() {
    }

    public WhitelistItemDTO(UUID id, ProductDTO product) {
        this.id = id;
        this.product = product;
    }

}
