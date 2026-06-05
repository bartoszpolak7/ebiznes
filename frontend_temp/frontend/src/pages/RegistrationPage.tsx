import { useState } from "react";
import { register } from "../api/api";
import { useNavigate } from "react-router-dom";

export default function RegistrationPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");

    try {
      await register(email, password);
      navigate("/login");
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Registration failed");
    }
  };

  return (
    <div className="login-page">
      <h1 style={{ margin: "2em", marginLeft: "4em" }}>Register</h1>
      <form onSubmit={handleSubmit}>
        <div style={{ margin: "2em", gap: "1em", display: "flex" }}>
          <label htmlFor="email">Email:</label>
          <input
            type="email"
            id="email"
            name="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
        </div>
        <div style={{ margin: "2em", gap: "1em", display: "flex" }}>
          <label htmlFor="password">Password:</label>
          <input
            type="password"
            id="password"
            name="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
        <button style={{ marginLeft: "150px" }}>Register</button>
      </form>
      {error && <div style={{ color: "red", marginLeft: "4em" }}>{error}</div>}
      <div style={{ margin: "2em", marginLeft: "4em" }}>
        Already have an account? <a href="/login">Login here</a>
      </div>
    </div>
  );
}
