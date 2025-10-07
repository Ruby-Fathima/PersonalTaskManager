package com.taskmanager.model;

import java.time.LocalDateTime;

import java.util.UUID;

public class Task {
 
    private final String taskId;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Priority priority;
    private Status status;
    private Category category;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public Task(String title, String description, LocalDateTime dueDate, Priority priority, Category category) {
    	
        this.taskId = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.category = category;
        this.status = Status.PENDING; 
        this.createdDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
    }


    //--getters--
    public String getTaskId() {
        return taskId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public Status getStatus() {
        return status;
    }

    public Category getCategory() {
        return category;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

  
    //--setters---
    public void setTitle(String title) {
        this.title = title;
        this.lastModifiedDate = LocalDateTime.now();
    }

    public void setDescription(String description) {
        this.description = description;
        this.lastModifiedDate = LocalDateTime.now();
    }

    public void setDueDate(LocalDateTime localDate) {
        this.dueDate = localDate;
        this.lastModifiedDate = LocalDateTime.now();
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
        this.lastModifiedDate = LocalDateTime.now();
    }

    public void setStatus(Status status) {
        this.status = status;
        this.lastModifiedDate = LocalDateTime.now();
    }

    public void setCategory(Category category) {
        this.category = category;
        this.lastModifiedDate = LocalDateTime.now();
    }
   
    public void setcreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
		
	}
    
    public void setLastModifiedDate(LocalDateTime lastModifiedDate ) {
		this.lastModifiedDate = lastModifiedDate;
		
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) 
        	
        	return false;
        Task task = (Task) o;
        return taskId.equals(task.taskId);
    }
    
    @Override
    public int hashCode() {
        return taskId.hashCode();
    }
  
    @Override
    public String toString() {
        return "Task{" +
                "taskId='" + taskId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dueDate=" + dueDate +
                ", priority=" + priority +
                ", category=" + category +
                ", status=" + status +
                ", createdDate=" + createdDate +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }
}