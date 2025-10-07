package com.taskmanager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.taskmanager.exception.InvalidDateException;
import com.taskmanager.exception.TaskNotFoundException;
import com.taskmanager.model.Category;
import com.taskmanager.model.Priority;
import com.taskmanager.model.Status;
import com.taskmanager.model.Task;
import com.taskmanager.repository.CategoryRepository;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.service.CategoryService;
import com.taskmanager.service.NotificationService;
import com.taskmanager.service.ReportService;
import com.taskmanager.service.TaskService;
import com.taskmanager.util.DateValidator;
import com.taskmanager.util.TaskValidator;

public class App {

	private final Scanner scanner;
	private final TaskService taskService;
	private final CategoryService categoryService;
	private final NotificationService notificationService;
	private final ReportService reportService;

	public App() {
		this.scanner = new Scanner(System.in);

		TaskRepository taskRepository = new TaskRepository();
		CategoryRepository categoryRepository = new CategoryRepository();
		TaskValidator taskValidator = new TaskValidator();
		DateValidator dateValidator = new DateValidator();

		taskService = new TaskService(taskRepository, taskValidator, dateValidator);
		categoryService = new CategoryService(categoryRepository);
		reportService = new ReportService(taskRepository);
		notificationService = new NotificationService(taskService);

	}

	public static void main(String[] args) {
		App app = new App();
		app.start();
	}

