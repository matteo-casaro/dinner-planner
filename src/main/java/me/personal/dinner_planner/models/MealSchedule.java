package me.personal.dinner_planner.models;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import me.personal.dinner_planner.enums.DayOfWeek;
import me.personal.dinner_planner.enums.MealType;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Entity
@Table(name = "meal_schedule", uniqueConstraints = @UniqueConstraint(columnNames = {"selected_day", "meal_type", "user_id"}))
public class MealSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Enumerated(EnumType.STRING)
    private DayOfWeek selectedDay;

    @Enumerated(EnumType.STRING)
    private MealType mealType;

    private LocalDateTime createdAt;
}
