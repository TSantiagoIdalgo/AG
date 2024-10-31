package com.ancore.ancoregaming.whitelist.services;

import com.ancore.ancoregaming.product.model.Product;
import com.ancore.ancoregaming.product.services.product.ProductService;
import com.ancore.ancoregaming.user.model.User;
import com.ancore.ancoregaming.user.services.user.IUserService;
import com.ancore.ancoregaming.whitelist.model.Whitelist;
import com.ancore.ancoregaming.whitelist.repositories.IWhitelistRepository;
import com.cloudinary.api.exceptions.AlreadyExists;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
  public Whitelist findByUserAndProduct(String userId, String productId) {
    Optional<Whitelist> whitelist = this.whitelistRepository.findByUserIdAndProductId(userId, UUID.fromString(productId));
    if (whitelist.isEmpty()) {
      throw new EntityNotFoundException("User whitelist not found");
    }
    return whitelist.get();
  }

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
    Whitelist userWhitelist = this.whitelistRepository.findByUserEmail(userId);
    if (userWhitelist == null) {
      throw new EntityNotFoundException("User whitelist not found");
    }

    return userWhitelist;
  }

  @Override
  public Whitelist addProductToWhitelist(String userId, String productId) throws AlreadyExists {
    Product product = this.productService.findProduct(productId);
    User user = this.userService.findUser(userId);

    Whitelist userWhitelist = this.findOrCreateWhitelist(user);
    if (userWhitelist.getProducts().contains(product)) {
      throw new AlreadyExists("The product is already added to the whitelist");
    }
    userWhitelist.getProducts().add(product);

    return this.whitelistRepository.save(userWhitelist);
  }

  @Override
  public Whitelist removeProductFromWhitelist(String userId, String productId) {
    Product product = this.productService.findProduct(productId);
    User user = this.userService.findUser(userId);

    Whitelist userWhitelist = this.whitelistRepository.findByUserEmail(user.getEmail());
    if (userWhitelist == null) {
      throw new EntityNotFoundException("User whitelist not found");
    }

    userWhitelist.getProducts().remove(product);
    return this.whitelistRepository.save(userWhitelist);
  }

  private Whitelist findOrCreateWhitelist(User user) {
    Whitelist whitelist = this.whitelistRepository.findByUserEmail(user.getEmail());
    if (whitelist == null) {
      ArrayList<Product> products = new ArrayList<>();
      whitelist = new Whitelist(user, products);
    }

    return whitelist;
  }

}
