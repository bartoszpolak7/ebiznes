import { Navigate } from "react-router-dom";

export default function ProtectedRoute({
  children,
}: {
  children: React.ReactNode;
}) {
  const token = localStorage.getItem("token");
  if (!token) {
    sessionStorage.setItem("redirectMessage", "Please log in to continue");
    return <Navigate to="/login" replace />;
  }

  return <>{children}</>;
}
