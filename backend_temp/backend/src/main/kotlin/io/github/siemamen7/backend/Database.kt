package io.github.siemamen7.backend

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Table
import jakarta.persistence.Id
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import org.springframework.data.jpa.repository.JpaRepository

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val email: String,
    val password: String? = null,
    val provider: String = "local",
    val providerUserId: String? = null,
)

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
}

@Entity
@Table(name = "products")
data class Product(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    @field:Positive(message = "price of a product must be greater than 0")
    val price: Double,
)

interface ProductRepository : JpaRepository<Product, Long> {}

data class ProductRequest(
    @field:NotBlank val name: String,
    @field:Positive val price: Double,
)

@Entity
@Table(name = "payments")
data class Payment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val total: Double
)

interface PaymentRepository : JpaRepository<Payment, Long> {}

data class PaymentRequest(
    @field:Positive(message = "total must be greater than 0")
    val total: Double
)

