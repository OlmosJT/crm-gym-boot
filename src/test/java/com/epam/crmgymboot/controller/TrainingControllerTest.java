package com.epam.crmgymboot.controller;

import com.epam.crmgymboot.dto.common.TrainingDTO;
import com.epam.crmgymboot.dto.request.AddTrainingRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:data/schema.sql", "classpath:data/data.sql"})
class TrainingControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("POST api/v1/trainings 201 CREATED")
    void addTraining() {
        String url = "http://localhost:" + port + "/api/v1/trainings";

        AddTrainingRequest request = new AddTrainingRequest(
                "john.doe",
                "ava.taylor",
                "Yoga session with John Doe for an hour",
                LocalDateTime.now(),
                60
        );

        ResponseEntity<TrainingDTO> response = restTemplate.postForEntity(
                url,
                request,
                TrainingDTO.class
        );
        System.out.println(response.toString());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}