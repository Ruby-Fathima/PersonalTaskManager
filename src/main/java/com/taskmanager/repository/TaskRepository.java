package com.taskmanager.repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import com.taskmanager.model.Task;

public class TaskRepository {

	private ConcurrentHashMap<String, Task> tasks = new ConcurrentHashMap<>();

	public Task save(Task task) {			 			//creating task or updating task
		tasks.put(task.getTaskId(), task);
		return task;
	}

	public Optional<Task> findById(String taskId) {		//finding task
		return Optional.ofNullable(tasks.get(taskId));
	}

	public List<Task> findAll() {						//find all the task
		return tasks.values().stream().collect(Collectors.toUnmodifiableList());
	}
	public void deleteById(String taskId) {					//delete the task
		tasks.remove(taskId);
	}
	public boolean exists(String taskId) {				//check the task is already task
		return tasks.containsKey(taskId);
	}
	public void clear() {
		tasks.clear();
	}
}
