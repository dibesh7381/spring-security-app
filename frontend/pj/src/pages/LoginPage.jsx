import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function LoginPage() {
  const [form, setForm] = useState({ email: "", password: "" });
  const [msg, setMsg] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMsg("");
    setLoading(true);

    try {
      const res = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include", // ✅ include cookies
        body: JSON.stringify(form),
      });

      const data = await res.json();

      // ✅ Always show message returned by backend
      setMsg(data.message || "No message received");

      if (res.ok && data.status) {
        // 🔥 Trigger global auth event
        window.dispatchEvent(new Event("authChanged"));
        // ⏳ Redirect to profile after short delay
        setTimeout(() => navigate("/profile"), 1000);
      }
    } catch (err) {
      console.error("Login failed:", err);
      setMsg("⚠️ Something went wrong. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <div className="bg-white p-6 rounded-xl shadow-md w-80">
        <h2 className="text-xl font-bold mb-4 text-center">Login</h2>

        <form onSubmit={handleSubmit} className="flex flex-col gap-3">
          <input
            name="email"
            type="email"
            placeholder="Email"
            value={form.email}
            onChange={handleChange}
            className="border p-2 rounded"
            required
          />
          <input
            name="password"
            type="password"
            placeholder="Password"
            value={form.password}
            onChange={handleChange}
            className="border p-2 rounded"
            required
          />

          <button
            type="submit"
            disabled={loading}
            className={`p-2 rounded text-white transition-colors ${
              loading
                ? "bg-gray-400 cursor-not-allowed"
                : "bg-green-600 hover:bg-green-700"
            }`}
          >
            {loading ? "Logging in..." : "Login"}
          </button>
        </form>

        {msg && (
          <p
            className={`mt-3 text-center text-sm font-medium ${
              msg.toLowerCase().includes("success")
                ? "text-green-600"
                : msg.toLowerCase().includes("invalid") ||
                  msg.toLowerCase().includes("wrong") ||
                  msg.toLowerCase().includes("fail")
                ? "text-red-600"
                : "text-gray-700"
            }`}
          >
            {msg}
          </p>
        )}
      </div>
    </div>
  );
}


