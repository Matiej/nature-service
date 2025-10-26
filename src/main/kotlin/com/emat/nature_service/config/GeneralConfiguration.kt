package com.emat.nature_service.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GeneralConfiguration {

    @Bean
    fun appData(): AppData {
        return AppData()
    }
}