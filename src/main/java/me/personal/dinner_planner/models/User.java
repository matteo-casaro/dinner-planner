package me.personal.dinner_planner.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    private UUID id;
    private String email;
    private String password;
    private String tempToken;
    private LocalDateTime tempTokenExp;
    private LocalDateTime createdAt;
}
