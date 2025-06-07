package org.yihao.shared.DTOS;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yihao.shared.ENUMS.FactoryStatus;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from JSON
public class FactoryDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long factoryId;

    private Long supplierId;

    private String factoryName;

    private Address factoryAddress;

    private FactoryStatus factoryStatus;

    private String factoryImagesUrl;
}
