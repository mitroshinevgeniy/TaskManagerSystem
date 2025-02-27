package ru.mitroshin.taskmanagersystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.mitroshin.taskmanagersystem.model.Priority;
import ru.mitroshin.taskmanagersystem.model.Status;
import ru.mitroshin.taskmanagersystem.model.Task;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    Page<Task> findByStatus(Status status, Pageable pageable);

    Page<Task> findByPriority(Priority priority, Pageable pageable);

    Page<Task> findByAuthorId(Long authorId, Pageable pageable);

    Page<Task> findByAssigneeId(Long assigneeId, Pageable pageable);

    Page<Task> findByStatusAndPriority(Status status, Priority priority, Pageable pageable);

    Page<Task> findByStatusAndPriorityAndAuthorId(Status status, Priority priority, Long authorId, Pageable pageable);

    Page<Task> findByStatusAndPriorityAndAssigneeId(Status status, Priority priority, Long assigneeId, Pageable pageable);
}