package io.github.siemamen7.backend

import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

fun main(args: Array<String>) {
    runApplication<Server>(*args)
}

data class Product(val id: Int, val name: String, val price: Double)

data class PaymentRequest(
    @field:Positive(message = "total must be greater than 0")
    val total: Double
)

data class PaymentResponse(val status: String)

class ProductCatalog {
    private val products = listOf(
        Product(1, "Dziadek do orzechów", 29.99),
        Product(2, "Lego Ninjago", 49.99),
        Product(3, "Buty na rzepy", 99.99)
    )

    fun getProducts(): List<Product> = products

    fun findById(id: Int): Product? = products.find { it.id == id }
}

class PaymentService {
    fun processPayment(request: PaymentRequest): PaymentResponse {
        return PaymentResponse(status = "ok")
    }
}

@SpringBootApplication
@RestController
@CrossOrigin(origins = ["http://localhost:5173"])
class Server(
    private val productCatalog: ProductCatalog = ProductCatalog(),
    private val paymentService: PaymentService = PaymentService()
) {

    @GetMapping("/products")
    fun getProducts(): List<Product> = productCatalog.getProducts()

    @PostMapping("/payments")
    fun processPayment(@Valid @RequestBody paymentRequest: PaymentRequest): PaymentResponse {
        return paymentService.processPayment(paymentRequest)
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationErrors(): Map<String, String> =
        mapOf("status" to "error", "message" to "invalid payment payload")
}
