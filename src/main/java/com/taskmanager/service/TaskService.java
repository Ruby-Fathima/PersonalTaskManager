package com.taskmanager.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.taskmanager.exception.DuplicateTaskException;
import com.taskmanager.exception.InvalidDateException;
import com.taskmanager.exception.TaskNotFoundException;
import com.taskmanager.exception.TaskValidationException;
import com.taskmanager.model.Priority;
import com.taskmanager.model.Task;
import com.taskmanager.model.Status;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.util.DateValidator;
import com.taskmanager.util.TaskValidator;

public class TaskService {
	private final TaskRepository taskRepository;
    private final TaskValidator taskValidator;
    private final DateValidator dateValidator;

    public TaskService(TaskRepository taskRepository, TaskValidator taskValidator, DateValidator dateValidator) {
        this.taskRepository = taskRepository;
        this.taskValidator = taskValidator;
        this.dateValidator = dateValidator;
    }

    public Task createTask(Task task) throws DuplicateTaskException, InvalidDateException, TaskValidationException {
        taskValidator.validateTask(task);
        dateValidator.validateFutureDate(task.getDueDate());
       
        if (isDuplicateTask(task)) {
            throw new DuplicateTaskException("This is duplicate task. Task is already available.");
        }
        return taskRepository.save(task);
    }

    public Task updateTask(String taskId, Task updatedTask) throws TaskNotFoundException {
        Task existingTask = getTaskById(taskId);
       
        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setDueDate(updatedTask.getDueDate());
        existingTask.setPriority(updatedTask.getPriority());
        existingTask.setCategory(updatedTask.getCategory());
        existingTask.setStatus(updatedTask.getStatus());
        existingTask.setLastModifiedDate(LocalDateTime.now());
       
        return taskRepository.save(existingTask);
    }
    
    public Task getTaskById(String taskId) throws TaskNotFoundException {
        return taskRepository.findById(taskId)
            .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + taskId));
    }
    
    public List<Task> getTasksByStatus(Status status) {
        return taskRepository.findAll().stream()
            .filter(task -> task.getStatus() == status)
            .collect(Collectors.toList());
    }

    public void deleteTask(String taskId) throws TaskNotFoundException {
        if (!taskRepository.exists(taskId)) {
            throw new TaskNotFoundException("Task not found with ID: " + taskId);
        }
        taskRepository.deleteById(taskId);
    }

    public List<Task> getTasksByCategory(String categoryId) {
        return taskRepository.findAll().stream()
            .filter(task -> task.getCategory().getCategoryId().equals(categoryId))
            .collect(Collectors.toList());
    }

    public List<Task> getOverdueTasks() {
        return taskRepository.findAll().stream()
            .filter(task -> task.getDueDate().isBefore(LocalDateTime.now())
                && task.getStatus() != Status.COMPLETED)
            .collect(Collectors.toList());
    }
    
    public List<Task> getTasksByPriority(Priority priority) {
        return taskRepository.findAll().stream()
            .filter(task -> task.getPriority() == priority)
            .collect(Collectors.toList());
    }

    private boolean isDuplicateTask(Task task) {
        return taskRepository.findAll().stream()
            .anyMatch(existingTask ->
                existingTask.getTitle().equals(task.getTitle()) &&
                existingTask.getDescription().equals(task.getDescription()) &&
                existingTask.getCategory().getCategoryId().equals(task.getCategory().getCategoryId())
            );
    }
    
    public Task updateTaskStatus(String taskId, Status newStatus) throws TaskNotFoundException {
        Task existingTask = getTaskById(taskId);
        
        existingTask.setStatus(newStatus);
        existingTask.setLastModifiedDate(LocalDateTime.now());
        
        return taskRepository.save(existingTask);
    }
    public List<Task> getAllTasks() {
    	return taskRepository.findAll();
    }
}