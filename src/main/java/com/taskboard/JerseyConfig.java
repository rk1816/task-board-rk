package com.taskboard;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(JerseyConfig.class);
    public JerseyConfig() {
        packages("org.glassfish.jersey.jackson", "com.taskboard");
        property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);
        //register(TaskBoardResource.class);
        //register(TestResource.class);
        //register(JacksonFeature.class);
        System.out.println("JerseyConfig: registered");
    }
}
