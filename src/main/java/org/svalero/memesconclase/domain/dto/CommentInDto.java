package org.svalero.memesconclase.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentInDto {

    @NotNull(message = "El ID del usuario es obligatorio.")
    private Long userId;

    @NotNull(message = "El ID de la publicación es obligatorio.")
    private Long publicationId;

    @NotBlank(message = "El contenido del comentario no puede estar vacío.")
    @Size(max = 500, message = "El contenido no puede exceder 500 caracteres.")
    private String content;
}