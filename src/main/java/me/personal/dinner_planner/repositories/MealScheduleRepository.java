package me.personal.dinner_planner.repositories;

import me.personal.dinner_planner.models.MealSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MealScheduleRepository extends JpaRepository<MealSchedule, UUID> {
    List<MealSchedule> findByUserId(UUID userId);
}
