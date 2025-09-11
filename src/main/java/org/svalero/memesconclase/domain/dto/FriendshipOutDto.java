package org.svalero.memesconclase.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FriendshipOutDto {
    private Long id;
    private UserOutDto user;
    private UserOutDto friend;
    private String status;
    private LocalDateTime createdAt;

    private int closenessScore;
    private String notes;
    private boolean favorite;
    private LocalDateTime updatedAt;
}
