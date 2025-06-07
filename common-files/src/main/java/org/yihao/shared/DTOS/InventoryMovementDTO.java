package org.yihao.shared.DTOS;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yihao.shared.ENUMS.MovementType;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InventoryMovementDTO {
    private Long movementId;

    private Long inventoryId;   // Optional: link to inventory
    private Long productId;
    private String productName;
    private Long warehouseId;

    private Integer quantity;

    private MovementType movementType; // INBOUND or OUTBOUND
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}