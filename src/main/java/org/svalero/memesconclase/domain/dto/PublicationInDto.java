package org.svalero.memesconclase.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class PublicationInDto {

    @NotNull(message = "El ID del usuario es obligatorio.")
    private Long userId;

    @NotBlank(message = "El contenido no puede estar vacío.")
    @Size(max = 500, message = "El contenido no puede exceder 500 caracteres.")
    private String content;

    private String imageUrl;

    @NotBlank(message = "El tipo de contenido es obligatorio.")
    @Pattern(regexp = "TEXT|IMAGE|VIDEO", message = "El tipo de contenido debe ser TEXT, IMAGE o VIDEO.")
    private String typeContent;

    @NotBlank(message = "La privacidad es obligatoria.")
    @Pattern(regexp = "PUBLIC|PRIVATE", message = "La privacidad debe ser PUBLIC o PRIVATE.")
    private String privacy;
}
