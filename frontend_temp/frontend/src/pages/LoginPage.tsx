import { useEffect, useState } from "react";
import { login } from "../api/api";

export default function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const message = sessionStorage.getItem("redirectMessage");

  useEffect(() => {
    sessionStorage.removeItem("redirectMessage");
  }, []);

  const handleSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError("");

    try {
      const data = await login(email, password);
      localStorage.setItem("token", data.token);
      window.location.href = "/";
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Invalid credentials");
    }
  };

  return (
    <div className="login-page">
      <h1 style={{ margin: "2em", marginLeft: "4em" }}>Login</h1>
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
        <button style={{ marginLeft: "150px" }}>Login</button>
      </form>
      {error && <div style={{ color: "red", marginLeft: "4em" }}>{error}</div>}
      <div style={{ margin: "2em", marginLeft: "4em" }}>
        Don't have an account? <a href="/register">Register here</a>
      </div>
      <div style={{ margin: "2em", marginLeft: "4em" }}>
        <a href="http://localhost:8080/oauth2/authorization/google">
          <button type="button">Login with Google</button>
        </a>
        <a href="http://localhost:8080/oauth2/authorization/github">
          <button type="button">Login with GitHub</button>
        </a>
      </div>
      {message && <div style={{ color: "orange" }}>{message}</div>}
    </div>
  );
}
