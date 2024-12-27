package com.ancore.ancoregaming.product.services.requirements;

import com.ancore.ancoregaming.product.dtos.RequirementsDTO;
import com.ancore.ancoregaming.product.model.Requirements;
import java.util.List;

public interface IRequirementsService {

  Requirements createRequirement(RequirementsDTO requirement);

  List<Requirements> bulkCreateRequirements(List<RequirementsDTO> requirements);

  List<Requirements> findAllRequirements();

  Requirements findRequirement(Long requirementsId);

  Requirements updateRequirement(Long requirementId, Requirements requirements);

  Requirements destroyRequirement(Long requirementsId);
}
