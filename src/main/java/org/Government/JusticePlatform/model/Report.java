package org.Government.JusticePlatform.model;

import lombok.*;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reports") // Specify table name explicitly
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reportId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Replaces raw Integer userId

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article; // Nullable in case it's a comment report

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment; // Nullable in case it's an article report

    @Column(nullable = false)
    private String reason;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING; // Default status when created

    @PrePersist
    protected void onCreate() {
        Date now = new Date();
        createdAt = now;
        updatedAt = now; // Initialize updatedAt when the report is created
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date(); // Update timestamp when the report is modified
    }
}
