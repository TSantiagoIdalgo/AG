package com.ancore.ancoregaming.product.services.platform;

import com.ancore.ancoregaming.product.model.Platform;
import java.util.List;

public interface IPlatformService {

  public Platform createPlatform(Platform platform);

  public List<Platform> bulkCreatePlatforms(List<Platform> platforms);

  public List<Platform> findAllPlatforms();

  public Platform findPlatform(String name);

  public Platform updatePlatform(Platform platform);

  public Platform destroyPlatform(String name);
}
