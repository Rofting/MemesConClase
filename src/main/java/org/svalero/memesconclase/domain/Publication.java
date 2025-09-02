package org.svalero.memesconclase.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table(name = "publications")
@Entity(name = "Publication")
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_content")
    private ContentType typeContent;

    @Column(name = "publication_date", updatable = false)
    private LocalDateTime publicationDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Privacy privacy = Privacy.PUBLIC;

    // ——— NUEVOS CAMPOS ———
    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    public enum ContentType {
        IMAGE, VIDEO, TEXT
    }

    public enum Privacy {
        PUBLIC, PRIVATE
    }
}
