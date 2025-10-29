import { useState, useEffect } from "react";

export default function SellerHomePage() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await fetch("http://localhost:8080/api/auth/seller-home", {
          method: "GET",
          credentials: "include", // ✅ send JWT cookie
        });

        const result = await res.json();

        if (result.status) {
          setData(result.data);
        } else {
          setError(result.message || "Unauthorized");
        }
      } catch {
        setError("Something went wrong.");
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  if (loading) return <p className="text-center mt-10">Loading...</p>;
  if (error)
    return (
      <p className="text-center text-red-500 font-semibold mt-10">{error}</p>
    );

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
      <div className="bg-white p-6 rounded-xl shadow-lg text-center w-96">
        <h1 className="text-2xl font-bold mb-3">{data.title}</h1>
        <p className="text-gray-700">{data.content}</p>
      </div>
    </div>
  );
}
