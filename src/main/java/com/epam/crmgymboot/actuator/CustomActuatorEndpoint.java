package com.epam.crmgymboot.actuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.stereotype.Component;

/**
 * <a href="http://localhost:8080//actuator/custom">...</a>
 */
@Component
@Endpoint(id = "custom")
public class CustomActuatorEndpoint {

}
