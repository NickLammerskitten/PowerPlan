package de.powerplan

import de.powerplan.shared.PowerplanDataSourceProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(value = [PowerplanDataSourceProperties::class])
class PowerplanApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<PowerplanApplication>(*args)
}
