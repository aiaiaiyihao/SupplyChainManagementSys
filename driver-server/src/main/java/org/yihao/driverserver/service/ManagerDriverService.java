package org.yihao.driverserver.service;

import jakarta.validation.Valid;
import org.yihao.shared.DTOS.*;
import org.yihao.shared.ENUMS.DriverStatus;

public interface ManagerDriverService {
    DriverResponse getAllDrivers(DriverStatus driverStatus, Integer pageNumber, Integer pageSize, String sortBy, boolean desc);

    DriverDTO getDriverById(Long id);

    DriverDTO createDriver(DriverDTO driverDTO);

    DriverDTO updateById(Long id, @Valid DriverDTO driverDTO);

    DriverDTO deleteById(Long id);

    DriverDTO getTheMostAvailableDriver();

    DriverDTO updateDriverStatus(DriverStatusChangeRequest request);
}
