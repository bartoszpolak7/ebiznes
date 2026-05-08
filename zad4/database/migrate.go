package database

import (
	"zad4/models"

	"gorm.io/gorm"
)

func Migrate(db *gorm.DB) error {
	return db.AutoMigrate(
		&models.Category{},
		&models.Product{},
		&models.Cart{},
		&models.CartItem{},
	)
}
