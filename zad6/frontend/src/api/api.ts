import axios from "axios";
import type { Payment, Product } from "../types";

export const api = axios.create({
  baseURL: "http://localhost:8080",
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const healthCheck = async () => {
  try {
    const res = await api.get("/health");
    return res.data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      throw new Error(
        error.response?.data?.message ||
          error.response?.data?.error ||
          "Health check failed",
        { cause: error },
      );
    }
  }
};

export const getProducts = async (): Promise<Product[]> => {
  try {
    const res = await api.get<Product[]>("/products");
    return res.data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      throw new Error(
        error.response?.data?.message ||
          error.response?.data?.error ||
          "Failed to fetch products",
        { cause: error },
      );
    }
    throw new Error("Network or unexpected error occurred", { cause: error });
  }
};

export const sendPayment = (data: Payment) => api.post("/payments", data);

export const register = async (email: string, password: string) => {
  try {
    const res = await api.post("/register", { email, password });
    return res.data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      throw new Error(
        error.response?.data?.message ||
          error.response?.data?.error ||
          "Registration failed",
        { cause: error },
      );
    }
    throw new Error("Registration failed", { cause: error });
  }
};

export const login = async (email: string, password: string) => {
  try {
    const res = await api.post("/login", { email, password });
    return res.data;
  } catch (error) {
    if (axios.isAxiosError(error)) {
      throw new Error(
        error.response?.data?.message ||
          error.response?.data?.error ||
          "Invalid credentials",
        { cause: error },
      );
    }
    throw new Error("Invalid credentials", { cause: error });
  }
};
