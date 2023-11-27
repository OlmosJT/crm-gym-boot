package com.epam.crmgymboot.controller;

import com.epam.crmgymboot.dto.common.TraineeDTO;
import com.epam.crmgymboot.dto.common.TrainingDTO;
import com.epam.crmgymboot.dto.request.EnableDisableUserRequest;
import com.epam.crmgymboot.dto.request.SignUpTraineeRequest;
import com.epam.crmgymboot.dto.response.SignUpResponse;
import com.epam.crmgymboot.dto.response.TrainerResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:data/schema.sql", "classpath:data/data.sql"})
class TraineeControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("POST /trainees returns 201 Created and a SignUpResponse JSON")
    void signUpTrainee() {
        String url = "http://localhost:" + port + "/api/v1/trainees";

        SignUpTraineeRequest request = new SignUpTraineeRequest(
                "John",
                "Cena",
                "Canada, VG 1023",
                LocalDate.of(1977, 4,25)
        );

        ResponseEntity<SignUpResponse> response = restTemplate.postForEntity(url, request, SignUpResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(10, response.getBody().password().length());
    }

    @Test
    @DisplayName("GET /trainees/{username} returns 200 OK and a TraineeDTO JSON")
    void getTrainee_200OK() {
        String url = "http://localhost:" + port + "/api/v1/trainees/john.doe";

        ResponseEntity<TraineeDTO> response = restTemplate.getForEntity(url, TraineeDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getFirstname());
        assertEquals("Doe", response.getBody().getLastname());
        assertEquals("123 Main St", response.getBody().getAddress());
        assertTrue(response.getBody().getIsActive());
    }

    @Test
    @DisplayName("GET /trainees/{username} returns 404 Not Found and a ErrorAPI JSON")
    void getTrainee_404NotFound() {
        String url = "http://localhost:" + port + "/api/v1/trainees/random.user";

        ResponseEntity<TraineeDTO> response = restTemplate.getForEntity(url, TraineeDTO.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updateTrainee() {
    }

    @Test
    @DisplayName("DELETE /trainees/{username} returns 200 OK")
    void deleteTraineeProfile() {
        String url = "http://localhost:" + port + "/api/v1/trainees/john.doe";

        restTemplate.delete(url);

        ResponseEntity<TraineeDTO> response = restTemplate.getForEntity(url, TraineeDTO.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }



    @Test
    @DisplayName("PATCH trainees/{username} tries to activate or deactivate a unknown trainee")
    void enableDisableTrainee_404OK() {
        String url = "http://localhost:" + port + "/api/v1/trainees";

        UriComponents uriComponents = UriComponentsBuilder.fromUriString(url)
                .path("random.user")
                .queryParam("isActive", false)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Void> deactivateResponse = restTemplate.exchange(
                uriComponents.toUri(),
                HttpMethod.PUT,
                new HttpEntity<>(headers),
                Void.class
        );

        assertEquals(HttpStatus.NOT_FOUND, deactivateResponse.getStatusCode());
    }

    @Test
    void getActiveTrainersNotAssignedOnTrainee() {
        String url = "http://localhost:" + port + "/api/v1/trainees/john.doe/available-trainers";

        ResponseEntity<Object[]> response = restTemplate.getForEntity(
            url, Object[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

    }
}