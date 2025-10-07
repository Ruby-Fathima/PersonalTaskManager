package com.taskmanager.model;

import java.util.UUID;

public final class Category {
	private  String categoryId;
	private  String name;
	private  String description;

	public Category(String name, String description) {
		this.categoryId = UUID.randomUUID().toString();
		this.name = name;
		this.description = description;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
	public void setCategoryId(String categoryId) {
		this.categoryId= categoryId;
	}

	public void setName(String name) {
		this.name= name;
	}

	public void setDescription(String description) {
		this.description= description;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return categoryId.equals(category.categoryId);
    }
    
    @Override
    public int hashCode() {
        return categoryId.hashCode();
    }
    
    @Override
    public String toString() {
        return "Category{" +
        		"categoryId='" + categoryId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

