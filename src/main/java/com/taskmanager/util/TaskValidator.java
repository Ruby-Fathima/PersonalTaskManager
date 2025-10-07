package com.taskmanager.util;

import com.taskmanager.exception.TaskValidationException;
import com.taskmanager.model.Category;
import com.taskmanager.model.Priority;
import com.taskmanager.model.Task;

public class TaskValidator {
    private static final int MAX_TITLE_LENGTH = 100;
    private static final int MAX_DESCRIPTION_LENGTH = 500;

    public void validateTask(Task task) throws TaskValidationException {
        validateTitle(task.getTitle());
        validateDescription(task.getDescription());
        validateCategory(task.getCategory());
        validatePriority(task.getPriority());
    }

    private void validateTitle(String title) throws TaskValidationException {
        if (title == null || title.trim().isEmpty()) {
            throw new TaskValidationException("Task title cannot be empty");
        }
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new TaskValidationException("Task title cannot exceed " + MAX_TITLE_LENGTH + " characters");
        }
    }

    private void validateDescription(String description) throws TaskValidationException {
        if (description != null && description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new TaskValidationException("Task description cannot exceed " + MAX_DESCRIPTION_LENGTH + " characters");
        }
    }

    private void validateCategory(Category category) throws TaskValidationException {
        if (category == null) {
            throw new TaskValidationException("Task category cannot be null");
        }
        if (category.getCategoryId() == null || category.getCategoryId().trim().isEmpty()) {
            throw new TaskValidationException("Category ID cannot be empty");
        }
    }

    private void validatePriority(Priority priority) throws TaskValidationException {
        if (priority == null) {
            throw new TaskValidationException("Task priority cannot be null");
        }
    }
}


