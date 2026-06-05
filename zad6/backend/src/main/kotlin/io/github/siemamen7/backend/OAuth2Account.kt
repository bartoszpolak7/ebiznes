package io.github.siemamen7.backend

import jakarta.persistence.*

@Entity
@Table(name = "oauth2_accounts")
data class OAuth2Account(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val provider: String,
    val providerUserId: String,
    val accessToken: String,
    val email: String,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User
)

interface OAuth2AccountRepository : org.springframework.data.jpa.repository.JpaRepository<OAuth2Account, Long> {
    fun findByProviderAndProviderUserId(provider: String, providerUserId: String): OAuth2Account?
}