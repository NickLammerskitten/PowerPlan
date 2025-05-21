package de.powerplan.shared.auth

import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Service

@Service
class JwtTokenProvider {

    /** Extracts “Bearer …” token from the Authorization header */
    fun resolveToken(request: HttpServletRequest): String? {
        val bearer = request.getHeader("Authorization") ?: return null
        return bearer.removePrefix("Bearer ").takeIf(String::isNotBlank)
    }
}