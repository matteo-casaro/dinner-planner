package me.personal.dinner_planner.models;

import jakarta.persistence.*;
import lombok.Data;
import me.personal.dinner_planner.enums.IngredientType;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "ingredients")
public class Ingredient {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String name;

    @Enumerated(EnumType.STRING)
    private IngredientType type;

    private String unit;
    private LocalDateTime createdAt;
}
