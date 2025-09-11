package org.svalero.memesconclase.domain.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FriendshipPatchDto {

    @Pattern(regexp = "PENDING|ACCEPTED|BLOCKED", message = "El estado no es válido.")
    private String status;

    @Size(max = 255, message = "Las notas no pueden superar 255 caracteres.")
    private String notes;

    private Boolean favorite;
    private Integer closenessScore;
}
