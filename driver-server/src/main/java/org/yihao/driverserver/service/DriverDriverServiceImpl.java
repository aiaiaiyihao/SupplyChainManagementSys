package org.yihao.driverserver.service;

import com.google.common.base.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.yihao.driverserver.Exception.APIException;
import org.yihao.driverserver.Exception.ResourceNotFoundException;
import org.yihao.shared.ENUMS.DriverStatus;
import org.yihao.driverserver.model.Driver;
import org.yihao.driverserver.repository.DeliveryRepository;
import org.yihao.driverserver.repository.DriverRepository;
import org.yihao.shared.DTOS.DriverDTO;
import org.yihao.shared.ENUMS.DeliveryStatus;

import java.time.LocalDate;
import java.util.List;

@Service
public class DriverDriverServiceImpl implements DriverDriverService {
    private DeliveryRepository deliveryRepository;
    private DriverRepository driverRepository;
    private ModelMapper modelMapper;
    public DriverDriverServiceImpl(DeliveryRepository deliveryRepository
            , DriverRepository driverRepository, ModelMapper modelMapper) {
        this.deliveryRepository = deliveryRepository;
        this.driverRepository = driverRepository;
        this.modelMapper = modelMapper;
    }
    @Override
    public DriverDTO driverStatusRefresh(Long driverId) {

        Driver driverFound = driverRepository.findById(driverId)
                .orElseThrow(()->new ResourceNotFoundException("driver","driverId",driverId));
        long numOfTasks = deliveryRepository.countByDriverIdAndStatuses(
                driverId, List.of(DeliveryStatus.CREATED,
                        DeliveryStatus.APPOINTED,DeliveryStatus.DELIVERING));
        //Training and Vacation shouldn't be switched to idle based on task
        if(numOfTasks == 0
                &&!driverFound.getDriverStatus().equals(DriverStatus.TRAINING)
                &&!driverFound.getDriverStatus().equals(DriverStatus.VACATION)
        ){
            driverFound.setDriverStatus(DriverStatus.IDLE);
            Driver saved = driverRepository.save(driverFound);
            return modelMapper.map(saved, DriverDTO.class);
        }
        return modelMapper.map(driverFound, DriverDTO.class);
    }

    @Override
    public DriverDTO findDriveById(Long driverId) {
        Driver driverFound = driverRepository.findById(driverId)
                .orElseThrow(()->new ResourceNotFoundException("driver","driverId",driverId));
        return modelMapper.map(driverFound, DriverDTO.class);
    }

    @Override
    public DriverDTO updateDriveById(Long driverId, DriverDTO driverDTO) {
        Driver driverFound = driverRepository.findById(driverId)
                .orElseThrow(()->new ResourceNotFoundException("driver","driverId",driverId));

        //Email cannot be changed
        if (driverDTO.getEmail() != null&&!driverDTO.getEmail().equals(driverFound.getEmail())){
            throw new APIException("You cannot change email");
        }

        // Phone Number
        if (driverDTO.getPhoneNumber() != null && !driverDTO.getPhoneNumber().equals(driverFound.getPhoneNumber())) {
            if (driverRepository.existsDriverByPhoneNumber(driverDTO.getPhoneNumber())) {
                throw new APIException("Phone number is already in use by another driver.");
            }
            driverFound.setPhoneNumber(driverDTO.getPhoneNumber());
        }

        // License Number
        if (driverDTO.getLicenseNumber() != null && !driverDTO.getLicenseNumber().equals(driverFound.getLicenseNumber())) {
            if (driverRepository.existsDriverByLicenseNumber(driverDTO.getLicenseNumber())) {
                throw new APIException("License number is already in use by another driver.");
            }
            driverFound.setLicenseNumber(driverDTO.getLicenseNumber());
        }

        // Update other fields only if not null
        if (driverDTO.getFirstName() != null) {
            driverFound.setFirstName(driverDTO.getFirstName());
        }

        if (driverDTO.getLastName() != null) {
            driverFound.setLastName(driverDTO.getLastName());
        }

        if (driverDTO.getDateOfBirth() != null) {
            driverFound.setDateOfBirth(driverDTO.getDateOfBirth());
        }

        if (driverDTO.getLicenseExpiryDate() != null) {
            if(driverDTO.getLicenseExpiryDate().isBefore(LocalDate.now())){
                throw new APIException("Cannot register expired license");
            }
            driverFound.setLicenseExpiryDate(driverDTO.getLicenseExpiryDate());
        }

        Driver saved = driverRepository.save(driverFound);
        return modelMapper.map(saved, DriverDTO.class);
    }
}
