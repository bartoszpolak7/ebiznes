export type Product = {
  id: number;
  name: string;
  price: number;
};

export type CartItem = {
  productId: number;
  amount: number;
};

export type CartContextType = {
  cart: CartItem[];
  addToCart: (product: Product) => void;
  clearCart: () => void;
  getTotal: () => number;
};

export type Payment = {
  total: number;
};