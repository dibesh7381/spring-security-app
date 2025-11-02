import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function SignupPage() {
  const [form, setForm] = useState({ username: "", email: "", password: "" });
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
      const res = await fetch("http://localhost:8080/api/auth/signup", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(form),
      });

      const data = await res.json();

      // ✅ Always show backend message
      setMsg(data.message || "Something went wrong.");

      if (res.ok && data.status) {
        // ✅ Redirect after short delay
        setTimeout(() => navigate("/login"), 1200);
      }
    } catch (err) {
      console.error("Signup failed:", err);
      setMsg("⚠️ Server error. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <div className="bg-white p-6 rounded-xl shadow-md w-80">
        <h2 className="text-xl font-bold mb-4 text-center">Signup</h2>

        <form onSubmit={handleSubmit} className="flex flex-col gap-3">
          <input
            name="username"
            type="text"
            placeholder="Username"
            value={form.username}
            onChange={handleChange}
            className="border p-2 rounded"
            required
          />
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
                : "bg-blue-600 hover:bg-blue-700"
            }`}
          >
            {loading ? "Signing up..." : "Signup"}
          </button>
        </form>

        {msg && (
          <p
            className={`mt-3 text-center text-sm font-medium ${
              msg.toLowerCase().includes("success")
                ? "text-green-600"
                : "text-red-600"
            }`}
          >
            {msg}
          </p>
        )}
      </div>
    </div>
  );
}

