import { render, screen, fireEvent } from "@testing-library/react";
import { describe, expect, it, vi, beforeEach } from "vitest";
import { CartProvider } from "../context/CartProvider";
import { useCart } from "../context/CartContext";
import { ProductsContext } from "../context/ProductsContext";
import type { Product } from "../types";

const sampleProducts: Product[] = [
  { id: 1, name: "Dziadek do orzechów", price: 29.99 },
  { id: 2, name: "Lego Ninjago", price: 49.99 },
  { id: 3, name: "Buty na rzepy", price: 99.99 },
];

function CartProbe() {
  const { cart, addToCart, clearCart, getTotal } = useCart();

  return (
    <div>
      <div data-testid="cart-size">{cart.length}</div>
      <div data-testid="cart-total">{getTotal()}</div>
      <div data-testid="first-quantity">{cart[0]?.amount ?? 0}</div>
      <button onClick={() => addToCart(sampleProducts[0])}>add-first</button>
      <button onClick={() => addToCart(sampleProducts[1])}>add-second</button>
      <button onClick={clearCart}>clear</button>
    </div>
  );
}

function renderWithProducts() {
  return render(
    <ProductsContext.Provider value={sampleProducts}>
      <CartProvider>
        <CartProbe />
      </CartProvider>
    </ProductsContext.Provider>
  );
}

describe("CartProvider", () => {
  beforeEach(() => {
    vi.clearAllMocks();
  });

  it("starts with empty cart and zero total", () => {
    renderWithProducts();
    expect(screen.getByTestId("cart-size")).toHaveTextContent("0");
    expect(screen.getByTestId("cart-total")).toHaveTextContent("0");
    expect(screen.getByTestId("first-quantity")).toHaveTextContent("0");
  });

  it("adds a new product to cart", () => {
    renderWithProducts();
    fireEvent.click(screen.getByText("add-first"));
    expect(screen.getByTestId("cart-size")).toHaveTextContent("1");
    expect(screen.getByTestId("first-quantity")).toHaveTextContent("1");
    expect(screen.getByTestId("cart-total")).toHaveTextContent("29.99");
  });

  it("increments quantity for existing product", () => {
    renderWithProducts();
    fireEvent.click(screen.getByText("add-first"));
    fireEvent.click(screen.getByText("add-first"));
    expect(screen.getByTestId("cart-size")).toHaveTextContent("1");
    expect(screen.getByTestId("first-quantity")).toHaveTextContent("2");
    expect(screen.getByTestId("cart-total").textContent).toMatch(/59\.98/);
  });

  it("calculates total for multiple products", () => {
    renderWithProducts();
    fireEvent.click(screen.getByText("add-first"));
    fireEvent.click(screen.getByText("add-second"));
    expect(screen.getByTestId("cart-size")).toHaveTextContent("2");
    expect(screen.getByTestId("cart-total")).toHaveTextContent("79.98");
  });

  it("clears cart after clearCart", () => {
    renderWithProducts();
    fireEvent.click(screen.getByText("add-first"));
    fireEvent.click(screen.getByText("add-second"));
    fireEvent.click(screen.getByText("clear"));
    expect(screen.getByTestId("cart-size")).toHaveTextContent("0");
    expect(screen.getByTestId("cart-total")).toHaveTextContent("0");
  });
});
