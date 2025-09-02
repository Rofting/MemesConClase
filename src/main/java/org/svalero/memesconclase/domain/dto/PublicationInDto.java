package org.svalero.memesconclase.domain.dto;

import jakarta.validation.constraints.*;

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
    @Pattern(regexp = "TEXT|IMAGE|VIDEO", message = "El tipo debe ser TEXT, IMAGE o VIDEO.")
    private String typeContent;

    @NotBlank(message = "La privacidad es obligatoria.")
    @Pattern(regexp = "PUBLIC|PRIVATE", message = "La privacidad debe ser PUBLIC o PRIVATE.")
    private String privacy;

    // ——— NUEVOS CAMPOS ———
    @NotNull(message = "La latitud es obligatoria.")
    @DecimalMin(value = "-90.0", message = "Latitud debe estar entre -90 y 90")
    @DecimalMax(value = "90.0",  message = "Latitud debe estar entre -90 y 90")
    private Double latitude;

    @NotNull(message = "La longitud es obligatoria.")
    @DecimalMin(value = "-180.0", message = "Longitud debe estar entre -180 y 180")
    @DecimalMax(value = "180.0",  message = "Longitud debe estar entre -180 y 180")
    private Double longitude;
}
