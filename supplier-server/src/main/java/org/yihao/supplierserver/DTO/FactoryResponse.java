package org.yihao.supplierserver.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactoryResponse {
    private Long factoryId;

    private String message;
}
