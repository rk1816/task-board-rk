package com.taskboard.resources;

import com.taskboard.models.*;
import com.taskboard.service.TaskBoardService;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

@Path("/taskboard")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskBoardResource {
    private static final Logger logger = LoggerFactory.getLogger(TaskBoardResource.class);
    private final TaskBoardService service;

    public TaskBoardResource() {
        logger.info("Initializing TaskBoardResource with default constructor");
        this.service = new TaskBoardService();
        logger.info("TaskBoardResource initialized");
    }

    public TaskBoardResource(TaskBoardService service) {
        logger.info("Initializing TaskBoardResource with service: {}", service);
        this.service = service;
        logger.info("TaskBoardResource initialized");
    }

    // User Story 1: View all lists and tasks
    @GET
    @Path("/lists")
    public Response getAllLists() {
        logger.info("Handling GET /api/taskboard/lists");
        try {
            List<TaskList> taskLists = service.getAllTaskLists();
            return Response.ok(taskLists).build();
        } catch (SQLException e) {
            logger.error("Error fetching task lists", e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // User Story 2: Create an empty list
    @POST
    @Path("/lists")
    public Response createList(@QueryParam("name") String name) {
        logger.info("Handling POST /api/taskboard/lists with name: {}", name);
        try {
            service.createList(name);
            return Response.status(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request: {}", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (SQLException e) {
            logger.error("Error creating list", e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // User Story 3: Add new task to an existing list
    @POST
    @Path("/lists/{listId}/tasks")
    public Response createTask(@PathParam("listId") int listId, Task task) {
        logger.info("Handling POST /api/taskboard/lists/{}/tasks", listId);
        try {
            Task newTask = service.createTask(listId, task.name(), task.description());
            return Response.status(Response.Status.CREATED).entity(newTask).build();
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request: {}", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (SQLException e) {
            logger.error("Error creating task", e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // User Story 4: Update task name and description
    @PUT
    @Path("/tasks/{taskId}")
    public Response updateTask(@PathParam("taskId") int taskId, Task task) {
        logger.info("Handling PUT /api/taskboard/tasks/{}", taskId);
        try {
            service.updateTask(taskId, task.name(), task.description());
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid request: {}", e.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (SQLException e) {
            logger.error("Error updating task", e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // User Story 5: Delete a task
    @DELETE
    @Path("/tasks/{taskId}")
    public Response deleteTask(@PathParam("taskId") int taskId) {
        logger.info("Handling DELETE /api/taskboard/tasks/{}", taskId);
        try {
            service.deleteTask(taskId);
            return Response.ok().build();
        } catch (SQLException e) {
            logger.error("Error deleting task", e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // User Story 6: Delete a list and its tasks
    @DELETE
    @Path("/lists/{listId}")
    public Response deleteList(@PathParam("listId") int listId) {
        logger.info("Handling DELETE /api/taskboard/lists/{}", listId);
        try {
            service.deleteList(listId);
            return Response.ok().build();
        } catch (SQLException e) {
            logger.error("Error deleting list", e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // User Story 7: Move task to a different list
    @PUT
    @Path("/tasks/{taskId}/move")
    public Response moveTask(@PathParam("taskId") int taskId, @QueryParam("newListId") int newListId) {
        logger.info("Handling PUT /api/taskboard/tasks/{}/move to newListId: {}", taskId, newListId);
        try {
            service.moveTask(taskId, newListId);
            return Response.ok().build();
        } catch (SQLException e) {
            logger.error("Error moving task", e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}