package org.yihao.shared.DTOS;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON
public class InventoryDTO {
    private Long inventoryId;

    private Long productId;      // From Product Service
    private Long warehouseId;    // From Facility Server

    private String productName;  // Optional snapshot

    private Integer quantity;

    private LocalDateTime lastUpdated;
}
