package handlers

import (
	"net/http"

	"github.com/labstack/echo/v5"
	"gorm.io/gorm"

	"zad4/models"
)

type ProductHandler struct {
	DB *gorm.DB
}

func (h *ProductHandler) GetProducts(c *echo.Context) error {
	var products []models.Product

	h.DB.Scopes(models.WithCategory).Find(&products)

	return c.JSON(http.StatusOK, products)
}

func (h *ProductHandler) GetProduct(c *echo.Context) error {
	id := c.Param("id")
	var product models.Product

	result := h.DB.Scopes(models.WithCategory).First(&product, id)
	if result.Error != nil {
		return c.JSON(http.StatusNotFound, map[string]string{"error": "product not found"})
	}

	return c.JSON(http.StatusOK, product)
}

func (h *ProductHandler) CreateProduct(c *echo.Context) error {
	var product models.Product
	if err := c.Bind(&product); err != nil {
		return c.JSON(http.StatusBadRequest, map[string]string{"error": "invalid request"})
	}
	h.DB.Scopes(models.WithCategory).Create(&product)
	return c.JSON(http.StatusCreated, product)
}

func (h *ProductHandler) UpdateProduct(c *echo.Context) error {
	id := c.Param("id")

	var product models.Product

	result := h.DB.Scopes(models.WithCategory).First(&product, id)

	if result.Error != nil {
		return c.JSON(http.StatusNotFound, map[string]string{"error": "product not found"})
	}

	if err := c.Bind(&product); err != nil {
		return c.JSON(http.StatusBadRequest, map[string]string{"error": "invalid request"})
	}
	h.DB.Save(&product)
	h.DB.Scopes(models.WithCategory).First(&product, id)

	return c.JSON(http.StatusOK, product)
}

func (h *ProductHandler) DeleteProduct(c *echo.Context) error {
	id := c.Param("id")
	var product models.Product

	result := h.DB.Scopes(models.WithCategory).First(&product, id)
	if result.Error != nil {
		return c.JSON(http.StatusNotFound, map[string]string{"error": "product not found"})
	}
	h.DB.Delete(&product)
	return c.JSON(http.StatusOK, map[string]string{"message": "product deleted"})
}
