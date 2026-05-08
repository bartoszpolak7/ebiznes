package models

import "gorm.io/gorm"

type Product struct {
	gorm.Model
	Name       string
	Price      uint
	CategoryID uint
	Category   Category
}

func WithCategory(db *gorm.DB) *gorm.DB {
	return db.Preload("Category")
}
