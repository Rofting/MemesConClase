package org.svalero.memesconclase.domain.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ReactionPatchDto {
    @Pattern(regexp = "LIKE|LOVE|HAHA|WOW|SAD|ANGRY", message = "El tipo de reacción no es válido.")
    private String type;
    private Boolean active;
    private Integer weight;
    private Double confidence;
}
