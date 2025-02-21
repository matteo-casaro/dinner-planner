package me.personal.dinner_planner.repositories;

import me.personal.dinner_planner.models.StepIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StepIngredientRepository extends JpaRepository<StepIngredient, UUID> {
    List<StepIngredient> findByStepId(UUID stepId);
}
