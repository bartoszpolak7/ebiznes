import { useEffect } from "react";
import { useState } from "react";
import type { Product } from "../types";
import { getProducts } from "../api/api";
import { ProductsContext } from "./ProductsContext";


export const ProductsProvider = ({ children }: { children: React.ReactNode }) => {
  const [products, setProducts] = useState<Product[]>([]);
  useEffect(() => {
    getProducts().then((res) => setProducts(res.data));
  }, []);

  return (
    <ProductsContext.Provider value={products}>
      {children}
    </ProductsContext.Provider>
  );
}