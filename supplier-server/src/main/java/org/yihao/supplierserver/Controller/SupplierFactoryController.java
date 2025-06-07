package org.yihao.supplierserver.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yihao.shared.DTOS.*;
import org.yihao.shared.ENUMS.Role;
import org.yihao.shared.config.AppConstants;
import org.yihao.supplierserver.Exception.APIException;
import org.yihao.supplierserver.Service.SupplierFactoryService;

@RestController
@RequestMapping("/supplier/factories")
public class SupplierFactoryController {
    private final SupplierFactoryService factoryService;
    public SupplierFactoryController(SupplierFactoryService factoryService) {
        this.factoryService = factoryService;
    }

    //pageable
    @GetMapping
    public ResponseEntity<FactoryPageResponse> findSupplierFactoriesBySupplierId(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestHeader("tableId") Long supplierId,@RequestHeader("role") String role) {
        if(!role.equals(Role.SUPPLIER.name())){
            throw new APIException("ONLY SUPPLIER CAN FIND ITS FACTORIES");
        }
        FactoryPageResponse factoryDTOFound = factoryService.findSupplierFactoriesBySupplierId(pageNumber, pageSize, supplierId);
        return new ResponseEntity<>(factoryDTOFound, HttpStatus.OK);
    }

    @GetMapping("/{factoryId}")
    public ResponseEntity<FactoryDTO> findSupplierFactoryByFactoryId(
            @RequestHeader("tableId") Long supplierId,@RequestHeader("role") String role,
            @PathVariable Long factoryId
    ){
        if(!role.equals(Role.SUPPLIER.name())){
            throw new APIException("ONLY SUPPLIER CAN FIND ITS FACTORY By Id");
        }
        FactoryDTO factoryDTO = factoryService.findSupplierFactoryByFactoryId(supplierId,factoryId);
        return new ResponseEntity<>(factoryDTO, HttpStatus.OK);
    }

    //    @PostMapping("/factories")
//    public ResponseEntity<SupplierFactoryDTO> createSupplierFactories(@Valid @RequestBody SupplierFactoryDTO supplierFactoryDTO){
//        SupplierFactoryDTO supplierFactoryDTOResponse = supplierService.createSupplierFactories(supplierFactoryDTO);
//        return new ResponseEntity<>(supplierFactoryDTOResponse, HttpStatus.CREATED);
//    }
    @PostMapping
    public ResponseEntity<FactoryDTO> createFactoryById(@RequestHeader("tableId") Long supplierId,
            @RequestBody FactoryDTO factoryDTO,@RequestHeader("role") String role) {
        if(!role.equals(Role.SUPPLIER.name())){
            throw new APIException("ONLY SUPPLIER CAN CREATE ITS FACTORIES");
        }
        FactoryDTO factoryDTOCreated = factoryService.createFactoryById(supplierId,factoryDTO);
        return new ResponseEntity<>(factoryDTOCreated, HttpStatus.CREATED);
    }

    @PutMapping("/{factoryId}")
    public ResponseEntity<FactoryDTO> updateSupplierFactoriesBySupplierId(
            @RequestHeader("tableId") Long supplierId, @RequestHeader("role") String role,
            @PathVariable("factoryId") Long factoryId, @RequestBody FactoryDTO factoryDTO){
        if(!role.equals(Role.SUPPLIER.name())){
            throw new APIException("ONLY SUPPLIER CAN UPDATE ITS FACTORIES");
        }
        FactoryDTO factoryDTOUpdated = factoryService.updateFactoryById(supplierId,factoryId,factoryDTO);
        return new ResponseEntity<>(factoryDTOUpdated, HttpStatus.OK);
    }

    @DeleteMapping("/{factoryId}")
    public ResponseEntity<FactoryDTO> deleteSupplierFactoriesBySupplierId
            ( @RequestHeader("tableId") Long supplierId, @RequestHeader("role") String role,
              @PathVariable("factoryId") Long factoryId) {
        if(!role.equals(Role.SUPPLIER.name())){
            throw new APIException("ONLY SUPPLIER CAN DELETE ITS FACTORIES");
        }
        FactoryDTO supplierFactoryDTOResponse = factoryService.deleteFactoryById(supplierId, factoryId);
        return new ResponseEntity<>(supplierFactoryDTOResponse, HttpStatus.OK);
    }
}
