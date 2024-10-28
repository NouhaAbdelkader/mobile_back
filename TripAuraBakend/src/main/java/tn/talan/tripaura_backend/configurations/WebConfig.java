package tn.talan.tripaura_backend.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tn.talan.tripaura_backend.helpers.Conversions;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final Conversions.StringToFlightClassConverter stringToFlightClassConverter;
    private final Conversions.StringToFlightTypeConverter stringToFlightTypeConverter;

    public WebConfig(Conversions.StringToFlightClassConverter stringToFlightClassConverter, Conversions.StringToFlightTypeConverter stringToFlightTypeConverter) {
        this.stringToFlightClassConverter = stringToFlightClassConverter;
        this.stringToFlightTypeConverter = stringToFlightTypeConverter;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToFlightClassConverter);
        registry.addConverter(stringToFlightTypeConverter);
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
              //  .allowedOrigins("http://localhost:4200" ) // Ajoutez ici toutes les origines autorisées
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedOrigins("http://172.16.2.93:8081")// Émulateur Android

                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .allowCredentials(true);

    }


    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer
                .setUseTrailingSlashMatch(false) // Permet de gérer les routes paramétrées sans barre oblique à la fin
                .setUseSuffixPatternMatch(false); // Permet de gérer les extensions de fichier dans les URL (par
                                                  // exemple, .html)
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
