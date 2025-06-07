package org.yihao.productmanagementserver.service;

import org.springframework.stereotype.Service;
import org.yihao.productmanagementserver.Exception.APIException;
import org.yihao.productmanagementserver.service.remote.RemoteFacilityService;
import org.yihao.productmanagementserver.service.remote.RemoteProductService;
import org.yihao.shared.DTOS.ProductDTO;
import org.yihao.shared.DTOS.WarehouseDTO;
import org.yihao.shared.ENUMS.WarehouseStatus;

@Service
public class AdminProductManagementServiceImpl implements AdminProductManagementService {
    private final RemoteFacilityService remoteFacilityService;
    private final RemoteProductService remoteProductService;
    public AdminProductManagementServiceImpl(
            RemoteFacilityService remoteFacilityService,RemoteProductService remoteProductService) {
        this.remoteFacilityService = remoteFacilityService;
        this.remoteProductService = remoteProductService;
    }
    @Override
    public ProductDTO updateProductWarehouseId(String role, Long productId, ProductDTO productDTO) {
        if(productDTO.getWarehouseId() == null){
            throw new APIException("Product Warehouse Id is null, It can't be updated");
        }
        Long warehouseId = productDTO.getWarehouseId();
        WarehouseDTO warehouseDTO = remoteFacilityService.getWarehouseById(role, warehouseId).getBody();
        if(warehouseDTO == null){
            throw new APIException("Warehouse Does Not Exist");
        }
        if(!warehouseDTO.getStatus().equals(WarehouseStatus.OPERATIONAL)){
            throw new APIException("Warehouse Isn't Operational, you can't store this product there");
        }
        ProductDTO productDTOUpdated = remoteProductService.updateProductById(role, productId, productDTO).getBody();
        if(productDTOUpdated==null){
            throw new APIException("Update Product failed");
        }
        return productDTOUpdated;
    }
}
