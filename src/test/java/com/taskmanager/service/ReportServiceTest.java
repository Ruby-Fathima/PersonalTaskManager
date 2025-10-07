package com.taskmanager.service;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.taskmanager.model.Category;
import com.taskmanager.model.Priority;
import com.taskmanager.model.Status;
import com.taskmanager.model.Task;
import com.taskmanager.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.Map;

public class ReportServiceTest {

	private ReportService reportService;
	private TaskRepository taskRepository;
	private Category testCategory;

	@BeforeMethod
	public void setUp() {
		taskRepository = new TaskRepository();
		reportService = new ReportService(taskRepository);
		testCategory = new Category("Research laptop", "Find new laptop");
	}

	@Test
	public void testTasksCountByStatus() {
		// Create tasks with different statuses
		Task pendingTask = new Task("Learn Java", "Learn some besics in Java", LocalDateTime.now().plusDays(1),
				Priority.HIGH, testCategory);
		pendingTask.setStatus(Status.PENDING);

		Task completedTask = new Task("Water plants", "Have to water plants", LocalDateTime.now().plusDays(1),
				Priority.HIGH, testCategory);
		completedTask.setStatus(Status.COMPLETED);

		taskRepository.save(pendingTask);
		taskRepository.save(completedTask);

		Assert.assertEquals(reportService.tasksCountByStatus(Status.PENDING), 1);
		Assert.assertEquals(reportService.tasksCountByStatus(Status.COMPLETED), 1);
	}

	@Test
	public void testTasksCountByPriority() {
		Task highPriorityTask = new Task("Go to Office", "Have to schedule meeting", LocalDateTime.now().plusDays(1),
				Priority.HIGH, testCategory);

		Task lowPriorityTask = new Task("Hobby", "Create some art", LocalDateTime.now().plusDays(1), Priority.LOW,
				testCategory);

		taskRepository.save(highPriorityTask);
		taskRepository.save(lowPriorityTask);

		System.out.println(highPriorityTask);

		Assert.assertEquals(reportService.tasksCountByPriority(Priority.HIGH), 1);
		Assert.assertEquals(reportService.tasksCountByPriority(Priority.LOW), 1);
	}

	@Test
	public void testStatusReport() {
		Task pendingTask = new Task("Go to bank", "Need to Go bank", LocalDateTime.now().plusDays(1), Priority.HIGH,
				testCategory);
		taskRepository.save(pendingTask);

		Map<Status, Long> statusReport = reportService.taskStatusReport();
		Assert.assertEquals(statusReport.get(Status.PENDING).longValue(), 1L);
	}

	@Test
	public void testPriorityReport() {
		Task highPriorityTask = new Task("Pay Bill", "have to pay credit card bill", LocalDateTime.now().plusDays(1),
				Priority.HIGH, testCategory);
		taskRepository.save(highPriorityTask);

		Map<Priority, Long> priorityReport = reportService.taskPriorityReport();
		Assert.assertEquals(priorityReport.get(Priority.HIGH).longValue(), 1L);
	}

	@Test
	public void testCategoryReport() {
		Task task = new Task("Travel", "Spend some time in nature", LocalDateTime.now().plusDays(1), Priority.HIGH,
				testCategory);
		taskRepository.save(task);

		Map<String, Long> categoryReport = reportService.categoryReport();
		Assert.assertEquals(categoryReport.get("Research laptop").longValue(), 1L);
	}

	@Test
	public void testOverdueTasks() {
		Task overdueTask = new Task("Project ", "Submission of project", LocalDateTime.now().minusDays(1),
				Priority.HIGH, testCategory);
		taskRepository.save(overdueTask);

		Assert.assertEquals(reportService.overdueTasksCount(), 1);
	}

	@Test
	public void testEmptyReport() {
		String report = reportService.generateReport();
		Assert.assertTrue(report.contains("Total Tasks: 0"));
	}

	@Test
	public void testGenerateReport() {
		Task task = new Task("Office work", "Need to schedule meeting", LocalDateTime.now().plusDays(1), Priority.HIGH,
				testCategory);
		taskRepository.save(task);

		String report = reportService.generateReport();
		System.out.println(report);
	}

}