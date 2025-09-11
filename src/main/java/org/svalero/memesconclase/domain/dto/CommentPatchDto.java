package org.svalero.memesconclase.domain.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentPatchDto {

    @Size(max = 500, message = "El contenido no puede exceder 500 caracteres.")
    private String content;
    private Integer likes;
    private Double sentimentScore;
}
