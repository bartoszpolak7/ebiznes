import { createContext, useContext } from "react";
import type { Product } from "../types";

export const ProductsContext = createContext<Product[]>([]);

export const useProducts = () => {
  const ctx = useContext(ProductsContext);
  if (!ctx)
    throw new Error("useProducts must be used within a ProductsProvider");

  return ctx;
};
