package com.taskmanager.service;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.taskmanager.exception.CategoryNotFoundException;
import com.taskmanager.model.Category;
import com.taskmanager.repository.CategoryRepository;

import java.util.List;

public class CategoryServiceTest {
	private CategoryService categoryService;
	private CategoryRepository categoryRepository;
	private Category testCategory;

	@BeforeMethod
	public void setUp() {
		categoryRepository = new CategoryRepository();
		categoryService = new CategoryService(categoryRepository);

		testCategory = new Category("Code Review", "Code review and test review");
		testCategory.setName("Code Review");
		testCategory.setDescription("Code review and test review");
	}

	@Test
	public void testCreateCategory() {
		Category createdCategory = categoryService.createCategory(testCategory);
		Assert.assertNotNull(createdCategory.getCategoryId());
		Assert.assertEquals(createdCategory.getName(), "Code Review");
		Assert.assertEquals(createdCategory.getDescription(), "Code review and test review");
	}

	@Test
	public void testUpdateCategory() throws CategoryNotFoundException {
		Category createdCategory = categoryService.createCategory(testCategory);
		String categoryId = createdCategory.getCategoryId();

		Category updatedCategory = new Category("Creative", "Do some Art work");
		updatedCategory.setName("Creative");
		updatedCategory.setDescription("Do some Art work");

		Category result = categoryService.updateCategory(categoryId, updatedCategory);
		Assert.assertEquals(result.getName(), "Creative");
		Assert.assertEquals(result.getDescription(), "Do some Art work");
	}

	@Test(expectedExceptions = CategoryNotFoundException.class)
	public void testUpdateNonExistentCategory() throws CategoryNotFoundException {
		Category updatedCategory = new Category("Creative", "non-existent-id");
		updatedCategory.setName("Creative");
		categoryService.updateCategory("non-existent-id", updatedCategory);
	}

	@Test
	public void testCategoryById() throws CategoryNotFoundException {
		Category createdCategory = categoryService.createCategory(testCategory);
		Category retrievedCategory = categoryService.getCategoryById(createdCategory.getCategoryId());
		Assert.assertEquals(retrievedCategory.getCategoryId(), createdCategory.getCategoryId());
		Assert.assertEquals(retrievedCategory.getName(), createdCategory.getName());
	}

	@Test
	public void testDeleteCategory() throws CategoryNotFoundException {
		Category createdCategory = categoryService.createCategory(testCategory);
		String categoryId = createdCategory.getCategoryId();

		categoryService.deleteCategory(categoryId);

		Assert.assertThrows(CategoryNotFoundException.class, () -> {
			categoryService.getCategoryById(categoryId);
		});
	}

	@Test
	public void testGetAllCategories() {
		categoryService.createCategory(testCategory);

		Category secondCategory = new Category("Ticket booking", "Have to book train ticket");
		secondCategory.setName("Ticket booking");
		secondCategory.setDescription("Have to book train ticket");
		categoryService.createCategory(secondCategory);

		List<Category> allCategories = categoryService.getAllCategories();
		Assert.assertEquals(allCategories.size(), 2);
	}

	@Test
	public void testAllEmptyRepositoryCategories() {
		List<Category> allCategories = categoryService.getAllCategories();
		Assert.assertEquals(allCategories.size(), 0);
	}
}
