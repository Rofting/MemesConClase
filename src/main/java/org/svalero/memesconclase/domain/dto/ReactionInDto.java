package org.svalero.memesconclase.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ReactionInDto {

    @NotNull(message = "El ID del usuario es obligatorio.")
    private Long userId;

    @NotNull(message = "El ID de la publicación es obligatorio.")
    private Long publicationId;

    @NotBlank(message = "El tipo de reacción es obligatorio.")
    @Pattern(regexp = "LIKE|LOVE|HAHA|WOW|SAD|ANGRY", message = "El tipo de reacción no es válido.")
    private String type;
}
