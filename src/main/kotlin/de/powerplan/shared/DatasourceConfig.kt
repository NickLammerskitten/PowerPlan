package de.powerplan.shared

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.PropertyConversionMethod
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DatasourceConfig(properties: PowerplanDataSourceProperties) {

    private val supabase = createSupabaseClient(
        supabaseUrl = properties.url,
        supabaseKey = properties.key,
    ) {
        install(Postgrest) {
            defaultSchema = properties.schema
            propertyConversionMethod = PropertyConversionMethod.CAMEL_CASE_TO_SNAKE_CASE
        }

        install(Auth) {
            alwaysAutoRefresh = true
            enableLifecycleCallbacks = false
        }
    }

    @Bean
    fun dataSource(): SupabaseClient {
        return supabase
    }
}

@ConfigurationProperties(prefix = "spring.supabase")
class PowerplanDataSourceProperties {

    lateinit var url: String

    lateinit var key: String

    lateinit var schema: String
}