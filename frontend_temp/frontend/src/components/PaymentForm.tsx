import { useContext } from "react";
import { sendPayment } from "../api/api";
import { CartContext } from "../context/CartContext";

export const PaymentForm = () => {
    const cartState = useContext(CartContext);
    const { cart, clearCart } = cartState;



  const handlePayment = async () => {
    const payload = {
      total: cartState.getTotal(),
    };

    await sendPayment(payload);

    alert("Payment successful!");
    clearCart();
  };

  return (
    <div>
      <h2>Payment</h2>

      <p>Total: {cartState.getTotal().toFixed(2)} zł</p>

      <button onClick={handlePayment} disabled={cart.length === 0}>
        Pay now
      </button>
    </div>
  );
};
