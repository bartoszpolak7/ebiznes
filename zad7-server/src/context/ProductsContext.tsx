import { createContext } from "react";
import type { Product } from "../types";

export const ProductsContext = createContext<Product[]>([]);