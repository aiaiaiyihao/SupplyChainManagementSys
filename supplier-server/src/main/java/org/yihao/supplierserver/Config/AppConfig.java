package org.yihao.supplierserver.Config;


import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yihao.supplierserver.Model.Factory;
import org.yihao.shared.DTOS.FactoryDTO;


@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();

        TypeMap<Factory, FactoryDTO> typeMap = modelMapper.createTypeMap(Factory.class, FactoryDTO.class);
        typeMap.addMappings(mapper ->
                mapper.map(src -> src.getSupplier().getSupplierId(), FactoryDTO::setSupplierId)
        );
        return new ModelMapper();
    }
}
