package org.svalero.memesconclase.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReactionOutDto {
    private Long id;
    private String userName;
    private Long publicationId;
    private String type;

    private LocalDateTime createdAt;
    private boolean active;
    private int weight;
    private double confidence;
}
