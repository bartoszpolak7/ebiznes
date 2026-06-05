import { BrowserRouter } from "react-router-dom";
import App from "./App";
import { CartProvider } from "./context/CartProvider";
import { ProductsProvider } from "./context/ProductsProvider";
import { StrictMode } from "react";
import { createRoot } from "react-dom/client";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <ProductsProvider>
      <CartProvider>
        <BrowserRouter>
          <App />
        </BrowserRouter>
      </CartProvider>
    </ProductsProvider>
  </StrictMode>
);