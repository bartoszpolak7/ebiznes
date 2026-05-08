package handlers

import (
	"time"

	"zad4/models"
)

// CartItemResponse represents a cart item without nested Product data
type CartItemResponse struct {
	ID        uint   `json:"id"`
	ProductID uint   `json:"product_id"`
	Name      string `json:"name"`
	Quantity  uint   `json:"quantity"`
	Total     uint   `json:"total"`
}

// CartResponse represents a cart with simplified items
type CartResponse struct {
	ID        uint               `json:"id"`
	CreatedAt time.Time          `json:"created_at"`
	UpdatedAt time.Time          `json:"updated_at"`
	Items     []CartItemResponse `json:"items"`
}

// ToCartResponse converts a Cart model to CartResponse
func ToCartResponse(cart *models.Cart) CartResponse {
	items := make([]CartItemResponse, len(cart.Items))
	for i, item := range cart.Items {
		items[i] = CartItemResponse{
			ID:        item.ID,
			ProductID: item.ProductID,
			Name:      item.Product.Name,
			Quantity:  item.Quantity,
			Total:     item.Product.Price * item.Quantity,
		}
	}

	return CartResponse{
		ID:        cart.ID,
		CreatedAt: cart.CreatedAt,
		UpdatedAt: cart.UpdatedAt,
		Items:     items,
	}
}

// ToCartResponseList converts a slice of Carts to CartResponses
func ToCartResponseList(carts []models.Cart) []CartResponse {
	responses := make([]CartResponse, len(carts))
	for i, cart := range carts {
		responses[i] = ToCartResponse(&cart)
	}
	return responses
}

// ProductResponse represents a product without nested Category data
type ProductResponse struct {
	ID         uint      `json:"id"`
	CreatedAt  time.Time `json:"created_at"`
	UpdatedAt  time.Time `json:"updated_at"`
	Name       string    `json:"name"`
	Price      uint      `json:"price"`
	CategoryID uint      `json:"category_id"`
}

// ToProductResponse converts a Product model to ProductResponse
func ToProductResponse(product *models.Product) ProductResponse {
	return ProductResponse{
		ID:         product.ID,
		CreatedAt:  product.CreatedAt,
		UpdatedAt:  product.UpdatedAt,
		Name:       product.Name,
		Price:      product.Price,
		CategoryID: product.CategoryID,
	}
}

// ToProductResponseList converts a slice of Products to ProductResponses
func ToProductResponseList(products []models.Product) []ProductResponse {
	responses := make([]ProductResponse, len(products))
	for i, product := range products {
		responses[i] = ToProductResponse(&product)
	}
	return responses
}
