package org.yihao.deliveryserver.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yihao.deliveryserver.enums.DriverStatus;


import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON
public class DriverDTO {
    private Long driverId;
    private DriverStatus driverStatus;

    private String firstName;
    private String lastName;
    @Email
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;

    private String licenseNumber;

    private LocalDate licenseExpiryDate;

}
