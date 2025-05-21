package de.powerplan

import de.powerplan.shared.PowerplanDataSourceProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@EnableWebSecurity
@SpringBootApplication
@EnableConfigurationProperties(value = [PowerplanDataSourceProperties::class])
class PowerplanApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<PowerplanApplication>(*args)
}
