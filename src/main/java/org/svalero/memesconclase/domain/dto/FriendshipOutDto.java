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
}