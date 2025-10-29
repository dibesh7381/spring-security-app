import { useState } from "react";

export default function BecomeSellerPage() {
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);

  const handleBecomeSeller = async () => {
    try {
      setLoading(true);
      setMessage("");

      const res = await fetch("http://localhost:8080/api/auth/change-role?newRole=SELLER", {
        method: "POST",
        credentials: "include", // ✅ send cookies
      });

      const data = await res.json();

      if (data.status) {
        setMessage("🎉 You are now a Seller! Please re-login to access seller routes.");
      } else {
        setMessage("⚠️ " + data.message);
      }
    } catch {
      setMessage("❌ Failed to change role. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <div className="bg-white shadow-md rounded-xl p-6 w-80 text-center">
        <h1 className="text-2xl font-semibold mb-4 text-gray-800">
          Become a Seller
        </h1>
        <p className="text-gray-600 mb-6">
          Click below to change your account role to <b>SELLER</b>.
        </p>

        <button
          onClick={handleBecomeSeller}
          disabled={loading}
          className={`px-4 py-2 rounded-lg text-white w-full ${
            loading
              ? "bg-gray-400 cursor-not-allowed"
              : "bg-blue-600 hover:bg-blue-700"
          }`}
        >
          {loading ? "Processing..." : "Become Seller"}
        </button>

        {message && (
          <p className="mt-4 text-sm text-gray-700 font-medium">{message}</p>
        )}
      </div>
    </div>
  );
}
