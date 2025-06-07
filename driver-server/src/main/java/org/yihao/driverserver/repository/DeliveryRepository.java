package org.yihao.driverserver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.yihao.driverserver.model.Delivery;
import org.yihao.shared.ENUMS.DeliveryStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Page<Delivery> findByDriverId(Long driverId, Pageable pageable);
    List<Delivery> findByDriverId(Long driverId);
    Optional<Delivery> findByDriverIdAndDeliveryId(Long driverId, Long deliveryId);

    @Query("SELECT COUNT(d) FROM Delivery d WHERE d.driverId = :driverId AND d.deliveryStatus IN (:statuses)")
    long countByDriverIdAndStatuses(@Param("driverId") Long driverId,
                                    @Param("statuses") List<DeliveryStatus> statuses);

    List<Delivery> findByDeliveryStatusInAndEstimatedDeliveryTimeBefore(List<DeliveryStatus> created, LocalDateTime now);
}
