package com.taskmanager.repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.taskmanager.model.Category;

public class CategoryRepository {

	private ConcurrentHashMap<String, Category> categories = new ConcurrentHashMap<>();

	public Category save(Category category) {
		categories.put(category.getCategoryId(), category);
		return category;
	}

	public Optional<Category> findById(String categoryId) {
		return Optional.ofNullable(categories.get(categoryId));
	}

	public List<Category> findAll() {
		return categories.values().stream().collect(Collectors.toUnmodifiableList());
	}
	public void deleteById(String categoryId) {
		categories.remove(categoryId);
	}
	public boolean exists(String categoryId) {
		return categories.containsKey(categoryId);
	}
	public void clear() {
		categories.clear();
	}
}
