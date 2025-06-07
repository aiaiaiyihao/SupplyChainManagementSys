package org.yihao.supplierserver.Service;

import org.yihao.shared.DTOS.Address;
import org.yihao.shared.DTOS.FactoryPageResponse;
import org.yihao.shared.DTOS.*;

public interface SupplierFactoryService {
    FactoryPageResponse findSupplierFactoriesBySupplierId(Integer pageNumber, Integer pageSize, Long supplierId);

    FactoryDTO deleteFactoryById(Long supplierId, Long factoryId);

    FactoryDTO updateFactoryById(Long supplierId, Long factoryId, FactoryDTO factoryDTO);

    FactoryDTO createFactoryById(Long supplierId, FactoryDTO factoryDTO);

    Address findFactoryAddressByFactoryId(Long supplierId, Long factoryId);

    FactoryPageResponse getAllFactories(Integer pageNumber, Integer pageSize);

    FactoryDTO findSupplierFactoryByFactoryId(Long supplierId, Long factoryId);
}
