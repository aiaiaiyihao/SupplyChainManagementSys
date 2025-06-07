package org.yihao.supplierserver.Service;

import org.yihao.shared.DTOS.*;

public interface ManagerService {
    SupplierStatusDTO offBoardSupplierBySupplierId(Long supplierId);

    SupplierPageResponse getAllSuppliers(Integer pageNumber, Integer pageSize);

    SupplierDTO getSupplierById(Long supplierId);
}
