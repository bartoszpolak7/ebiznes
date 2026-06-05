import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import { PaymentForm } from "../components/PaymentForm";
import { CartContext } from "../context/CartContext";
import type { CartContextType } from "../types";

vi.mock("../api/api", () => ({
  sendPayment: vi.fn().mockResolvedValue({ data: { status: "ok" } }),
}));

import { sendPayment } from "../api/api";

function renderPaymentForm(cartState: CartContextType) {
  return render(
    <CartContext.Provider value={cartState}>
      <PaymentForm />
    </CartContext.Provider>
  );
}

describe("PaymentForm", () => {
  it("disables pay button for empty cart", () => {
    const cartState: CartContextType = {
      cart: [],
      addToCart: vi.fn(),
      clearCart: vi.fn(),
      getTotal: () => 0,
    };

    renderPaymentForm(cartState);
    expect(screen.getByRole("button", { name: /pay now/i })).toBeDisabled();
    expect(screen.getByText(/total: 0\.00 zł/i)).toBeInTheDocument();
  });

  it("enables pay button when cart has items", () => {
    const cartState: CartContextType = {
      cart: [{ productId: 1, amount: 2 }],
      addToCart: vi.fn(),
      clearCart: vi.fn(),
      getTotal: () => 59.98,
    };

    renderPaymentForm(cartState);
    expect(screen.getByRole("button", { name: /pay now/i })).toBeEnabled();
    expect(screen.getByText(/total: 59\.98 zł/i)).toBeInTheDocument();
  });

  it("sends payment and clears cart on success", async () => {
    const clearCart = vi.fn();
    const cartState: CartContextType = {
      cart: [{ productId: 2, amount: 1 }],
      addToCart: vi.fn(),
      clearCart,
      getTotal: () => 49.99,
    };

    vi.spyOn(globalThis, "alert").mockImplementation(() => {});

    renderPaymentForm(cartState);
    fireEvent.click(screen.getByRole("button", { name: /pay now/i }));

    await waitFor(() => {
      expect(sendPayment).toHaveBeenCalledWith({ total: 49.99 });
      expect(clearCart).toHaveBeenCalled();
      expect(globalThis.alert).toHaveBeenCalledWith("Payment successful!");
    });
  });
});
