package org.svalero.memesconclase.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class FriendshipInDto {

    @NotNull(message = "El ID del usuario es obligatorio.")
    private Long userId;

    @NotNull(message = "El ID del amigo es obligatorio.")
    private Long friendId;

    @NotBlank(message = "El estado de la amistad es obligatorio.")
    @Pattern(regexp = "PENDING|ACCEPTED|BLOCKED", message = "El estado no es válido.")
    private String status;
}