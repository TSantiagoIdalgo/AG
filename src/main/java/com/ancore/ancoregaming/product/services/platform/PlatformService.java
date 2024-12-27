package com.ancore.ancoregaming.product.services.platform;

import com.ancore.ancoregaming.product.model.Platform;
import com.ancore.ancoregaming.product.repositories.IPlatformRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlatformService implements IPlatformService {

  private final IPlatformRepository platformRepository;
  
  @Autowired
  public PlatformService(IPlatformRepository platformRepository) {
    this.platformRepository = platformRepository;
  }
  
  @Override
  public Platform createPlatform(Platform platform) {
    Optional<Platform> platformFound = this.platformRepository.findByNameAndPlatform(platform.getName(), platform.getPlatform());
    if (platformFound.isPresent()) {
      return platformFound.get();
    }

    Platform newPlatform = new Platform(platform.getName(), platform.getPlatform(), platform.isDisabled());
    this.platformRepository.save(newPlatform);
    return newPlatform;
  }

  @Override
  public List<Platform> bulkCreatePlatforms(List<Platform> platforms) {
    return platforms
            .stream()
            .map(this::createPlatform)
            .collect(Collectors.toList());
  }

  @Override
  public List<Platform> findAllPlatforms() {
    return this.platformRepository.findAll();
  }

  @Override
  public Platform findPlatform(String name) {
    return this.platformRepository.findById(name).orElseThrow(() -> new EntityNotFoundException("Platform not found"));
  }

  @Override
  public Platform updatePlatform(Platform platform) {
    Platform platformFound = this.findPlatform(platform.getName());
    platformFound.setName(platform.getName());
    platformFound.setDisabled(platform.isDisabled());
    this.platformRepository.save(platformFound);

    return platformFound;
  }

  @Override
  public Platform destroyPlatform(String name) {
    Platform platform = this.findPlatform(name);
    this.platformRepository.delete(platform);

    return platform;
  }

}
