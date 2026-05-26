import axios from "axios";
import type { Payment, Product } from "../types";

export const api = axios.create({
  baseURL: "http://localhost:8080",
});

export const getProducts = () => api.get<Product[]>("/products");
export const sendPayment = (data: Payment) => api.post("/payments", data);