package eu.domibus.ep.edelivery.plugin.rest.configuration;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Sets;
import eu.domibus.ep.edelivery.plugin.rest.controller.RestBackendPlugin;
import eu.domibus.ep.edelivery.plugin.rest.dto.SubmitMessage;
//import org.apache.xml.resolver.apps.resolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {

    @Autowired
    private TypeResolver typeResolver;

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select().apis(RequestHandlerSelectors.basePackage("eu.domibus.ep.edelivery.plugin.rest.controller"))
                .paths(regex("/ext/api/v1/ep.*"))
                .build()
                .protocols(Sets.newHashSet("http"))
                .additionalModels(typeResolver.resolve(SubmitMessage.class))
                .apiInfo(metaData())
                .securityContexts(Arrays.asList(actuatorSecurityContext()))
                .securitySchemes(Arrays.asList(basicAuthScheme()));

    }
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("Rest Back-end Plugin API Documentation")
                .description("\"Back-end plugin representing communication between corner 1 and corner 2\"")
                .version("1.0.0")
                .license("European Parliament")
                .licenseUrl("https://www.europarl.europa.eu\"")
                .contact(new Contact("DG ITEC CORPORATE IT System", "https://www.europarl.europa.eu", "ITECServiceDesk@europarl.europa.eu"))
                .build();

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/ext/api/v1/ep/**")
                .allowedMethods("GET","POST");
    }

    private SecurityContext actuatorSecurityContext() {
        return SecurityContext.builder()
                .securityReferences(Arrays.asList(basicAuthReference()))
                .forPaths(PathSelectors.ant("/actuator/**"))
                .build();
    }

    private SecurityScheme basicAuthScheme() {
        return new BasicAuth("basicAuth");
    }

    private SecurityReference basicAuthReference() {
        return new SecurityReference("basicAuth", new AuthorizationScope[0]);
    }
}
