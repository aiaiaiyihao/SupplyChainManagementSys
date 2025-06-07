package org.yihao.productmanagementserver.service;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.yihao.productmanagementserver.Exception.APIException;
import org.yihao.productmanagementserver.service.remote.RemoteProductService;
import org.yihao.productmanagementserver.service.remote.RemoteSupplierService;
import org.yihao.shared.DTOS.FactoryDTO;
import org.yihao.shared.DTOS.ProductDTO;
import org.yihao.shared.ENUMS.FactoryStatus;
import org.yihao.shared.ENUMS.Role;

@Service
public class SupplierProductManServiceImpl implements SupplierProductManService {
    private final RemoteSupplierService remoteSupplierService;
    private final RemoteProductService remoteProductService;

    public SupplierProductManServiceImpl(
            RemoteSupplierService remoteSupplierService,
            RemoteProductService remoteProductService) {
        this.remoteSupplierService = remoteSupplierService;
        this.remoteProductService = remoteProductService;
    }

    @Override
    public ProductDTO createProduct(Long supplierId, ProductDTO productDTO, String role) {
        //1 check if factorId exists and belongs to supplierId
        Long factoryId = productDTO.getFactoryId();
        FactoryDTO factoryDTOFound = remoteSupplierService
                .findSupplierFactoryByFactoryId(supplierId, role, factoryId).getBody();
        if (factoryDTOFound == null) {
            throw new APIException("Supplier Factory with ID:" + factoryId + " Not Found");
        }
        if(factoryDTOFound.getFactoryStatus().equals(FactoryStatus.CLOSED)){
            throw new APIException("Supplier Factory with ID:" + factoryId + " is Closed");
        }

        //2 call product server to create
        ProductDTO productDTOCreated = remoteProductService
                .createProduct(supplierId, role, productDTO).getBody();
        if (productDTOCreated == null) {
            throw new APIException("Product Not Created Successfully");
        }
        return productDTOCreated;
    }
}
