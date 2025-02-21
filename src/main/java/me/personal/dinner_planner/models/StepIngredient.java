package me.personal.dinner_planner.models;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Entity
@Table(name = "step_ingredients", uniqueConstraints = @UniqueConstraint(columnNames = {"step_id", "ingredient_id"}))
public class StepIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "step_id", nullable = false)
    private Step step;

    @ManyToOne
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    private Double quantity;
    private LocalDateTime createdAt;
}