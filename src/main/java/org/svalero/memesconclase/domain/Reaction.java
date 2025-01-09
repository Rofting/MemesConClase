package org.svalero.memesconclase.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "reactions")
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "publication_id", nullable = false)
    private Publication publication;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReactionType type;

    public enum ReactionType {
        LIKE, LOVE, HAHA, WOW, SAD, ANGRY
    }
}
