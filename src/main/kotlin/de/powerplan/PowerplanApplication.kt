package de.powerplan

import de.powerplan.shared.PowerplanDataSourceProperties
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@EnableWebSecurity
@SpringBootApplication
@EnableConfigurationProperties(value = [PowerplanDataSourceProperties::class])
@OpenAPIDefinition(
    info = Info(title = "API Documentation of the Powerplan open source project", version = "1.0"),
    tags = [
        Tag(name = "Exercises"),
        Tag(name = "Plans"),
        Tag(name = "Plan Weeks", description = "Operations for managing weeks in plans"),
        Tag(name = "Plan Days", description = "Operations for managing days in plan weeks"),
        Tag(name = "Plan Exercises", description = "Operations for managing exercises in plan days"),
        Tag(name = "Plan Exercise Sets", description = "Operations for managing sets in plan exercises"),
        Tag(name = "Workout Sessions"),
    ]
)
class PowerplanApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<PowerplanApplication>(*args)
}
