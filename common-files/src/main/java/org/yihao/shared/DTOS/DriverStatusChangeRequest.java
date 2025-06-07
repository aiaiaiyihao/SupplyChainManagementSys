package org.yihao.shared.DTOS;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yihao.shared.ENUMS.DriverStatus;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverStatusChangeRequest {
    @NotNull(message = "Driver ID cannot be null")
    private Long driverId;
    @NotNull(message = "Driver Status cannot be null")
    private DriverStatus status;
}
