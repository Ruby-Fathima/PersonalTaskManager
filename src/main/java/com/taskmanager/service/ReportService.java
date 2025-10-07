package com.taskmanager.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.taskmanager.model.Priority;
import com.taskmanager.model.Status;
import com.taskmanager.model.Task;
import com.taskmanager.repository.TaskRepository;

public class ReportService {
	private final TaskRepository taskRepository;

	public ReportService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	public long tasksCountByStatus(Status status) {
		return taskRepository.findAll().stream().filter(task -> task.getStatus() == status).count();
	}

	public long tasksCountByPriority(Priority priority) {
		return taskRepository.findAll().stream().filter(task -> task.getPriority() == priority).count();
	}

	public Map<Status, Long> taskStatusReport() {
		return taskRepository.findAll().stream().collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));
	}

	public Map<Priority, Long> taskPriorityReport() {
		return taskRepository.findAll().stream()
				.collect(Collectors.groupingBy(Task::getPriority, Collectors.counting()));
	}

	public Map<String, Long> categoryReport() {
		return taskRepository.findAll().stream()
				.collect(Collectors.groupingBy(task -> task.getCategory().getName(), Collectors.counting()));
	}

	public long overdueTasksCount() {
		return taskRepository.findAll().stream()
				.filter(task -> task.getDueDate().isBefore(LocalDateTime.now()) && task.getStatus() != Status.COMPLETED)
				.count();
	}

	public String generateReport() {
		StringBuilder report = new StringBuilder();
		List<Task> allTasks = taskRepository.findAll();

		report.append("Task Management Report\n\n");

		report.append("Total Tasks: ").append(allTasks.size()).append("\n\n");

		report.append("Status Count:\n");
		taskStatusReport().forEach((status, count) -> report.append(status).append(": ").append(count).append("\n"));

		report.append("\nPriority Count:\n");
		taskPriorityReport()
				.forEach((priority, count) -> report.append(priority).append(": ").append(count).append("\n"));

		report.append("\nCategory Count:\n");
		categoryReport()
				.forEach((category, count) -> report.append(category).append(": ").append(count).append("\n"));

		report.append("\nOverdue Tasks: ").append(overdueTasksCount()).append("\n");

		return report.toString();
	}

	public List<Task> getAllTasks() {
		return taskRepository.findAll();
	}
}