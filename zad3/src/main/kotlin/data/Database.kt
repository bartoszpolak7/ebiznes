package io.github.siemamen7.data

import io.github.siemamen7.Category
import io.github.siemamen7.Product

object Database {

    val categories = listOf(
        Category(1, "Dania"),
        Category(2, "Napitki"),
    )
    val products = listOf(
        Product(1, "Cienka zupa szczawiowa", 10.99, categoryId = 1),
        Product(2, "Szaszłyk z drobiu trolla", 20.99, categoryId = 1),
        Product(3, "Tur nadziewany mięsaczem (zestaw rodzinny)", 60.99, categoryId = 1),
        Product(3, "Wywar z okolumpa", 10.99, categoryId = 2),
        Product(3, "Białe uszy koboldzie posypane parmezanem", 10.99, categoryId = 1),
        Product(3, "Piwo", 5.99, categoryId = 2),
        Product(3, "Wino ze Złotej Przystani", 300.0, categoryId = 2),


    )
}