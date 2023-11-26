package com.epam.crmgymboot.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class UpdateTrainerRequest {
    @NotEmpty
    private String firstname;
    @NotEmpty
    private String lastname;
    @NotEmpty
    private String username;
    private String address;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("birthDate")
    @NotNull
    private LocalDate dateOfBirth;
    @NotNull
    private Boolean isActive;
    @NotEmpty
    private String specialization;

    public void setAddress(String address) {
        this.address = (address != null) ? address : "Empty Address";
    }

}
