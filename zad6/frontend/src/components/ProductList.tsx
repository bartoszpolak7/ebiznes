import { Link } from "react-router-dom";
import { useCart } from "../context/CartContext";
import { useProducts } from "../context/ProductsContext";

export const ProductList = () => {
  const { addToCart } = useCart();
  const contextProducts = useProducts();

  const products = Array.isArray(contextProducts) ? contextProducts : [];

  return (
    <div>
      <h2>Products</h2>

      {products.map((product) => (
        <div
          key={product.id}
          style={{ border: "1px solid gray", margin: 10, padding: 10 }}
        >
          <h3>{product.name}</h3>
          <p>{product.price} zł</p>

          <button onClick={() => addToCart(product)}>Add to cart</button>
        </div>
      ))}

      <button>
        <Link to="/cart">Go to cart</Link>
      </button>
    </div>
  );
};
