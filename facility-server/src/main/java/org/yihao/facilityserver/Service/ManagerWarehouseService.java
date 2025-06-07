package org.yihao.facilityserver.Service;

import org.yihao.shared.DTOS.WarehouseDTO;
import org.yihao.shared.DTOS.WarehouseResponse;
import org.yihao.shared.ENUMS.WarehouseStatus;

public interface ManagerWarehouseService{
    WarehouseResponse getAllWarehouses(Integer pageNumber, Integer pageSize, WarehouseStatus status, Long zipCode, String sortBy, boolean desc);

    WarehouseDTO getWarehouseById(Long warehouseId);

    WarehouseDTO createWarehouse(WarehouseDTO warehouseDTO);

    WarehouseDTO updateWarehouse(Long warehouseId, WarehouseDTO warehouseDTO);

    WarehouseDTO deleteWarehouse(Long warehouseId);

    WarehouseDTO changeWarehouseStatus(WarehouseDTO warehouseDTO);
}
