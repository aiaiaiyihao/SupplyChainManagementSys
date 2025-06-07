package org.yihao.inventoryserver.config;


import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yihao.inventoryserver.model.InventoryMovement;
import org.yihao.shared.DTOS.InventoryMovementDTO;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<InventoryMovement, InventoryMovementDTO>() {
            @Override
            protected void configure() {
                map().setInventoryId(source.getInventory().getInventoryId());
                map().setProductId(source.getInventory().getProductId());
                map().setProductName(source.getInventory().getProductName());
                map().setWarehouseId(source.getInventory().getWarehouseId());
            }
        });

        return modelMapper;
    }
}
