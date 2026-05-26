package io.github.siemamen7.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.*

fun main(args: Array<String>) {
    runApplication<Server>(*args)
}

// 1. Define your data structures
data class Product(val id: Int, val name: String, val price: Double)

@SpringBootApplication
@RestController
@CrossOrigin(origins = ["http://localhost:5173"]) // Replaces app.use(cors())
class Server {

    private val products = listOf(
        Product(1, "Dziadek do orzechów", 29.99),
        Product(2, "Lego Ninjago", 49.99),
        Product(3, "Buty na rzepy", 99.99)
    )

    @GetMapping("/products")
    fun getProducts(): List<Product> {
        return products
    }

    @PostMapping("/payments")
    fun processPayment(@RequestBody paymentData: Map<String, Any>): Map<String, String> {
        println("Payment: $paymentData")

        return mapOf("status" to "ok")
    }
}