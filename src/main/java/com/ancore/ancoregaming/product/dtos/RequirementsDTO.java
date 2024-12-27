package com.ancore.ancoregaming.product.dtos;

import com.ancore.ancoregaming.product.model.Requirements;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequirementsDTO {

  private Long id;
  @NotNull(message = "System operative is required")
  private String OS;
  @NotNull(message = "Memory is required")
  private double memory;
  @NotNull(message = "Graphic card name is required")
  private String graphics;
  @NotNull(message = "DirectX version is required")
  private double Directx_v;
  @NotNull(message = "Storage is required")
  private double storage;
  @NotNull(message = "Processor name are required")
  private String processor;
  @NotNull(message = "Type of requirement is required")
  private Requirements.RequirementType type;

  public RequirementsDTO() {
  }

  public RequirementsDTO(String OS, double memory, String graphics, double Directx_v, double storage, String processor, Requirements.RequirementType type) {
    this.OS = OS;
    this.memory = memory;
    this.graphics = graphics;
    this.Directx_v = Directx_v;
    this.storage = storage;
    this.processor = processor;
    this.type = type;
  }

  @Override
  public String toString() {
    return "RequirementsDTO{" + "OS=" + OS + ", memory=" + memory + ", graphics=" + graphics + ", Directx_v=" + Directx_v + ", storage=" + storage + ", processor=" + processor + ", type=" + type + '}';
  }

}
