package io.github.siemamen7.backend

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuth2SuccessHandler(
    private val userRepository: UserRepository,
    private val jwtService: JwtService
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oauthUser = authentication.principal as OAuth2User
        val email = oauthUser.attributes["email"] as? String
            ?: oauthUser.attributes["login"] as? String
            ?: throw RuntimeException("Email not found")

        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found after OAuth2 login")

        val token = jwtService.generateToken(user.id, user.email)

        response.sendRedirect("http://localhost:5173/oauth2/callback?token=$token")
    }
}