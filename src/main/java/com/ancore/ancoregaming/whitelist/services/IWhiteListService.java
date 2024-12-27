package com.ancore.ancoregaming.whitelist.services;

import com.ancore.ancoregaming.whitelist.model.Whitelist;
import java.util.List;

public interface IWhiteListService {

  public List<Whitelist> findAllWhitelist();

  public Whitelist findUserWhiteList(String userId);

  public Whitelist findByUserAndProduct(String userId, String productId);

  public Whitelist addProductToWhitelist(String userId, String productId) throws Exception;

  public Whitelist removeProductFromWhitelist(String userId, String productId);
}
