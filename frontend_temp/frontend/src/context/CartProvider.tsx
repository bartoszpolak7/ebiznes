import { useContext, useMemo, useState, type ReactNode } from "react";
import type { CartItem, Product } from "../types";
import { CartContext } from "./CartContext";
import { ProductsContext } from "./ProductsContext";

export const CartProvider = ({ children }: { children: ReactNode }) => {
  const [cart, setCart] = useState<CartItem[]>([]);
  const contextProducts = useContext(ProductsContext);

  const products = useMemo(() => {
    return Array.isArray(contextProducts) ? contextProducts : [];
  }, [contextProducts]);

  const addToCart = (product: Product) => {
    setCart((prev) => {
      const existing = prev.find((item) => item.productId === product.id);

      if (existing) {
        return prev.map((item) =>
          item.productId === product.id
            ? { ...item, amount: item.amount + 1 }
            : item,
        );
      }

      return [...prev, { productId: product.id, amount: 1 }];
    });
  };

  const clearCart = () => setCart([]);

  // useMemo to avoid recalculating total on every render unless cart or products change
  const getTotal = useMemo(() => {
    const productMap = new Map(products.map((p) => [p.id, p]));

    return () =>
      cart.reduce((sum, item) => {
        const product = productMap.get(item.productId);
        return sum + (product ? product.price * item.amount : 0);
      }, 0);
  }, [cart, products]);

  return (
    <CartContext.Provider
      value={useMemo(
        () => ({ cart, addToCart, clearCart, getTotal }),
        [cart, getTotal],
      )}
    >
      {children}
    </CartContext.Provider>
  );
};
