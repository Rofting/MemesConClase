package org.svalero.memesconclase.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FriendshipOutDto {
    private Long id;
    private String userName;
    private String friendName;
    private String status;
    private LocalDateTime createdAt;
}