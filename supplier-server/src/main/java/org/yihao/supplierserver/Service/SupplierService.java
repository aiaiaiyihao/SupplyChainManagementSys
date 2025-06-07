package org.yihao.supplierserver.Service;

import jakarta.validation.Valid;
import org.yihao.shared.DTOS.*;

import java.util.List;

public interface SupplierService {
    SupplierDTO createSupplierProfile(SupplierDTO supplierDTO);

    SupplierDTO getSupplierProfileBySupplierId(Long supplierId);

    SupplierDTO updateSupplierProfileBySupplierId(SupplierDTO supplierDTO, Long supplierId);

    SupplierDTO deleteSupplierProfileBySupplierId(Long supplierId);


}

