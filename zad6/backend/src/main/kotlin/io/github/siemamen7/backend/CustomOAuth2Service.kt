package io.github.siemamen7.backend

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService(
    private val userRepository: UserRepository,
    private val oauth2AccountRepository: OAuth2AccountRepository
) : DefaultOAuth2UserService() {

    override fun loadUser(request: OAuth2UserRequest): OAuth2User {
        val oauthUser = super.loadUser(request)
        val provider = request.clientRegistration.registrationId  // "google" or "github"
        val accessToken = request.accessToken.tokenValue

        val email = oauthUser.attributes["email"] as? String
            ?: oauthUser.attributes["login"] as? String  // github fallback
            ?: throw RuntimeException("Email not found from provider")

        val providerUserId = oauthUser.attributes["sub"] as? String  // google
            ?: oauthUser.attributes["id"]?.toString()               // github

        // find or create user
        val user = userRepository.findByEmail(email) ?: userRepository.save(
            User(email = email, provider = provider)
        )

        // save or update OAuth2 account
        val existing = providerUserId?.let {
            oauth2AccountRepository.findByProviderAndProviderUserId(provider, it)
        }

        if (existing == null && providerUserId != null) {
            oauth2AccountRepository.save(
                OAuth2Account(
                    provider = provider,
                    providerUserId = providerUserId,
                    accessToken = accessToken,
                    email = email,
                    user = user
                )
            )
        }

        return oauthUser
    }
}