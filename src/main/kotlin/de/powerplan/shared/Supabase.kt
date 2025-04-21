package de.powerplan.shared

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.PropertyConversionMethod
import org.springframework.stereotype.Component

@Component
class Supabase {

    private val supabase = createSupabaseClient(
        supabaseUrl = "",
        supabaseKey = "",
    ) {
        install(Postgrest) {
            defaultSchema = "public"
            propertyConversionMethod = PropertyConversionMethod.CAMEL_CASE_TO_SNAKE_CASE
        }
    }

    fun getSupabaseClient(): SupabaseClient {
        return supabase
    }
}