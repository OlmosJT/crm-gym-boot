package com.epam.crmgymboot.controller;

import com.epam.crmgymboot.dto.common.TrainingTypeDTO;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:data/schema.sql", "classpath:data/data.sql"})
class TrainingTypeControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getTrainingTypes() {
        String url = "http://localhost:" + port + "/api/v1/training-types";
        ResponseEntity<TrainingTypeDTO[]> response = restTemplate.getForEntity(
                url,
                TrainingTypeDTO[].class
        );

        assertNotNull(response.getBody());
        TrainingTypeDTO[] trainingTypes = response.getBody();
        System.out.println(Arrays.toString(trainingTypes));
        // FIXME: It works well, but, creating Method is modifying table, and next time causing to false below:
        assertEquals(10, trainingTypes.length);
    }

    @Test
    @DisplayName("POST /api/v1/training-types 201 Created and String returned as JSON")
    void createTrainingType_200OK() {
        String url = "http://localhost:" + port + "/api/v1/training-types";

        UriComponents uriComponents = UriComponentsBuilder.fromUriString(url)
                .queryParam("name", "Stretching")
                .build();

        ResponseEntity<String> response = restTemplate.postForEntity(
                uriComponents.toUriString(),
                null,
                String.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("POST /api/v1/training-types 409 Conflict and String returned as JSON")
    void createTrainingType_409Conflict() {
        String url = "http://localhost:" + port + "/api/v1/training-types";

        UriComponents uriComponents = UriComponentsBuilder.fromUriString(url)
                .queryParam("name", "Yoga")
                .build();

        ResponseEntity<String> response = restTemplate.postForEntity(
                uriComponents.toUriString(),
                null,
                String.class
        );

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    @DisplayName("DELETE /training-types/{id} deletes training type")
    @Disabled("Training type is used as FK in Trainer entity. Therefore there is problem of deleting it.")
    void deleteTrainingType() {
    }

    @Test
    @Transactional
    void changeTrainingTypeName_404NOT_FOUND() {
        String url = "http://localhost:" + port + "/api/v1/training-types/123";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Void> deactivateResponse = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(headers),
                Void.class
        );
        // FIXME: 405 METHOD IS NOT ALLOWED status comes
        assertEquals(HttpStatus.NOT_FOUND, deactivateResponse.getStatusCode());
    }
}