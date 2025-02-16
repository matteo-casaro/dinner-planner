package me.personal.dinner_planner.repositories;

import me.personal.dinner_planner.models.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StepRepository extends JpaRepository<Step, UUID> {
    List<Step> findByRecipeIdOrderByOrderNumber(UUID recipeId);
}
