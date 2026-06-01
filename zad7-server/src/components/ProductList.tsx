import { useEffect, useState, useContext } from "react";
import { getProducts } from "../api/api";
import { Link } from "react-router-dom";
import { type Product } from "../types";
import { CartContext } from "../context/CartContext";

export const ProductList = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const { addToCart } = useContext(CartContext);

  useEffect(() => {
  getProducts().then((res) => setProducts(res.data));
}, []);

  return (
    <div>
      <h2>Products</h2>

      {products.map((product) => (
        <div key={product.id} style={{ border: "1px solid gray", margin: 10, padding: 10 }}>
          <h3>{product.name}</h3>
          <p>{product.price} zł</p>

          <button onClick={() => addToCart(product)}>
            Add to cart
          </button>
        </div>
        
      ))}

      <button>
        <Link to="/cart">Go to cart</Link>
      </button>
    </div>
  );
};