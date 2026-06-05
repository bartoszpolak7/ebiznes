package io.github.siemamen7.backend

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date

@Service
class JwtService (
    @Value($$"${jwt.secret}") private val secret: String
) {
    private val key = Keys.hmacShaKeyFor(secret.toByteArray())
    private val expiration = 1000 * 60 * 60 * 24 // 24 hours

    fun generateToken(userId: Long, email: String): String {
        return Jwts.builder()
            .subject(email)
            .claim("userId", userId)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + expiration))
            .signWith(key)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getEmailFromToken(token: String): String {
        return Jwts.parser().verifyWith(key).build()
            .parseSignedClaims(token).payload.subject
    }
}