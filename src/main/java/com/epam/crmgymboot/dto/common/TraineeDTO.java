package com.epam.crmgymboot.dto.common;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TraineeDTO {
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
    @NotNull
    private List<UserDTO> assignedTrainers;

    public void setAddress(String address) {
        this.address = (address != null) ? address : "Empty Address";
    }

}
