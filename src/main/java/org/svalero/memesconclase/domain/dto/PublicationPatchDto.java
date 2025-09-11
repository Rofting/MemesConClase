package org.svalero.memesconclase.domain.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PublicationPatchDto {

    @Size(max = 500, message = "El contenido no puede exceder 500 caracteres.")
    private String content;

    private String imageUrl;

    @Pattern(regexp = "TEXT|IMAGE|VIDEO", message = "El tipo debe ser TEXT, IMAGE o VIDEO.")
    private String typeContent;

    @Pattern(regexp = "PUBLIC|PRIVATE", message = "La privacidad debe ser PUBLIC o PRIVATE.")
    private String privacy;

    @DecimalMin(value = "-90.0", message = "Latitud debe estar entre -90 y 90")
    @DecimalMax(value = "90.0",  message = "Latitud debe estar entre -90 y 90")
    private Double latitude;

    @DecimalMin(value = "-180.0", message = "Longitud debe estar entre -180 y 180")
    @DecimalMax(value = "180.0",  message = "Longitud debe estar entre -180 y 180")
    private Double longitude;

    private Integer views;
    private Boolean featured;
}
