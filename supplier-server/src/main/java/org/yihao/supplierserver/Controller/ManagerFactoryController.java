package org.yihao.supplierserver.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yihao.shared.DTOS.Address;
import org.yihao.shared.DTOS.*;
import org.yihao.shared.ENUMS.Role;
import org.yihao.shared.config.AppConstants;

import org.yihao.supplierserver.Exception.APIException;
import org.yihao.supplierserver.Service.SupplierFactoryService;

@RestController
@RequestMapping("/admin/suppliers/{supplierId}/factories")
public class ManagerFactoryController {
    private final SupplierFactoryService factoryService;

    public ManagerFactoryController(SupplierFactoryService factoryService) {
        this.factoryService = factoryService;
    }

    //pageable
    @GetMapping("/all")
    public ResponseEntity<FactoryPageResponse> getAllFactories(
            @RequestHeader("role") String role,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize
    ) {
        if(!role.equals(Role.MANAGER.name())){
            throw new APIException("Only Manager Can Get All Supplier Factory...");
        }
        FactoryPageResponse factoryPageResponse =  factoryService.getAllFactories(pageNumber,pageSize);
        return new ResponseEntity<>(factoryPageResponse, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<FactoryPageResponse> findSupplierFactoriesBySupplierId(
            @RequestHeader("role") String role,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @PathVariable("supplierId") Long supplierId) {
        if(!role.equals(Role.MANAGER.name())){
            throw new APIException("Only Manager Can Find Supplier Factories...");
        }
        FactoryPageResponse factoryDTOFound = factoryService.findSupplierFactoriesBySupplierId(pageNumber,pageSize,supplierId);
        return new ResponseEntity<>(factoryDTOFound, HttpStatus.OK);
    }

    //    @PostMapping("/factories")
//    public ResponseEntity<SupplierFactoryDTO> createSupplierFactories(@Valid @RequestBody SupplierFactoryDTO supplierFactoryDTO){
//        SupplierFactoryDTO supplierFactoryDTOResponse = supplierService.createSupplierFactories(supplierFactoryDTO);
//        return new ResponseEntity<>(supplierFactoryDTOResponse, HttpStatus.CREATED);
//    }

    @GetMapping("{factoryId}/address")
    public ResponseEntity<Address> findAddressByFactoryId(
            @RequestHeader("role") String role,
            @PathVariable("supplierId") Long supplierId,
            @PathVariable("factoryId") Long factoryId) {
        if(!role.equals(Role.MANAGER.name())){
            throw new APIException("Only Manager Can Find Supplier Factory Address...");
        }
        Address addressDTO = factoryService.findFactoryAddressByFactoryId(supplierId,factoryId);
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<FactoryDTO> createFactoryForSupplier(
            @RequestHeader("role") String role,
            @PathVariable("supplierId") Long supplierId
            ,@RequestBody FactoryDTO factoryDTO) {
        if(!role.equals(Role.MANAGER.name())){
            throw new APIException("Only Manager Can Create Supplier Factory...");
        }
        FactoryDTO factoryDTOCreated = factoryService.createFactoryById(supplierId,factoryDTO);
        return new ResponseEntity<>(factoryDTOCreated, HttpStatus.CREATED);
    }

    @PutMapping("/{factoryId}")
    public ResponseEntity<FactoryDTO> updateSupplierFactoriesBySupplierId(
            @RequestHeader("role") String role, @PathVariable("supplierId") Long supplierId
            , @PathVariable("factoryId") Long factoryId, @RequestBody FactoryDTO factoryDTO){
        if(!role.equals(Role.MANAGER.name())){
            throw new APIException("Only Manager Can Update Supplier Factory...");
        }
        FactoryDTO factoryDTOUpdated = factoryService.updateFactoryById(supplierId,factoryId,factoryDTO);
        return new ResponseEntity<>(factoryDTOUpdated, HttpStatus.OK);
    }

    @DeleteMapping("{factoryId}")
    public ResponseEntity<FactoryDTO> deleteSupplierFactoriesBySupplierId(
            @RequestHeader("role") String role,@PathVariable("supplierId") Long supplierId,
            @PathVariable("factoryId") Long factoryId) {
        if(!role.equals(Role.MANAGER.name())){
            throw new APIException("Only Manager Can Delete Supplier Factory...");
        }
        FactoryDTO supplierFactoryDTOResponse = factoryService.deleteFactoryById(supplierId, factoryId);
        return new ResponseEntity<>(supplierFactoryDTOResponse, HttpStatus.OK);
    }
}
