package org.yihao.driverserver.repository;

import com.google.common.base.Optional;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.yihao.shared.ENUMS.DriverStatus;
import org.yihao.driverserver.model.Driver;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Long> , JpaSpecificationExecutor<Driver> {

    Optional<Driver> findByEmail(String email);

    boolean existsDriverByLicenseNumber(String licenseNumber);


    List<Driver> findByDriverStatus(DriverStatus driverStatus);

    boolean existsDriverByEmail(@Email String email);

    @Query(value = """
    SELECT d.driver_id 
    FROM deliveries d
    WHERE d.delivery_status IN ('DELIVERING', 'APPOINTED','CREATED')
    GROUP BY d.driver_id
    ORDER BY COUNT(*) ASC
    LIMIT 1
""", nativeQuery = true)
    Long findDriverWithLeastActiveTasks();

    boolean existsDriverByPhoneNumber(String phoneNumber);

}
