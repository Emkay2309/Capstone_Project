package com.capstone.backend.config;


import com.capstone.backend.services.FileService;
import com.capstone.backend.services.implementations.FileServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfig {

    @Bean
    public ModelMapper mapper() {
        return new ModelMapper();
    }

    @Bean
    public FileService fileService() {
        return new FileServiceImpl(); // Your implementation class
    }

}
