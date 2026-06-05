import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

export default function OAuth2CallbackPage() {
  const navigate = useNavigate();

  useEffect(() => {
    console.log("full URL:", window.location.href);
    console.log("search params:", window.location.search);
    const params = new URLSearchParams(window.location.search);
    const token = params.get("token");
    console.log("token:", token);
    if (token) {
      localStorage.setItem("token", token);
      window.location.href = "/";
    } else {
      navigate("/login");
    }
  }, [navigate]);

  return <div>Logging in...</div>;
}
