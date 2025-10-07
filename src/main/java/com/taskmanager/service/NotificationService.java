package com.taskmanager.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.taskmanager.model.Task;

public class NotificationService {
	private final TaskService taskService;

	public NotificationService(TaskService taskService) {
		this.taskService = taskService;
	}

	public void checkAndNotify() {
		List<Task> allTasks = taskService.getAllTasks();
		System.out.println("Checking for overdue tasks...");

		for (Task task : allTasks) {
			if (task.getDueDate().isBefore(LocalDateTime.now())) {
				System.out.printf("Reminder! Task '%s' is overdue since %s.%n", task.getTitle(),
						task.getDueDate().format(DateTimeFormatter.ISO_LOCAL_DATE));

			}
		}
	}

	public void checkOverdueTasks() {
		List<Task> overdueTasks = taskService.getOverdueTasks();
		overdueTasks.forEach(task -> System.out
				.println("ALERT: Task '" + task.getTitle() + "' is overdue! (Due: " + task.getDueDate() + ")"));
	}
}
