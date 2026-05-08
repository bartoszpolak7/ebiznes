package handlers

import (
	"net/http"

	"github.com/labstack/echo/v5"
	"gorm.io/gorm"

	"zad4/models"
)

type CategoryHandler struct {
	DB *gorm.DB
}

func (h *CategoryHandler) GetCategories(c *echo.Context) error {
	var categories []models.Category

	h.DB.Find(&categories)

	return c.JSON(http.StatusOK, categories)
}

func (h *CategoryHandler) GetCategory(c *echo.Context) error {
	id := c.Param("id")
	var category models.Category

	result := h.DB.First(&category, id)
	if result.Error != nil {
		return c.JSON(http.StatusNotFound, map[string]string{"error": "category not found"})
	}

	return c.JSON(http.StatusOK, category)
}

func (h *CategoryHandler) CreateCategory(c *echo.Context) error {
	var category models.Category
	if err := c.Bind(&category); err != nil {
		return c.JSON(http.StatusBadRequest, map[string]string{"error": "invalid request"})
	}
	h.DB.Create(&category)
	return c.JSON(http.StatusCreated, category)
}

func (h *CategoryHandler) UpdateCategory(c *echo.Context) error {
	id := c.Param("id")

	var category models.Category

	result := h.DB.First(&category, id)

	if result.Error != nil {
		return c.JSON(http.StatusNotFound, map[string]string{"error": "category not found"})
	}

	if err := c.Bind(&category); err != nil {
		return c.JSON(http.StatusBadRequest, map[string]string{"error": "invalid request"})
	}
	h.DB.Save(&category)

	return c.JSON(http.StatusOK, category)
}

func (h *CategoryHandler) DeleteCategory(c *echo.Context) error {
	id := c.Param("id")
	var category models.Category

	result := h.DB.First(&category, id)
	if result.Error != nil {
		return c.JSON(http.StatusNotFound, map[string]string{"error": "category not found"})
	}
	h.DB.Delete(&category)
	return c.JSON(http.StatusOK, map[string]string{"message": "category deleted"})
}

func (h *CategoryHandler) GetProductsByCategory(c *echo.Context) error {
	id := c.Param("id")
	var category models.Category

	result := h.DB.Preload("Products").First(&category, id)
	if result.Error != nil {
		return c.JSON(http.StatusNotFound, map[string]string{"error": "category not found"})
	}

	return c.JSON(http.StatusOK, map[string]any{
		"category": category,
		"products": ToProductResponseList(category.Products),
	})
}
