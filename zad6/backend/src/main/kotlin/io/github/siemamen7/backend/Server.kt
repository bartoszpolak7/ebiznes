package io.github.siemamen7.backend

import jakarta.validation.Valid
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.client.HttpStatusCodeException

fun main(args: Array<String>) {
    runApplication<Server>(*args)
}

@SpringBootApplication
@RestController
@CrossOrigin(origins = ["http://localhost:5173"])
class Server(
    private val productRepository: ProductRepository,
    private val paymentRepository: PaymentRepository,
    private val userRepository: UserRepository,
    private val jwtService: JwtService,
) {
    // seeds the database on startup
    @Bean
    fun seedProducts(): CommandLineRunner = CommandLineRunner {
        if (productRepository.count() == 0L) {
            productRepository.saveAll(
                listOf(
                    Product(name = "Dziadek do orzechów", price = 29.99),
                    Product(name = "Lego Ninjago", price = 49.99),
                    Product(name = "Buty na rzepy", price = 99.99),
                )
            )
        }
    }

    @GetMapping("/products")
    fun getProducts(): List<Product> = productRepository.findAll()

    @PostMapping("/payments")
    fun processPayment(@Valid @RequestBody request: PaymentRequest): ResponseEntity<Map<String, String>> {
        val payment = Payment(total = request.total)
        paymentRepository.save(payment)
        return ResponseEntity.ok().body(mapOf("status" to "ok", "message" to "payment processed succesfully"))
    }

    @PostMapping("/register")
    fun registerUser(@RequestBody body: Map<String, String>): ResponseEntity<Map<String, String>> {
        val email = body["email"] ?: return ResponseEntity.badRequest()
            .body(mapOf("status" to "error", "message" to "email is required"))
        val password = body["password"] ?: return ResponseEntity.badRequest()
            .body(mapOf("status" to "error", "message" to "password is required"))
        if (userRepository.findByEmail(email) != null) {
            return ResponseEntity.badRequest().body(mapOf("error" to "email already in use"))
        }

        val hashed = BCryptPasswordEncoder().encode(password) ?: return ResponseEntity.badRequest()
            .body(mapOf("status" to "error", "message" to "password could not be encoded"))
        userRepository.save(User(email = email, password = hashed))

        return ResponseEntity.ok().body(mapOf("status" to "ok", "message" to "user registered successfully"))
    }

    @PostMapping("/login")
    fun loginUser(@RequestBody body: Map<String, String>): ResponseEntity<Map<String, String>> {
        val email = body["email"] ?: return ResponseEntity.badRequest().body(mapOf("error" to "email is required"))
        val password =
            body["password"] ?: return ResponseEntity.badRequest().body(mapOf("error" to "password is required"))

        val user = userRepository.findByEmail(email)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("error" to "invalid credentials"))

        if (user.password == null || !BCryptPasswordEncoder().matches(password, user.password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(mapOf("error" to "invalid credentials"))
        }

        val token = jwtService.generateToken(user.id, user.email)  // ← real token
        return ResponseEntity.ok().body(mapOf("status" to "ok", "token" to token))
    }


    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationErrors(): ResponseEntity<Map<String, String>> =
        ResponseEntity.badRequest().body(mapOf("status" to "error", "message" to "invalid payment payload"))

    @GetMapping("/health")
    fun health() = ResponseEntity.ok().body(mapOf("status" to "ok"))
}
