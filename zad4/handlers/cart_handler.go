package handlers

import (
	"net/http"

	"github.com/labstack/echo/v5"
	"gorm.io/gorm"

	"zad4/models"
)

type CartHandler struct {
	DB *gorm.DB
}

func (h *CartHandler) GetCarts(c *echo.Context) error {
	var carts []models.Cart
	h.DB.Preload("Items.Product").Find(&carts)

	return c.JSON(http.StatusOK, ToCartResponseList(carts))
}

func (h *CartHandler) GetCart(c *echo.Context) error {
	id := c.Param("id")
	var cart models.Cart

	result := h.DB.Preload("Items.Product").First(&cart, id)
	if result.Error != nil {
		return c.JSON(http.StatusNotFound, map[string]string{"error": "cart not found"})
	}

	return c.JSON(http.StatusOK, ToCartResponse(&cart))
}

func (h *CartHandler) CreateCart(c *echo.Context) error {
	var cart models.Cart
	if err := c.Bind(&cart); err != nil {
		return c.JSON(http.StatusBadRequest, map[string]string{"error": "invalid request"})
	}
	h.DB.Create(&cart)
	return c.JSON(http.StatusCreated, cart)
}

func (h *CartHandler) AddItemToCart(c *echo.Context) error {
	cartID := c.Param("id")

	var cart models.Cart

	if err := h.DB.First(&cart, cartID).Error; err != nil {
		return c.JSON(http.StatusNotFound,
			map[string]string{"error": "cart not found"})
	}

	var req struct {
		ProductID uint `json:"product_id"`
		Quantity  uint `json:"quantity"`
	}

	if err := c.Bind(&req); err != nil {
		return c.JSON(http.StatusBadRequest,
			map[string]string{"error": "invalid request"})
	}

	if err := h.DB.First(&models.Product{}, req.ProductID).Error; err != nil {
		return c.JSON(http.StatusNotFound,
			map[string]string{"error": "product not found"})
	}

	// check if item already exists in cart
	var item models.CartItem

	err := h.DB.
		Where("cart_id = ? AND product_id = ?", cart.ID, req.ProductID).
		First(&item).Error

	if err == nil {
		// item exists -> increase quantity
		item.Quantity += req.Quantity
		h.DB.Save(&item)
		h.DB.Preload("Product").First(&item, item.ID)

		return c.JSON(http.StatusOK, CartItemResponse{
			ID:        item.ID,
			ProductID: item.ProductID,
			Name:      item.Product.Name,
			Quantity:  item.Quantity,
			Total:     item.Product.Price * item.Quantity,
		})
	}

	// item does not exist → create new
	newItem := models.CartItem{
		CartID:    cart.ID,
		ProductID: req.ProductID,
		Quantity:  req.Quantity,
	}

	if err := h.DB.Create(&newItem).Error; err != nil {
		return c.JSON(http.StatusInternalServerError,
			map[string]string{"error": "failed to add item"})
	}

	// Preload product data for response
	h.DB.Preload("Product").First(&newItem, newItem.ID)

	return c.JSON(http.StatusOK, CartItemResponse{
		ID:        newItem.ID,
		ProductID: newItem.ProductID,
		Name:      newItem.Product.Name,
		Quantity:  newItem.Quantity,
		Total:     newItem.Product.Price * newItem.Quantity,
	})
}

func (h *CartHandler) RemoveItemFromCart(c *echo.Context) error {
	cartID := c.Param("cartId")
	itemID := c.Param("itemId")

	var cart models.Cart

	if err := h.DB.First(&cart, cartID).Error; err != nil {
		return c.JSON(http.StatusNotFound,
			map[string]string{"error": "cart not found"})
	}

	var item models.CartItem

	if err := h.DB.Where("id = ? AND cart_id = ?", itemID, cart.ID).First(&item).Error; err != nil {
		return c.JSON(http.StatusNotFound,
			map[string]string{"error": "item not found"})
	}

	if item.Quantity > 1 {
		// decrease quantity
		item.Quantity -= 1
		h.DB.Save(&item)

		h.DB.Preload("Product").First(&item, item.ID)

		return c.JSON(http.StatusOK, CartItemResponse{
			ID:        item.ID,
			ProductID: item.ProductID,
			Name:      item.Product.Name,
			Quantity:  item.Quantity,
			Total:     item.Product.Price * item.Quantity,
		})
	}

	h.DB.Delete(&item)

	return c.JSON(http.StatusOK, map[string]string{"message": "item removed"})
}