	public void start() {
		boolean running = true;
		while (running) {
			displayMenu();

			try {
				System.out.print("Enter your choice: ");
				int choice = Integer.parseInt(scanner.nextLine());

				switch (choice) {
				case 1:
					createTask();
					break;
				case 2:
					updateTask();
					break;
				case 3:
					viewTaskById();
					break;
				case 4:
					viewTasksByStatus();
					break;
				case 5:
					viewTasksByPriority();
					break;
				case 6:
					viewTasksByCategory();
					break;
				case 7:
					updateTaskStatus();
					break;
				case 8:
					viewOverdueTask();
					break;
				case 9:
					deleteTask();
					break;
				case 10:
					createCategory();
					break;
				case 11:
					listCategories();
					break;
				case 12:
					showAllTasks();
					break;
				case 13:
					generateReports();
				case 0:
					running = false;
					System.out.println("Goodbye!");
					break;

				default:
					System.out.println("Invalid choice. Please Try again.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid number.");
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}
		}
		scanner.close();
	}

	private void displayMenu() {
		System.out.println("\n--- Personal Task Manager ---");
		System.out.println("1. Create New Task");
		System.out.println("2. Update Task");
		System.out.println("3. View Task by ID");
		System.out.println("4. List Tasks by Status");
		System.out.println("5. List Tasks by Priority");
		System.out.println("6. List Tasks by Category");
		System.out.println("7. Update Task status");
		System.out.println("8. View Overdue Tasks");
		System.out.println("9. Delete Task");
		System.out.println("10. Create Category");
		System.out.println("11. View All Categories");
		System.out.println("12. View All Tasks");
		System.out.println("13. Generate Report");
		System.out.println("0. Exit");
		System.out.print("Enter choice: ");
	}

	private void displayTasks(List<Task> tasks) {
		if (tasks.isEmpty()) {
			System.out.println("No tasks found.");
			return;
		}

		for (Task task : tasks) {
			System.out.println("\nID: " + task.getTaskId());
			System.out.println("Title: " + task.getTitle());
			System.out
					.println("Due Date: " + task.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
			System.out.println("Status: " + task.getStatus());
			System.out.println("Priority: " + task.getPriority());
			System.out.println("Category: " + task.getCategory().getName());
		}
	}

	private Priority getPriorityChoice() {
		System.out.println("Priority: 1-HIGH, 2-MEDIUM, 3-LOW");
		int choice = Integer.parseInt(scanner.nextLine());
		switch (choice) {
		case 1:
			return Priority.HIGH;
		case 2:
			return Priority.MEDIUM;
		case 3:
			return Priority.LOW;
		default:
			return Priority.MEDIUM;
		}
	}

	private Status getStatusChoice() {
		System.out.println("Status: 1-PENDING, 2-IN_PROGRESS, 3-COMPLETED");
		int choice = Integer.parseInt(scanner.nextLine());
		switch (choice) {
		case 1:
			return Status.PENDING;
		case 2:
			return Status.IN_PROGRESS;
		case 3:
			return Status.COMPLETED;
		default:
			return Status.PENDING;
		}
	}

	private void createTask() throws Exception {

		System.out.println("\n=== Create New Task ===");

		System.out.print("Enter title: ");
		String title = scanner.nextLine();

		System.out.print("Enter description: ");
		String description = scanner.nextLine();

		LocalDateTime dueDate = null;
		boolean validDate = false;

		while (!validDate) {
			try {
				System.out.print("Enter due date (dd/MM/yyyy or dd-MM-yyyy) : ");
				String dueDateString = scanner.nextLine();
				dueDate = DateValidator.convertToDate(dueDateString);

				if (dueDate.isBefore(LocalDateTime.now())) {
					throw new InvalidDateException("Due date must be in the future");
				}
				validDate = true;
			} catch (InvalidDateException e) {
				System.out.println("Error: " + e.getMessage());
				// Continue loop to ask for date again
			}
		}

		Priority priority = getPriorityChoice();
		Category category = getOrCreateDefaultCategory();

		Task task = new Task(title, description, dueDate, priority, category

		);

		taskService.createTask(task);
		System.out.println("Task created successfully! ID: " + task.getTaskId());
		System.out.println("Task created successfully!");
	}

	private void updateTask() throws Exception {

		System.out.println("\n=== Update Task ===");

		System.out.print("Enter task ID to update: ");
		String taskId = scanner.nextLine();

		Task existingTask = taskService.getTaskById(taskId);

		Task updatedTask = new Task(existingTask.getTitle(), existingTask.getDescription(), existingTask.getDueDate(),
				existingTask.getPriority(), existingTask.getCategory());

		// Get updated values
		System.out.print("Enter new title (press Enter to keep current: " + existingTask.getTitle() + "): ");
		String title = scanner.nextLine();
		if (!title.trim().isEmpty()) {
			updatedTask.setTitle(title);
		}

		System.out
				.print("Enter new description (press Enter to keep current: " + existingTask.getDescription() + "): ");
		String description = scanner.nextLine();
		if (!description.trim().isEmpty()) {
			updatedTask.setDescription(description);
		}

		System.out.print("Enter new due date (yyyy-MM-dd HH:mm) (press Enter to keep current: "
				+ existingTask.getDueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "): ");
		String dateStr = scanner.nextLine();
		if (!dateStr.trim().isEmpty()) {
			LocalDateTime dueDate = DateValidator.convertToDate(dateStr);
			updatedTask.setDueDate(dueDate);
		}

		System.out.println("Update priority? (y/n) Current: " + existingTask.getPriority());
		if (scanner.nextLine().equalsIgnoreCase("y")) {
			updatedTask.setPriority(getPriorityChoice());
		}

		System.out.println("Update status? (y/n) Current: " + existingTask.getStatus());
		if (scanner.nextLine().equalsIgnoreCase("y")) {
			updatedTask.setStatus(getStatusChoice());
		}

		try {
			Task updated = taskService.updateTask(taskId, updatedTask);
			System.out.println("Task updated successfully!");
			List<Task> singleTaskList = new ArrayList<>();
			singleTaskList.add(updated);
			displayTasks(singleTaskList);

		} catch (TaskNotFoundException e) {
			System.out.println("Error: Task not found - " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Error updating task: " + e.getMessage());
		}
	}

	private void deleteTask() throws TaskNotFoundException {
		System.out.println("\n=== Delete Task ===");
		System.out.print("Enter task ID: ");
		String taskId = scanner.nextLine();

		Task task = taskService.getTaskById(taskId);
		System.out.println("Deleting task: " + task.getTitle());

		System.out.print("Are you sure? (y/n): ");
		if (scanner.nextLine().equalsIgnoreCase("y")) {
			taskService.deleteTask(taskId);
			System.out.println("Task deleted successfully!");
		}
	}

	private void showAllTasks() {
		try {

			System.out.println("\n=== All Tasks ===");
			List<Task> tasks = taskService.getAllTasks();

			if (tasks.isEmpty()) {
				System.out.println("No tasks found.");
				return;
			}

			displayTasks(tasks);

		} catch (Exception e) {
			System.out.println("Error retrieving all tasks: " + e.getMessage());
		}

	}

	private Category getOrCreateDefaultCategory() {
		List<Category> categories = categoryService.getAllCategories();
		if (categories.isEmpty()) {
			Category defaultCategory = new Category("Default", "Default Category");
			return categoryService.createCategory(defaultCategory);
		}
		return categories.get(0);
	}

	private void viewTaskById() throws Exception {
		System.out.print("Enter task ID: ");
		String taskId = scanner.nextLine();
		try {
			Task task = taskService.getTaskById(taskId);
			List<Task> singleTask = new ArrayList<>();
			singleTask.add(task);
			displayTasks(singleTask);
		} catch (TaskNotFoundException e) {
			System.out.println("Error: Task not found - " + e.getMessage());
		}
	}

	private void viewTasksByStatus() {
		System.out.print("Enter status (PENDING, IN_PROGRESS, COMPLETED): ");
		Status status = Status.valueOf(scanner.nextLine().toUpperCase());
		List<Task> tasks = taskService.getTasksByStatus(status);
		displayTasks(tasks);
	}

	private void viewTasksByPriority() {
		System.out.print("Enter priority (HIGH, MEDIUM, LOW): ");
		Priority priority = Priority.valueOf(scanner.nextLine().toUpperCase());
		List<Task> tasks = taskService.getTasksByPriority(priority);
		displayTasks(tasks);
	}

	private void viewTasksByCategory() {
		System.out.print("Enter category ID: ");
		String categoryId = scanner.nextLine();
		List<Task> tasks = taskService.getTasksByCategory(categoryId);
		displayTasks(tasks);
	}

	private void updateTaskStatus() {
		System.out.print("Enter task ID: ");
		String taskId = scanner.nextLine();

		System.out.println("Select new status (1-PENDING, 2-IN_PROGRESS, 3-COMPLETED): ");
		int statusChoice = scanner.nextInt();
		Status newStatus = Status.PENDING;
		switch (statusChoice) {
		case 1:
			newStatus = Status.PENDING;
			break;
		case 2:
			newStatus = Status.IN_PROGRESS;
			break;
		case 3:
			newStatus = Status.COMPLETED;
			break;
		}

		try {
			Task updatedTaskStatus = taskService.updateTaskStatus(taskId, newStatus);
			System.out.println("Task updated successfully!");

			List<Task> taskList = new ArrayList<>();
			taskList.add(updatedTaskStatus);
			displayTasks(taskList);

		} catch (TaskNotFoundException e) {
			System.out.println("Task not found: " + e.getMessage());
		}
	}

	private void viewOverdueTask() {

		try {
			System.out.println("\n=== Overdue Tasks ===");
			notificationService.checkOverdueTasks();
			List<Task> overdueTasks = taskService.getOverdueTasks();

			if (overdueTasks.isEmpty()) {
				System.out.println("No overdue tasks found.");
				return;
			}

			notificationService.checkOverdueTasks();
			System.out.println("\nDetailed Overdue Task Information:");
			displayTasks(overdueTasks);

		} catch (Exception e) {
			System.out.println("Error retrieving overdue tasks: " + e.getMessage());
		}
	}

	private void createCategory() {
		System.out.print("Enter category name: ");
		String name = scanner.nextLine();
		System.out.print("Enter description: ");
		String description = scanner.nextLine();

		Category category = new Category(name, description);
		categoryService.createCategory(category);
		System.out.println("Category created successfully! ID: " + category.getCategoryId());
	}

	private void listCategories() {
		List<Category> categories = categoryService.getAllCategories();
		categories.forEach(System.out::println);
	}

	private void generateReports() {
		System.out.println("\n=== Task Management Report ===");

		String report = reportService.generateReport();
		System.out.println(report);

	}

}
