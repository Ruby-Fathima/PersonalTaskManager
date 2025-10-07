package com.taskmanager.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.taskmanager.model.Category;
import com.taskmanager.model.Priority;
import com.taskmanager.model.Task;


public class TaskRepositoryTest {
    private TaskRepository taskRepository;
    private Task testTask;
    private Category testCategory;

    @BeforeMethod
    public void setUp() {
        taskRepository = new TaskRepository();
        testCategory = new Category("Work ", "Professional tasks and work-related responsibilities");
        
        testTask = new Task("Daily Team Meeting", "Attend daily standup meeting with development team",
        					LocalDateTime.now().plusDays(1),
        					Priority.HIGH,
        					testCategory);
        
        System.out.println(testTask);
    }

    @Test(priority =1) 
    public void testSaveTask() {
        Task savedTask = taskRepository.save(testTask);
        Assert.assertNotNull(savedTask);
        Assert.assertEquals(savedTask.getTaskId(), testTask.getTaskId());
    }

    @Test(priority =2)
    public void testFindById() {
        Task savedTask = taskRepository.save(testTask);
        Optional<Task> foundTask = taskRepository.findById(savedTask.getTaskId());
        Assert.assertTrue(foundTask.isPresent());
        Assert.assertEquals(foundTask.get().getTitle(), testTask.getTitle());
    }

    @Test(priority =3)
    public void testFindByIdNotFound() {
        Optional<Task> notFoundTask = taskRepository.findById("non-existent-id");
        Assert.assertFalse(notFoundTask.isPresent());
    }

    @Test(priority =4)
    public void testFindAll() {
        taskRepository.save(testTask);
       
		Task secondTask = new Task("Project Deadline" + UUID.randomUUID().toString(), 
        							"Complete and submit final project deliverables",
        							LocalDateTime.now().plusDays(2),
        							Priority.LOW,
        							testCategory);
        
        taskRepository.save(secondTask);

        List<Task> allTasks = taskRepository.findAll();
        Assert.assertEquals(allTasks.size(), 2);
        System.out.println(secondTask);
    }

    @Test(priority =5)
    public void testDelete() {
        Task savedTask = taskRepository.save(testTask);
        taskRepository.deleteById(savedTask.getTaskId());
        Assert.assertFalse(taskRepository.exists(savedTask.getTaskId()));
    }

    @Test(priority =6)
    public void testExists() {
        Task savedTask = taskRepository.save(testTask);
        Assert.assertTrue(taskRepository.exists(savedTask.getTaskId()));
        Assert.assertFalse(taskRepository.exists("non-existent-id"));
    }

    @Test(priority =7)
    public void testClear() {
        taskRepository.save(testTask);
        taskRepository.clear();
        Assert.assertEquals(taskRepository.findAll().size(), 0);
    }

}
