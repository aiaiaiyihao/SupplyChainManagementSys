package org.yihao.inventoryserver.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"productId", "warehouseId"}))
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryId;

    private Long productId;      // From Product Service
    private Long warehouseId;    // From Facility Server

    private String productName;  // Optional snapshot

    private Integer quantity;

    private LocalDateTime createdAt;
    private LocalDateTime lastUpdated;
}
