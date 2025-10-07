package com.taskmanager.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.taskmanager.exception.DuplicateTaskException;
import com.taskmanager.exception.InvalidDateException;
import com.taskmanager.exception.TaskNotFoundException;
import com.taskmanager.exception.TaskValidationException;
import com.taskmanager.model.Category;
import com.taskmanager.model.Priority;
import com.taskmanager.model.Status;
import com.taskmanager.model.Task;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.util.DateValidator;
import com.taskmanager.util.TaskValidator;

public class TaskServiceTest {
	private TaskService taskService;
	private TaskRepository taskRepository;
	private TaskValidator taskValidator;
	private DateValidator dateValidator;
	private Category testCategory;
	private Task testTask;

	@BeforeMethod
	public void setUp() {
		taskRepository = new TaskRepository();
		taskValidator = new TaskValidator();
		dateValidator = new DateValidator();
		taskService = new TaskService(taskRepository, taskValidator, dateValidator);

		// Create test category
		testCategory = new Category("Work Projects", "Project should be complete");

		// Create test task
		testTask = new Task("Water Garden Plants", "Essential evening care for vegetables",
				LocalDateTime.now().plusDays(1), Priority.HIGH, testCategory);
	}

	@Test
	public void testCreateTask() throws Exception {
		Task createdTask = taskService.createTask(testTask);
		Assert.assertNotNull(createdTask.getTaskId());
		Assert.assertEquals(createdTask.getTitle(), "Water Garden Plants");
	}

	@Test(expectedExceptions = DuplicateTaskException.class)
	public void testDuplicateTask() throws Exception {
		taskService.createTask(testTask);
		taskService.createTask(testTask);
	}

	@Test(expectedExceptions = InvalidDateException.class)
	public void testCreateTaskUsingPastDate() throws Exception {
		testTask.setDueDate(LocalDateTime.now().minusDays(1));
		taskService.createTask(testTask);
	}

	@DataProvider(name = "taskPriorities")
	public Object[][] createTaskPriorities() {
		return new Object[][] { { Priority.HIGH }, { Priority.MEDIUM }, { Priority.LOW } };
	}

	@Test(dataProvider = "taskPriorities")
	public void testCreateTaskUsingPriorities(Priority priority) throws Exception {
		testTask.setPriority(priority);
		Task createdTask = taskService.createTask(testTask);
		Assert.assertEquals(createdTask.getPriority(), priority);
	}

	@Test
	public void testUpdateTask() throws Exception {
		Task createdTask = taskService.createTask(testTask);
		String taskId = createdTask.getTaskId();

		Task updatedTask = new Task("Read Project", "Understand Project before coding starts",
				LocalDateTime.now().plusDays(2), Priority.MEDIUM, testCategory);

		Task result = taskService.updateTask(taskId, updatedTask);
		Assert.assertEquals(result.getTitle(), "Read Project");
		Assert.assertEquals(result.getDescription(), "Understand Project before coding starts");
	}

	@Test
	public void testTaskById() throws Exception {
		Task createdTask = taskService.createTask(testTask);
		Task retrievedTask = taskService.getTaskById(createdTask.getTaskId());
		Assert.assertEquals(retrievedTask.getTaskId(), createdTask.getTaskId());
	}

	@Test(expectedExceptions = TaskNotFoundException.class)
	public void testGetNonExistentTask() throws Exception {
		taskService.getTaskById("non-existent-id");
	}

	@Test
	public void testTasksByStatus() throws Exception {
		testTask.setStatus(Status.PENDING);
		taskService.createTask(testTask);

		List<Task> pendingTasks = taskService.getTasksByStatus(Status.PENDING);
		Assert.assertEquals(pendingTasks.size(), 1);
		Assert.assertEquals(pendingTasks.get(0).getStatus(), Status.PENDING);
	}

	@Test
	public void testTasksByPriority() throws Exception {
		testTask.setPriority(Priority.HIGH);
		taskService.createTask(testTask);

		List<Task> highPriorityTasks = taskService.getTasksByPriority(Priority.HIGH);
		Assert.assertEquals(highPriorityTasks.size(), 1);
		Assert.assertEquals(highPriorityTasks.get(0).getPriority(), Priority.HIGH);
	}

	@Test
	public void testTasksByCategory() throws Exception {

		@SuppressWarnings("unused")
		Task createdTask = taskService.createTask(testTask);
		List<Task> categoryTasks = taskService.getTasksByCategory(testCategory.getCategoryId());
		Assert.assertEquals(categoryTasks.size(), 1);
		Assert.assertEquals(categoryTasks.get(0).getCategory().getCategoryId(), testCategory.getCategoryId());
	}

	@Test
	public void testOverdueTasks() throws Exception {

		Task overdueTask = new Task("Badminton", "Play Badminton", LocalDateTime.now().minusHours(1), Priority.HIGH,
				testCategory);

		taskRepository.save(overdueTask);

		List<Task> overdueTasks = taskService.getOverdueTasks();
		Assert.assertEquals(overdueTasks.size(), 1);
		Assert.assertTrue(overdueTasks.get(0).getDueDate().isBefore(LocalDateTime.now()));
	}

	@Test
	public void testAllTasks() throws Exception {
		taskService.createTask(testTask);

		Task secondTask = new Task("Plan meeting" + UUID.randomUUID().toString(),
				"Finalize topics for Friday's team meeting", LocalDateTime.now().plusDays(2), Priority.LOW,
				testCategory);

		Task savedSecondTask = taskService.createTask(secondTask);
		Assert.assertNotNull(savedSecondTask.getTaskId());

		List<Task> allTasks = taskService.getAllTasks();
		Assert.assertEquals(allTasks.size(), 2);
	}

	@Test(expectedExceptions = TaskValidationException.class)
	public void testCreateTaskWithoutTitle() throws Exception {
		testTask.setTitle("");
		taskService.createTask(testTask);
	}
}
