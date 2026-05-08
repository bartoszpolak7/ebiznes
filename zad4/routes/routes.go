package routes

import (
	"zad4/handlers"

	"github.com/labstack/echo/v5"
)

func SetupRoutes(e *echo.Echo,
	cartHandler *handlers.CartHandler,
	productHandler *handlers.ProductHandler,
	categoryHandler *handlers.CategoryHandler,
) {
	// Product routes
	e.GET("/products", productHandler.GetProducts)
	e.GET("/products/:id", productHandler.GetProduct)
	e.POST("/products", productHandler.CreateProduct)
	e.PUT("/products/:id", productHandler.UpdateProduct)
	e.DELETE("/products/:id", productHandler.DeleteProduct)

	// Cart routes
	e.GET("/carts", cartHandler.GetCarts)
	e.GET("/carts/:cartId", cartHandler.GetCart)
	e.POST("/carts", cartHandler.CreateCart)

	e.POST("/carts/:cartId/items", cartHandler.AddItemToCart)
	e.DELETE("/carts/:cartId/items/:itemId", cartHandler.RemoveItemFromCart)

	// Category routes
	e.GET("/categories", categoryHandler.GetCategories)
	e.GET("/categories/:id", categoryHandler.GetCategory)
	e.POST("/categories", categoryHandler.CreateCategory)
	e.PUT("/categories/:id", categoryHandler.UpdateCategory)
	e.DELETE("/categories/:id", categoryHandler.DeleteCategory)
	e.GET("/categories/:id/products", categoryHandler.GetProductsByCategory)
}
