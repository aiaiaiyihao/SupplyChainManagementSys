package org.yihao.shared.DTOS;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yihao.shared.ENUMS.WarehouseStatus;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WarehouseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long warehouseId;

    private String warehouseName;

    private Address address;

    private WarehouseStatus status;

    private String contactNumber;



}
