package org.Government.JusticePlatform.model;

import lombok.*;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "votes") // Specify table name explicitly
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer voteId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Replaces raw Integer userId

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article; // Replaces raw Integer articleId

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VoteType voteType;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}
