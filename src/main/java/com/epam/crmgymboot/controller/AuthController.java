package com.epam.crmgymboot.controller;

import com.epam.crmgymboot.actuator.RequestsCounterMetrics;
import com.epam.crmgymboot.dto.common.TokenDto;
import com.epam.crmgymboot.dto.request.RefreshTokenRequest;
import com.epam.crmgymboot.dto.request.SignInRequest;
import com.epam.crmgymboot.dto.request.SignUpTraineeRequest;
import com.epam.crmgymboot.dto.request.SignUpTrainerRequest;
import com.epam.crmgymboot.dto.response.SignInResponse;
import com.epam.crmgymboot.dto.response.SignUpResponse;
import com.epam.crmgymboot.service.AuthService;
import com.epam.crmgymboot.service.TraineeService;
import com.epam.crmgymboot.service.TrainerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RequestsCounterMetrics metrics;
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    @PostMapping("/signin")
    public ResponseEntity<SignInResponse> authenticateUser(@Valid @RequestBody SignInRequest request) {
        var body = authService.authenticateUser(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(body);
    }

    @PostMapping("/signup/trainee")
    @ResponseStatus(HttpStatus.CREATED)
    public SignUpResponse signUpTrainee(@Valid @RequestBody SignUpTraineeRequest request) {
        metrics.incrementPostRequests();
        return traineeService.signUpTrainee(request);
    }

    @PostMapping("/signup/trainer")
    @ResponseStatus(HttpStatus.CREATED)
    public SignUpResponse signUpTrainer(@Valid @RequestBody SignUpTrainerRequest request) {
        metrics.incrementPostRequests();
        return trainerService.signUpTrainer(request);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@Valid @RequestBody RefreshTokenRequest request) {
        TokenDto tokenDto = authService.refreshAccessAndRefreshTokens(request.getToken());
        return ResponseEntity.ok(tokenDto);
    }

    @PostMapping("/logout/{username}")
    public ResponseEntity<?> logOutUser(@PathVariable String username) {
        authService.logOutUser(username);
        return ResponseEntity.ok("successfully sign out!");
    }

}
