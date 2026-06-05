package io.github.siemamen7.backend

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.OncePerRequestFilter

@Configuration
class SecurityConfig(
    private val customOAuth2UserService: CustomOAuth2UserService,
    private val oauth2SuccessHandler: OAuth2SuccessHandler,
    private val jwtAuthFilter: JwtAuthFilter,
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { }
            .csrf { it.disable() }
            .formLogin { it.disable() }   // disables the auto-generated HTML login page
            .httpBasic { it.disable() }   // disables basic auth popup
            .authorizeHttpRequests {
                it.requestMatchers("/login", "/register", "/health", "/oauth2/**", "/login/oauth2/**").permitAll()
                it.anyRequest().authenticated()  // ← now actually protects routes
            }
            .oauth2Login {
                it.userInfoEndpoint { u -> u.userService(customOAuth2UserService) }
                it.successHandler(oauth2SuccessHandler)
            }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)


        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): UrlBasedCorsConfigurationSource {
        val config = CorsConfiguration()
        config.allowedOrigins = listOf("http://localhost:5173")
        config.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        config.allowedHeaders = listOf("*")
        val source = org.springframework.web.cors.UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        return source
    }
}

@Component
class JwtAuthFilter(private val jwtService: JwtService) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val header = request.getHeader("Authorization")
        if (header != null && header.startsWith("Bearer ")) {
            val token = header.substring(7)
            if (jwtService.validateToken(token)) {
                val email = jwtService.getEmailFromToken(token)
                val auth = UsernamePasswordAuthenticationToken(email, null, emptyList())
                SecurityContextHolder.getContext().authentication = auth
            }
        }
        chain.doFilter(request, response)
    }
}