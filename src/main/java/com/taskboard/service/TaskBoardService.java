package com.taskboard.service;

import com.taskboard.models.*;
import com.taskboard.repo.TaskBoardRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class TaskBoardService {
    private static final Logger logger = LoggerFactory.getLogger(TaskBoardService.class);
    private final TaskBoardRepo repo;

    public TaskBoardService() {
        logger.info("Initializing TaskBoardService");
        this.repo = new TaskBoardRepo();
        logger.info("TaskBoardService initialized");
    }

    // User Story 1: View all lists and tasks
    public List<TaskList> getAllTaskLists() throws SQLException {
        logger.debug("Fetching all task lists with tasks");
        List<TaskList> taskLists = repo.getAllTaskLists();
        logger.debug("Retrieved {} task lists", taskLists.size());
        return taskLists;
    }

    // User Story 2: Create an empty list
    public TaskList createList(String name) throws SQLException {
        logger.debug("Creating list with name: {}", name);
        if (name == null || name.trim().isEmpty()) {
            logger.warn("Attempted to create list with null or empty name");
            throw new IllegalArgumentException("List name is required");
        }
        TaskList list = repo.createList(name);
        logger.info("Created list: {}", list);
        return list;
    }

    // User Story 3: Add new task to an existing list
    public Task createTask(int listId, String name, String description) throws SQLException {
        logger.debug("Creating task for listId: {}, name: {}", listId, name);
        if (name == null || name.trim().isEmpty()) {
            logger.warn("Attempted to create task with null or empty name");
            throw new IllegalArgumentException("Task name is required");
        }
        Task task = repo.createTask(listId, name, description);
        logger.info("Created task: {}", task);
        return task;
    }

    // User Story 4: Update task name and description
    public void updateTask(int taskId, String name, String description) throws SQLException {
        logger.debug("Updating task with id: {}, name: {}, description: {}", taskId, name, description);
        if (name == null || name.trim().isEmpty()) {
            logger.warn("Attempted to update task with null or empty name");
            throw new IllegalArgumentException("Task name is required");
        }
        repo.updateTask(taskId, name, description);
        logger.info("Updated task with id: {}", taskId);
    }

    // User Story 5: Delete a task
    public void deleteTask(int taskId) throws SQLException {
        logger.debug("Deleting task with id: {}", taskId);
        repo.deleteTask(taskId);
        logger.info("Deleted task with id: {}", taskId);
    }

    // User Story 6: Delete a list and its tasks
    public void deleteList(int listId) throws SQLException {
        logger.debug("Deleting list with id: {}", listId);
        repo.deleteList(listId);
        logger.info("Deleted list with id: {}", listId);
    }

    // User Story 7: Move task to a different list
    public void moveTask(int taskId, int newListId) throws SQLException {
        logger.debug("Moving task with id: {} to listId: {}", taskId, newListId);
        repo.moveTask(taskId, newListId);
        logger.info("Moved task with id: {} to listId: {}", taskId, newListId);
    }
}