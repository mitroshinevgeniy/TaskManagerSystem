package ru.mitroshin.taskmanagersystem.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "full_name", nullable = false, length = 200)
    private String fullName;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<Task> authoredTasks = new ArrayList<>();

    @OneToMany(mappedBy = "assignee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<Task> assignedTasks = new ArrayList<>();
}