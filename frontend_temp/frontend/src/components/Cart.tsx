import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import { getProducts } from "../api/api";
import type { Product } from "../types";
import { useCart } from "../context/CartContext";

export const Cart = () => {
  const cartState = useCart();
  const [products, setProducts] = useState<Product[]>([]);

  useEffect(() => {
    getProducts().then((res) => setProducts(res.data));
  }, []);

  return (
    <div>
      <h2>Cart</h2>

      {cartState.cart.map((item) => {
        const product = products.find((p) => p.id === item.productId);
        return (
          <div key={item.productId} style={{ border: "1px solid gray", margin: 10, padding: 10 }}>
            <h3>{product?.name}</h3>
            <p>{product?.price} zł</p>
            <p>Quantity: {item.amount}</p>
          </div>
        );
      })}

      <div style={{ padding: 10 }}>Total: {cartState.getTotal()} zł</div>
      <button>
        <Link to="/payment">Payment</Link>
      </button>
      <button>  
        <Link to="/">Back to products</Link>
      </button>
    </div>
  );
};
