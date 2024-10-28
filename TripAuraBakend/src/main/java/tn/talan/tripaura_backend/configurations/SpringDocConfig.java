package tn.talan.tripaura_backend.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(infoAPI());
    }

    public io.swagger.v3.oas.models.info.Info infoAPI() {
        return new Info().title("Talan-project")
                .description("agence Voyage ")
                .contact(contactAPI())
                .version("1");
    }

    public io.swagger.v3.oas.models.info.Contact contactAPI() {
        return new Contact().name("Equipe ASI II");
    }

    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("tn.talan.tripaura_backend"))
                .paths(PathSelectors.any())
                .build();
    }
}