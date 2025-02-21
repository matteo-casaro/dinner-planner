package me.personal.dinner_planner.models;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Entity
@Table(name = "steps", uniqueConstraints = @UniqueConstraint(columnNames = {"recipe_id", "order_number"}))
public class Step {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Column(name = "order_number", nullable = false)
    private Integer orderNumber;
    private String title;
    private String description;
    private LocalDateTime createdAt;
}
