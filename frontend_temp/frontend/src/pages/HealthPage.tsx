import { useEffect, useState } from "react";
import { healthCheck } from "../api/api";

export default function HealthPage() {
  const [status, setStatus] = useState<string>("checking...");

  useEffect(() => {
    const checkHealth = async () => {
      try {
        const data = await healthCheck();
        setStatus(data.status);
      } catch {
        setStatus("unreachable");
      }
    };

    checkHealth();
  }, []);

  return <div>Backend status: {status}</div>;
}