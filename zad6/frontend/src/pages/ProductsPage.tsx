import { useNavigate } from "react-router-dom";
import { ProductList } from "../components/ProductList";

export default function ProductsPage() {
  const navigate = useNavigate();
  const logout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };
  return (
    <div>
      <ProductList />
      <button style={{ marginTop: "1em" }} onClick={logout}>
        Log out
      </button>
    </div>
  );
}
