import { useEffect, useState } from "react";
import type { Product } from "../types";
import { getProducts } from "../api/api";
import { ProductsContext } from "./ProductsContext";

export const ProductsProvider = ({
  children,
}: {
  children: React.ReactNode;
}) => {
  const [products, setProducts] = useState<Product[]>([]);
  // const [error, setError] = useState<string | null>(
  //   "TEST ERROR: This is what it looks like!",
  // );
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const data = await getProducts();
        console.log("Fetched products:", data);
        console.log("is array:", Array.isArray(data));
        if (data == null) {
          throw new Error("Received null data from getProducts");
        }
        setProducts(data);
      } catch (err) {
        setError((err as Error).message || "Failed to load products");
      }
    };

    fetchProducts();
  }, []);

  return (
    <ProductsContext.Provider value={products}>
      {error && <div className="error-banner">{error}</div>}
      {children}
    </ProductsContext.Provider>
  );
};
