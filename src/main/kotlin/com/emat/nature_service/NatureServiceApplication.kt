package com.emat.nature_service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class NatureServiceApplication

fun main(args: Array<String>) {
	runApplication<NatureServiceApplication>(*args)
}
