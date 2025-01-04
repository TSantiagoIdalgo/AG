package com.ancore.ancoregaming.product.services.requirements;

import com.ancore.ancoregaming.product.dtos.RequirementsDTO;
import com.ancore.ancoregaming.product.model.Requirements;
import com.ancore.ancoregaming.product.repositories.IRequirementsRepository;
import jakarta.persistence.EntityNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequirementsService implements IRequirementsService {

  private final IRequirementsRepository requirementsRepository;
  
  @Autowired
  public RequirementsService(IRequirementsRepository requirementsRepository) {
    this.requirementsRepository = requirementsRepository;
  }
  
  @Override
  public Requirements createRequirement(RequirementsDTO requirement) {
    if (requirement.getId() != null) {
      Optional<Requirements> requirementFound = this.requirementsRepository.findById(requirement.getId());
      if (requirementFound.isPresent()) {
        return requirementFound.get();
      }
    }
    Requirements newRequirement = new Requirements.Builder()
            .setType(requirement.getType())
            .setStorage(requirement.getStorage())
            .setOS(requirement.getOs())
            .setMemory(requirement.getMemory())
            .setGraphics(requirement.getGraphics())
            .setDirectx_v(requirement.getDirectx_v())
            .setProcessor(requirement.getProcessor())
            .build();
    return this.requirementsRepository.save(newRequirement);
  }

  @Override
  public List<Requirements> bulkCreateRequirements(List<RequirementsDTO> requirements) {
    return requirements
            .stream()
            .map(this::createRequirement)
            .collect(Collectors.toList());
  }

  @Override
  public List<Requirements> findAllRequirements() {
    List<Requirements> requirements = this.requirementsRepository.findAll();

    if (requirements.isEmpty()) {
      throw new EntityNotFoundException("Requirements not found");
    }

    return requirements;
  }

  @Override
  public Requirements findRequirement(Long requirementsId) {
    Optional<Requirements> requirement = this.requirementsRepository.findById(requirementsId);
    if (requirement.isEmpty()) {
      throw new EntityNotFoundException("Requirement not found");
    }

    return requirement.get();
  }

  @Override
  public Requirements updateRequirement(Long requirementId, Requirements requirementUpdate) {
    Requirements requirement = this.findRequirement(requirementId);

    for (Method method : requirementUpdate.getClass().getMethods()) {
      if (method.getName().startsWith("get") && method.getReturnType().equals(Optional.class)) {
        try {
          Optional<?> value = (Optional<?>) method.invoke(requirementUpdate);
          if (value.isPresent()) {
            value.ifPresent(val -> setRequirementField(requirement, method.getName().substring(3), val));
          }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
          throw new RuntimeException("Error actualizando campos del producto: " + e.getMessage());
        }
      }
    }

    return this.requirementsRepository.save(requirement);
  }

  @Override
  public Requirements destroyRequirement(Long requirementsId) {
    Requirements requirement = this.findRequirement(requirementsId);
    this.requirementsRepository.delete(requirement);

    return requirement;
  }

  private void setRequirementField(Requirements requirement, String fieldName, Object value) {
    try {
      if (value instanceof List || value == null) {
        return;
      }
      Method setter = requirement.getClass().getMethod("set" + fieldName, value.getClass());
      setter.invoke(requirement, value);
    } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
      throw new RuntimeException("Error asignando el campo " + fieldName + "  " + e.getMessage());
    }
  }
}
