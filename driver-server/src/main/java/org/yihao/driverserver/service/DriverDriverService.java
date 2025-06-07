package org.yihao.driverserver.service;

import org.yihao.shared.DTOS.DriverDTO;

public interface DriverDriverService {
    DriverDTO driverStatusRefresh(Long driverId);

    DriverDTO findDriveById(Long driverId);

    DriverDTO updateDriveById(Long driverId, DriverDTO driverDTO);

}
