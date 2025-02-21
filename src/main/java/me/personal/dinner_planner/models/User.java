package me.personal.dinner_planner.models;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    private String email;
    private String password;
    private String tempToken;
    private LocalDateTime tempTokenExp;
    private LocalDateTime createdAt;
}
