package com.epam.crmgymboot.actuator;

import org.springframework.boot.actuate.endpoint.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * <a href="http://localhost:8080//actuator/custom">...</a>
 */
@Component
@Endpoint(id = "releaseNotes")
public class ReleaseNotesEndpoint {
    @ReadOperation
    public List<String> getReleaseNotes() {
        return Collections.emptyList();
    }

    @WriteOperation
    public String addReleaseNote(String version, String changeLogData) {
        return "version 1.0.0";
    }

    @DeleteOperation
    public void deleteReleaseNote(@Selector String version) {

    }
}
