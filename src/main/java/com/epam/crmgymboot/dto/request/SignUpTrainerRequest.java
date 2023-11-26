package com.epam.crmgymboot.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SignUpTrainerRequest {
    @NotEmpty
    private String firstname;
    @NotEmpty
    private String lastname;
    private String address;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("birthDate")
    @NotNull
    private LocalDate dateOfBirth;
    @NotNull
    @Min(value = 1L)
    private Long specializationId;

    public void setAddress(String address) {
        this.address = (address != null) ? address : "Empty Address";
    }
}
