package main

import (
	"github.com/labstack/echo/v5"
	"github.com/labstack/echo/v5/middleware"

	"zad4/database"
	"zad4/handlers"
	"zad4/routes"
)

func main() {

	e := echo.New()
	e.Use(middleware.RequestLogger())

	db, err := database.Connect()
	if err != nil {
		e.Logger.Error("failed to connect to database", "error", err)
	}

	database.Migrate(db)

	routes.SetupRoutes(
		e,
		&handlers.CartHandler{DB: db},
		&handlers.ProductHandler{DB: db},
		&handlers.CategoryHandler{DB: db},
	)

	if err := e.Start(":1323"); err != nil {
		e.Logger.Error("failed to start server", "error", err)
	}
}
