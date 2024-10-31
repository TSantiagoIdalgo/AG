package com.ancore.ancoregaming.whitelist.services;

import com.ancore.ancoregaming.product.model.Product;
import com.ancore.ancoregaming.product.services.product.ProductService;
import com.ancore.ancoregaming.user.model.User;
import com.ancore.ancoregaming.user.services.user.IUserService;
import com.ancore.ancoregaming.whitelist.model.Whitelist;
import com.ancore.ancoregaming.whitelist.repositories.IWhitelistRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WhitelistService implements IWhiteListService {

  @Autowired
  private IWhitelistRepository whitelistRepository;
  @Autowired
  private ProductService productService;
  @Autowired
  private IUserService userService;

  @Override

  public List<Whitelist> findAllWhitelist() {
    List<Whitelist> whitelists = this.whitelistRepository.findAll();
    if (whitelists.isEmpty()) {
      throw new EntityNotFoundException("Whitelists not found");
    }

    return whitelists;
  }

  @Override
  public Whitelist findUserWhiteList(String userId) {
    Whitelist userWhitelist = this.whitelistRepository.findByUserId(userId);
    if (userWhitelist == null) {
      throw new EntityNotFoundException("User whitelist not found");
    }

    return userWhitelist;
  }

  @Override
  public Whitelist addProductToWhitelist(String userId, String productId) {
    Product product = this.productService.findProduct(productId);
    User user = this.userService.findUser(userId);

    Whitelist userWhitelist = this.findOrCreateWhitelist(user);
    userWhitelist.getProducts().add(product);

    return this.whitelistRepository.save(userWhitelist);
  }

  @Override
  public Whitelist removeProductFromWhitelist(String userId, String productId) {
    Product product = this.productService.findProduct(productId);
    User user = this.userService.findUser(userId);

    Whitelist userWhitelist = this.whitelistRepository.findByUserId(user.getEmail());
    if (userWhitelist == null) {
      throw new EntityNotFoundException("User whitelist not found");
    }

    userWhitelist.getProducts().remove(product);
    return this.whitelistRepository.save(userWhitelist);
  }

  private Whitelist findOrCreateWhitelist(User user) {
    Whitelist whitelist = this.whitelistRepository.findByUserId(user.getEmail());
    if (whitelist == null) {
      return this.createWhitelist(user);
    }

    return whitelist;
  }

  private Whitelist createWhitelist(User user) {
    ArrayList<Product> products = new ArrayList<>();
    Whitelist whitelist = new Whitelist(user, products);
    this.whitelistRepository.save(whitelist);

    return whitelist;
  }
}
