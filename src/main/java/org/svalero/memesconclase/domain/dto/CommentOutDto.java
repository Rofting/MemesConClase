package org.svalero.memesconclase.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentOutDto {
    private Long id;
    private String userName;
    private Long publicationId;
    private String content;
    private LocalDateTime createdAt;
}