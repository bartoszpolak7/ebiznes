import { render, screen, waitFor } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import { ProductList } from "./ProductList";
import { CartContext } from "../context/CartContext";
import type { CartContextType } from "../types";

vi.mock("../api/api", () => ({
  getProducts: vi.fn().mockResolvedValue({
    data: [
      { id: 1, name: "Dziadek do orzechów", price: 29.99 },
      { id: 2, name: "Lego Ninjago", price: 49.99 },
      { id: 3, name: "Buty na rzepy", price: 99.99 },
    ],
  }),
}));

const cartState: CartContextType = {
  cart: [],
  addToCart: vi.fn(),
  clearCart: vi.fn(),
  getTotal: () => 0,
};

describe("ProductList", () => {
  it("renders products from API", async () => {
    render(
      <CartContext.Provider value={cartState}>
        <ProductList />
      </CartContext.Provider>
    );

    expect(screen.getByRole("heading", { name: "Products" })).toBeInTheDocument();

    await waitFor(() => {
      expect(screen.getByText("Dziadek do orzechów")).toBeInTheDocument();
      expect(screen.getByText("Lego Ninjago")).toBeInTheDocument();
      expect(screen.getByText("Buty na rzepy")).toBeInTheDocument();
      expect(screen.getByText("29.99 zł")).toBeInTheDocument();
      expect(screen.getByText("49.99 zł")).toBeInTheDocument();
      expect(screen.getByText("99.99 zł")).toBeInTheDocument();
    });

    expect(screen.getAllByRole("button", { name: /add to cart/i })).toHaveLength(3);
    expect(screen.getByRole("link", { name: /go to cart/i })).toBeInTheDocument();
  });
});
