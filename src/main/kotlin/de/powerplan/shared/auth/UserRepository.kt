package de.powerplan.shared.auth

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserInfo
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Repository

@Repository
class UserRepository(private val supabaseClient: SupabaseClient) {

    suspend fun getUserInfo(jwtToken: String): UserInfo {
        try {
            supabaseClient.auth.importAuthToken(jwtToken);
            return supabaseClient.auth.retrieveUserForCurrentSession()
        } catch (e: Exception) {
            throw BadCredentialsException("Invalid token", e)
        }
    }
}
