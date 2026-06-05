import { Routes, Route } from "react-router-dom";

import ProductsPage from "./pages/ProductsPage";
import CartPage from "./pages/CartPage";
import PaymentPage from "./pages/PaymentPage";
import RegistrationPage from "./pages/RegistrationPage";
import LoginPage from "./pages/LoginPage";
import HealthPage from "./pages/HealthPage";
import ProtectedRoute from "./components/ProtectedRoute";
import OAuth2CallbackPage from "./pages/OAuth2CallbackPage";

function App() {
  return (
    <Routes>
      <Route
        path="/"
        element={
          <ProtectedRoute>
            <ProductsPage />
          </ProtectedRoute>
        }
      />
      <Route
        path="/cart"
        element={
          <ProtectedRoute>
            <CartPage />
          </ProtectedRoute>
        }
      />
      <Route
        path="/payment"
        element={
          <ProtectedRoute>
            <PaymentPage />
          </ProtectedRoute>
        }
      />
      <Route path="/register" element={<RegistrationPage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/health" element={<HealthPage />} />
      <Route path="/oauth2/callback" element={<OAuth2CallbackPage />} />
    </Routes>
  );
}

export default App;
