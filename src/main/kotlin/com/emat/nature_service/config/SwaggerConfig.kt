package com.emat.nature_service.config

import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig(
    @Autowired private val appData: AppData
) {

    @Bean
    fun openApi(): OpenAPI = OpenAPI()
        .info(
            Info()
                .title("NATURE SERVICE")
                .description("API for synchronise data from weather and air condition services like GIOS")
                .contact(Contact().name("Maciej Wójcik").email("myEmail@gmail.com"))
                .license(License().name("Apache 2.0").url("http://springdoc.org"))
        ).externalDocs(
            ExternalDocumentation()
                .description("BookApp store Wiki Documentation")
                .url("https://springshop.wiki.github.org/docs")
        )
}