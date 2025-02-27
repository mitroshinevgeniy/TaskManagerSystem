package ru.mitroshin.taskmanagersystem.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.mitroshin.taskmanagersystem.model.Task;
import ru.mitroshin.taskmanagersystem.rest.dto.TaskFilter;

public interface TaskSpecification {

    static Specification<Task> withFilter(TaskFilter taskFilter) {
        return Specification.where(byAuthorId(taskFilter.getAuthorId()))
                .and(byAssigneeId(taskFilter.getAssigneeId()))
                .and(bySearchQuery(taskFilter.getSearchQuery()));
    }

    static Specification<Task> byAuthorId(Long authorId) {
        return (root, query, criteriaBuilder) -> {
            if (authorId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("author").get("id"), authorId);
        };
    }

    static Specification<Task> byAssigneeId(Long assigneeId) {
        return (root, query, criteriaBuilder) -> {
            if (assigneeId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("assignee").get("id"), assigneeId);
        };
    }

    static Specification<Task> bySearchQuery(String searchQuery) {
        return (root, query, criteriaBuilder) -> {
            if (searchQuery == null || searchQuery.isBlank()) {
                return null;
            }

            String searchPattern = "%" + searchQuery.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchPattern)
            );
        };
    }
}