package com.taskboard;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import java.net.URI;

public class TaskBoardApplication extends ResourceConfig {

    public static void main(String[] args) {
        URI baseUri = URI.create("http://0.0.0.0:8081/");
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, new JerseyConfig());
        System.out.println("Server started at http://0.0.0.0:8081/");

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            server.shutdownNow();
        }
    }
}
