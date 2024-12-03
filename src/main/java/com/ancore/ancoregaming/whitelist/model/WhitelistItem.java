package com.ancore.ancoregaming.whitelist.model;

import java.util.UUID;

import com.ancore.ancoregaming.product.model.Product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "whitelist_products")
public class WhitelistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "whitelist_id", nullable = false)
    private Whitelist whitelist;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public WhitelistItem(Product product, Whitelist whitelist) {
        this.product = product;
        this.whitelist = whitelist;
    }

    public WhitelistItem() {
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("WhitelistItem{");
        sb.append("id=").append(id);
        sb.append(", whitelist=").append(whitelist);
        sb.append(", product=").append(product);
        sb.append('}');
        return sb.toString();
    }

}
