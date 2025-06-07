package org.yihao.driverserver.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.yihao.driverserver.model.Delivery;
import org.yihao.driverserver.model.Driver;
import org.yihao.driverserver.repository.DriverRepository;
import org.yihao.shared.DTOS.DeliveryDelayDTO;
import org.yihao.shared.ENUMS.DeliveryStatus;
import org.yihao.driverserver.repository.DeliveryRepository;
import org.yihao.shared.config.AppConstants;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class DeliveryDelayReminderService {
    private final DeliveryRepository deliveryRepository;
    private final DriverRepository driverRepository;
    private final KafkaTemplate<String, DeliveryDelayDTO> kafkaTemplate;
    private final ModelMapper modelMapper;

    public DeliveryDelayReminderService(
            DeliveryRepository deliveryRepository, DriverRepository driverRepository,
            KafkaTemplate<String, DeliveryDelayDTO> kafkaTemplate, ModelMapper modelMapper) {
        this.deliveryRepository = deliveryRepository;
        this.driverRepository = driverRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.modelMapper = modelMapper;
    }

    //every 24 hours
    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void remindDelayedDeliveries() {
        LocalDateTime now = LocalDateTime.now();
        List<Delivery> delayedDeliveries = deliveryRepository.findByDeliveryStatusInAndEstimatedDeliveryTimeBefore(
                List.of(DeliveryStatus.CREATED, DeliveryStatus.APPOINTED, DeliveryStatus.DELIVERING), now);
        if (delayedDeliveries.isEmpty()) {
            return;
        }
        for(Delivery delivery : delayedDeliveries) {
            DeliveryDelayDTO deliveryDelayDTO = new DeliveryDelayDTO();
            deliveryDelayDTO.setDeliveryId(delivery.getDeliveryId());
            deliveryDelayDTO.setEstimatedDeliveryTime(delivery.getEstimatedDeliveryTime());
            Long driverId = delivery.getDriverId();
            Driver driver = driverRepository.findById(driverId).orElse(null);
            if (driver == null) continue;
            deliveryDelayDTO.setDriverId(delivery.getDriverId());
            deliveryDelayDTO.setDriverName(driver.getFirstName()+" "+driver.getLastName());
            deliveryDelayDTO.setDriveEmail(driver.getEmail());
            generateEmail(deliveryDelayDTO);
        }
    }

    private void generateEmail(DeliveryDelayDTO deliveryDelayDTO) {
        CompletableFuture<SendResult<String, DeliveryDelayDTO>> future
                = kafkaTemplate.send(AppConstants.TOPIC_NAME_DELIVERY_DELAY, deliveryDelayDTO);
        future.whenComplete((res, err) -> {
            if (Objects.isNull(err)) {
                log.info("send message=[{}] with offset=[{}]",
                        deliveryDelayDTO.toString(), res.getRecordMetadata().offset());
            } else {
                log.error("Unable to send message=[{}] due to: {}",
                        deliveryDelayDTO.toString(), err.getMessage());
            }
        });
    }

}
