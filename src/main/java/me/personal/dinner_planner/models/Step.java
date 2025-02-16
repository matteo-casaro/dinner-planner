package me.personal.dinner_planner.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "steps")
public class Step {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    private Integer orderNumber;
    private String title;
    private String description;
    private LocalDateTime createdAt;
}
