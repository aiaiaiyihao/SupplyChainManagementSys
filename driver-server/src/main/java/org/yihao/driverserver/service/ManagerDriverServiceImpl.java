package org.yihao.driverserver.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.yihao.driverserver.Exception.APIException;
import org.yihao.driverserver.Exception.ResourceNotFoundException;
import org.yihao.driverserver.specificaiton.DriverSpecification;
import org.yihao.shared.DTOS.*;
import org.yihao.shared.ENUMS.DriverStatus;
import org.yihao.driverserver.model.Delivery;
import org.yihao.driverserver.model.Driver;
import org.yihao.driverserver.repository.DeliveryRepository;
import org.yihao.driverserver.repository.DriverRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class ManagerDriverServiceImpl implements ManagerDriverService {
    private final DriverDeliveryService driverDeliveryService;
    private final DriverRepository driverRepository;
    private final DeliveryRepository deliveryRepository;
    private final ModelMapper modelMapper;

    public ManagerDriverServiceImpl(DriverRepository repository, ModelMapper mapper,
                                    DriverDeliveryService driverDeliveryService,
                                    DeliveryRepository deliveryRepository) {
        this.driverRepository = repository;
        this.modelMapper = mapper;
        this.driverDeliveryService = driverDeliveryService;
        this.deliveryRepository = deliveryRepository;
    }
    @Override
    public DriverResponse getAllDrivers(DriverStatus driverStatus, Integer pageNumber, Integer pageSize, String sortBy, boolean desc) {
        Sort sort = desc ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize,sort);
        Specification<Driver> specification = Specification.where(DriverSpecification.hasStatus(driverStatus));
        Page<Driver> driverInPage = driverRepository.findAll(specification,pageDetails);
        List<Driver> driverFound = driverInPage.getContent();
        if (driverFound.isEmpty()) {throw new APIException("No Driver registered");}
        List<DriverDTO> driverDTOS = driverFound.stream()
                .map(order -> modelMapper.map(order, DriverDTO.class)).toList();
        DriverResponse driverResponse = new DriverResponse();
        driverResponse.setContent(driverDTOS);
        driverResponse.setPageNumber(driverInPage.getNumber());
        driverResponse.setPageSize(driverInPage.getSize());
        driverResponse.setTotalPages(driverInPage.getTotalPages());
        driverResponse.setTotalElements(driverInPage.getTotalElements());
        driverResponse.setLastPage(driverInPage.isLast());
        return driverResponse;
    }

    @Override
    public DriverDTO getTheMostAvailableDriver() {
        //find the one that is IDLE first
        List<Driver> driverFound = driverRepository.findByDriverStatus(DriverStatus.IDLE);
        if (!driverFound.isEmpty()) {
            Random r = new Random();
            Driver randomDriver = driverFound.get(r.nextInt(driverFound.size()));
            return modelMapper.map(randomDriver,DriverDTO.class);
            //nobody is idle find the one with the least task
        } else{
            Long driverId = driverRepository.findDriverWithLeastActiveTasks();
            if(driverId == null){
                throw new APIException("No Driver Available now");
            }
            return this.getDriverById(driverId);
        }

    }

    @Override
    public DriverDTO updateDriverStatus(DriverStatusChangeRequest request) {
        Long id = request.getDriverId();
        Driver driverFound = driverRepository.findById(request.getDriverId())
                .orElseThrow(()->new ResourceNotFoundException("Driver not found", "DriverId", id));
        driverFound.setDriverStatus(request.getStatus());
        Driver updated = driverRepository.save(driverFound);
        return modelMapper.map(updated, DriverDTO.class);
    }


    @Override
    public DriverDTO getDriverById(Long id) {
        Driver driverFound = driverRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Driver not found", "DriverId", id));
        return modelMapper.map(driverFound, DriverDTO.class);
    }

    @Override
    public DriverDTO createDriver(DriverDTO driverDTO) {
        if (driverRepository.existsDriverByLicenseNumber(driverDTO.getLicenseNumber())){
            throw new APIException("Driver license already exists");
        }
        if(driverRepository.existsDriverByEmail(driverDTO.getEmail())){
            throw new APIException("Driver email already exists");
        }
        if(driverRepository.existsDriverByPhoneNumber(driverDTO.getPhoneNumber())){
            throw new APIException("Driver phone number already exists");
        }
        if(driverDTO.getLicenseExpiryDate().isBefore(LocalDate.now())){
            throw new APIException("License Expired, registration failed");
        }
        Driver driverToCreate = modelMapper.map(driverDTO, Driver.class);
        driverToCreate.setDriverStatus(DriverStatus.TRAINING);
        Driver saved = driverRepository.save(driverToCreate);
        return modelMapper.map(saved, DriverDTO.class);
    }

    @Override
    public DriverDTO updateById(Long driverId, DriverDTO driverDTO) {
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

    @Override
    public DriverDTO deleteById(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found", "DriverId", id));
        List<Delivery> deliveryByDriverId = deliveryRepository.findByDriverId(id) ;
        if(!deliveryByDriverId.isEmpty()){ throw new APIException("Cannot delete driver, he still has a task"); }
        driverRepository.deleteById(id);
        return modelMapper.map(driver, DriverDTO.class);
    }
}
