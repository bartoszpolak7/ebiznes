package io.github.siemamen7.data

object Database {

    data class Category(val id: Int, val name: String)
    data class Product(val id: Int, val name: String, val price: Double, val categoryId: Int)

    val categories = listOf(
        Category(1, "Zbroja"),
        Category(2, "Broń"),
    )
    val products = listOf(
        Product(1, "Miecz krótki", 2999.99, categoryId = 2),
        Product(2, "Macuahuitl", 5999.99, categoryId = 2),
        Product(3, "Napierśnik", 1999.99, categoryId = 1),
    )
}