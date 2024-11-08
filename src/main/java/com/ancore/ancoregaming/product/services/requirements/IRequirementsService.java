package com.ancore.ancoregaming.product.services.requirements;

import com.ancore.ancoregaming.product.model.Requirements;
import java.util.List;

public interface IRequirementsService {

  public Requirements createRequirement(Requirements requirement);

  public List<Requirements> bulkCreateRequirements(List<Requirements> requirements);

  public List<Requirements> findAllRequirements();

  public Requirements findRequirement(Long requirementsId);

  public Requirements updateRequirement(Long requirementId, Requirements requirements);

  public Requirements destroyRequirement(Long requirementsId);
}
