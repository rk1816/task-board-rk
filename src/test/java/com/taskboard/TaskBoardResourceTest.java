package com.taskboard;

import com.taskboard.models.Task;
import com.taskboard.resources.TaskBoardResource;
import com.taskboard.service.TaskBoardService;
import com.taskboard.models.TaskList;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskBoardResourceTest {

    @Mock
    private TaskBoardService service;

    @InjectMocks
    private TaskBoardResource resource;

    @BeforeEach
    public void setup() {
    }

    // User Story 1: View all lists and tasks
    @Test
    public void testGetAllLists() throws SQLException {
        List<TaskList> mockLists = List.of(
                new TaskList(1, "List1"),
                new TaskList(2, "List2")
        );
        when(service.getAllTaskLists()).thenReturn(mockLists);

        Response response = resource.getAllLists();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        List<TaskList> taskLists = (List<TaskList>) response.getEntity();
        assertEquals(2, taskLists.size());
        assertEquals("List1", taskLists.get(0).getName());
        assertEquals("List2", taskLists.get(1).getName());
        verify(service, times(1)).getAllTaskLists();
    }

    @Test
    public void testGetAllListsSQLException() throws SQLException {
        when(service.getAllTaskLists()).thenThrow(new SQLException("Database error"));

        Response response = resource.getAllLists();
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals("Database error", response.getEntity());
        verify(service, times(1)).getAllTaskLists();
    }

    // User Story 2: Create an empty list
    @Test
    public void testCreateList() throws SQLException {
        TaskList mockList = new TaskList(3, "New List");
        when(service.createList("New List")).thenReturn(mockList);

        Response response = resource.createList("New List");
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
    }

    @Test
    public void testCreateListEmptyName() throws SQLException {
        when(service.createList("")).thenThrow(new IllegalArgumentException("List name is required"));

        Response response = resource.createList("");
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("List name is required", response.getEntity());
        verify(service, times(1)).createList("");
    }

    // User Story 3: Add new task to an existing list
    @Test
    public void testCreateTask() throws SQLException {
        Task mockTask = new Task(1, 1, "New Task", "New Desc");
        when(service.createTask(1, "New Task", "New Desc")).thenReturn(mockTask);

        Task requestTask = new Task(0, 1, "New Task", "New Desc");
        Response response = resource.createTask(1, requestTask);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        Task createdTask = (Task) response.getEntity();
        assertEquals("New Task", createdTask.name());
        assertEquals("New Desc", createdTask.description());
        assertEquals(1, createdTask.listId());
        verify(service, times(1)).createTask(1, "New Task", "New Desc");
    }

    @Test
    public void testCreateTaskEmptyName() throws SQLException {
        when(service.createTask(1, "", "New Desc")).thenThrow(new IllegalArgumentException("Task name is required"));

        Task requestTask = new Task(0, 1, "", "New Desc");
        Response response = resource.createTask(1, requestTask);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Task name is required", response.getEntity());
        verify(service, times(1)).createTask(1, "", "New Desc");
    }

    // User Story 4: Update task name and description
    @Test
    public void testUpdateTask() throws SQLException {
        doNothing().when(service).updateTask(1, "Updated Task", "Updated Desc");

        Task requestTask = new Task(1, 1, "Updated Task", "Updated Desc");
        Response response = resource.updateTask(1, requestTask);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(service, times(1)).updateTask(1, "Updated Task", "Updated Desc");
    }

    // User Story 5: Delete a task
    @Test
    public void testDeleteTask() throws SQLException {
        doNothing().when(service).deleteTask(1);

        Response response = resource.deleteTask(1);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(service, times(1)).deleteTask(1);
    }

    @Test
    public void testDeleteTaskSQLException() throws SQLException {
        doThrow(new SQLException("Task not found")).when(service).deleteTask(999);

        Response response = resource.deleteTask(999);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals("Task not found", response.getEntity());
        verify(service, times(1)).deleteTask(999);
    }

    // User Story 6: Delete a list and its tasks
    @Test
    public void testDeleteList() throws SQLException {
        doNothing().when(service).deleteList(1);

        Response response = resource.deleteList(1);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(service, times(1)).deleteList(1);
    }

    @Test
    public void testDeleteListSQLException() throws SQLException {
        doThrow(new SQLException("List not found")).when(service).deleteList(999);

        Response response = resource.deleteList(999);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals("List not found", response.getEntity());
        verify(service, times(1)).deleteList(999);
    }

    // User Story 7: Move task to a different list
    @Test
    public void testMoveTask() throws SQLException {
        doNothing().when(service).moveTask(1, 2);

        Response response = resource.moveTask(1, 2);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(service, times(1)).moveTask(1, 2);
    }

    @Test
    public void testMoveTaskSQLException() throws SQLException {
        doThrow(new SQLException("Task not found")).when(service).moveTask(999, 2);

        Response response = resource.moveTask(999, 2);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals("Task not found", response.getEntity());
        verify(service, times(1)).moveTask(999, 2);
    }
}
