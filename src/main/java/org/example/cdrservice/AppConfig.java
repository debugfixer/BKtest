package org.example.cdrservice;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Конфигурация для доступа к сгенерированным отчетам.
 */
@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path reportsDirectory = Paths.get("reports").toAbsolutePath();
        registry.addResourceHandler("/reports/**")
                .addResourceLocations(reportsDirectory.toUri().toString());
    }
}