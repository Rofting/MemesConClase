package org.svalero.memesconclase.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PublicationOutDto {
    private Long id;
    private String content;
    private String imageUrl;
    private String typeContent;
    private String privacy;
    private LocalDateTime publicationDate;
    private String userName;
}
