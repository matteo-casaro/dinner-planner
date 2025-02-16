package me.personal.dinner_planner.models;

import jakarta.persistence.*;
import lombok.Data;
import me.personal.dinner_planner.enums.DayOfWeek;
import me.personal.dinner_planner.enums.MealType;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "meal_schedule")
public class MealSchedule {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Enumerated(EnumType.STRING)
    private DayOfWeek day;

    @Enumerated(EnumType.STRING)
    private MealType mealType;

    private LocalDateTime createdAt;
}
