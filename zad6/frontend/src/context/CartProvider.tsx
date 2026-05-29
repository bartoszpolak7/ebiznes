import { useContext, useState, type ReactNode } from "react";
import type { CartItem, Product } from "../types";
import { CartContext } from "./CartContext";
import { ProductsContext } from "./ProductsContext";

export const CartProvider = ({ children }: { children: ReactNode }) => {
  const [cart, setCart] = useState<CartItem[]>([]);
  const products = useContext(ProductsContext);


  const addToCart = (product: Product) => {
    setCart((prev) => {
    const existing = prev.find(
      (item) => item.productId === product.id
    );

    if (existing) {
      return prev.map((item) =>
        item.productId === product.id
          ? { ...item, amount: item.amount + 1 }
          : item
      );
    }

    return [...prev, { productId: product.id, amount: 1 }];
  });
  };


  const clearCart = () => setCart([]);

  const getTotal = () => {
     const productMap = new Map(products.map(p => [p.id, p]));

     return cart.reduce((sum, item) => {
       const product = productMap.get(item.productId);
       return sum + (product ? product.price * item.amount : 0);
     }, 0);
  };

  return (
    <CartContext.Provider value={{ cart, addToCart, clearCart, getTotal }}>
      {children}
    </CartContext.Provider>
  );
};

