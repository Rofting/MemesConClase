package org.svalero.memesconclase.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.svalero.memesconclase.domain.Publication;
import org.svalero.memesconclase.domain.dto.PublicationInDto;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(PublicationInDto.class, Publication.class)
                .addMappings(mapper -> mapper.skip(Publication::setId));

        return modelMapper;
    }
}