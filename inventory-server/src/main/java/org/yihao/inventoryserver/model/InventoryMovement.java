package org.yihao.inventoryserver.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.yihao.shared.ENUMS.MovementType;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(exclude = "inventory")
public class InventoryMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movementId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="inventory_id")
    private Inventory inventory;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private MovementType movementType; // INBOUND or OUTBOUND

    private String reason;             // e.g. "new delivery", "defective return"
    private LocalDateTime createdAt;
    private LocalDateTime UpdatedAt;
}
