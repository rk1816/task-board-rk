package com.taskboard.repo;

import com.taskboard.config.DatabaseConfig;
import com.taskboard.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.*;

public class TaskBoardRepo {
    private static final Logger logger = LoggerFactory.getLogger(TaskBoardRepo.class);

    public List<TaskList> getAllTaskLists() throws SQLException {
        logger.debug("Fetching all task lists with tasks from database");
        List<TaskList> taskLists = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection()) {
            String listQuery = "SELECT * FROM task_lists ORDER BY id";
            try (PreparedStatement stmt = conn.prepareStatement(listQuery)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    taskLists.add(new TaskList(rs.getInt("id"), rs.getString("name")));
                }
            }

            String taskQuery = "SELECT * FROM tasks WHERE list_id = ? ORDER BY id";
            for (TaskList list : taskLists) {
                try (PreparedStatement stmt = conn.prepareStatement(taskQuery)) {
                    stmt.setInt(1, list.getId());
                    ResultSet rs = stmt.executeQuery();
                    List<Task> tasks = new ArrayList<>();
                    while (rs.next()) {
                        tasks.add(new Task(
                                rs.getInt("id"),
                                rs.getInt("list_id"),
                                rs.getString("name"),
                                rs.getString("description")
                        ));
                    }
                    list.setTasks(tasks);
                }
            }
            logger.debug("Fetched {} task lists", taskLists.size());
            return taskLists;
        } catch (SQLException e) {
            logger.error("Database error fetching task lists", e);
            throw e;
        }
    }

    public TaskList createList(String name) throws SQLException {
        logger.debug("Creating list with name: {}", name);
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO task_lists (name) VALUES (?) RETURNING id")) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            TaskList list = new TaskList(rs.getInt("id"), name);
            logger.info("Created list: {}", list);
            return list;
        } catch (SQLException e) {
            logger.error("Database error creating list", e);
            throw e;
        }
    }

    public Task createTask(int listId, String name, String description) throws SQLException {
        logger.debug("Creating task for listId: {}, name: {}", listId, name);
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO tasks (list_id, name, description) VALUES (?, ?, ?) RETURNING id")) {
            stmt.setInt(1, listId);
            stmt.setString(2, name);
            stmt.setString(3, description);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            Task task = new Task(rs.getInt("id"), listId, name, description);
            logger.info("Created task: {}", task);
            return task;
        } catch (SQLException e) {
            logger.error("Database error creating task", e);
            throw e;
        }
    }

    public void updateTask(int taskId, String name, String description) throws SQLException {
        logger.debug("Updating task with id: {}, name: {}, description: {}", taskId, name, description);
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE tasks SET name = ?, description = ? WHERE id = ?")) {
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setInt(3, taskId);
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                logger.warn("No task found with id: {}", taskId);
                throw new SQLException("Task not found");
            }
            logger.info("Updated task with id: {}", taskId);
        } catch (SQLException e) {
            logger.error("Database error updating task", e);
            throw e;
        }
    }

    public void deleteTask(int taskId) throws SQLException {
        logger.debug("Deleting task with id: {}", taskId);
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM tasks WHERE id = ?")) {
            stmt.setInt(1, taskId);
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                logger.warn("No task found with id: {}", taskId);
                throw new SQLException("Task not found");
            }
            logger.info("Deleted task with id: {}", taskId);
        } catch (SQLException e) {
            logger.error("Database error deleting task", e);
            throw e;
        }
    }

    public void deleteList(int listId) throws SQLException {
        logger.debug("Deleting list with id: {}", listId);
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM task_lists WHERE id = ?")) {
            stmt.setInt(1, listId);
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                logger.warn("No list found with id: {}", listId);
                throw new SQLException("List not found");
            }
            logger.info("Deleted list with id: {} (and its tasks due to CASCADE)", listId);
        } catch (SQLException e) {
            logger.error("Database error deleting list", e);
            throw e;
        }
    }

    public void moveTask(int taskId, int newListId) throws SQLException {
        logger.debug("Moving task with id: {} to listId: {}", taskId, newListId);
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE tasks SET list_id = ? WHERE id = ?")) {
            stmt.setInt(1, newListId);
            stmt.setInt(2, taskId);
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                logger.warn("No task found with id: {}", taskId);
                throw new SQLException("Task not found");
            }
            logger.info("Moved task with id: {} to listId: {}", taskId, newListId);
        } catch (SQLException e) {
            logger.error("Database error moving task", e);
            throw e;
        }
    }
}