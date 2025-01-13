package org.svalero.memesconclase.domain.dto;

import lombok.Data;

@Data
public class ReactionOutDto {
    private Long id;
    private String userName;
    private Long publicationId;
    private String type;
}
