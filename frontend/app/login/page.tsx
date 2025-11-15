"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { apiPost, ApiResult } from "@/lip/apiClientSWR";
import { BaseUser } from "@/lip/types";

export default function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const router = useRouter();

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    try {
      // Call your API
      const res: ApiResult<BaseUser> = await apiPost("/api/login", {
        email,
        password,
      });
      setLoading(res.isLoading);

      if (res.isError || !res.data) {
        setError(error || "Login failed");
        return;
      }

      // data.user contains the logged-in user
      const userRole = res.data.role;

      // Redirect based on role
      if (userRole === "ADMIN") {
        router.push("/admin");
      } else if (userRole === "PATIENT") {
        router.push("/patient");
      } else {
        router.push("/"); // fallback
      }
    } catch (err) {
      setError("Something went wrong");
    }
  };

  return (
    <div className="flex justify-center items-center min-h-screen bg-gray-100">
      <form
        onSubmit={handleLogin}
        className="bg-white p-8 rounded shadow-md w-full max-w-sm flex flex-col gap-4"
      >
        <h1 className="text-2xl font-bold text-center">Login</h1>

        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
          className="border p-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
        />

        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
          className="border p-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
        />

        <button
          type="submit"
          disabled={loading}
          className="bg-blue-500 text-white p-2 rounded hover:bg-blue-600 transition disabled:opacity-50"
        >
          {loading ? "Logging in..." : "Login"}
        </button>

        {error && (
          <p className="text-red-500 text-center font-medium">{error}</p>
        )}
      </form>
    </div>
  );
}
