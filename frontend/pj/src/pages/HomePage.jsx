import React, { useEffect, useState } from "react";

export default function HomePage() {
  const [realData, setData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch("http://localhost:8080/api/auth/home", {
      method: "GET",
      credentials: "include", // ✅ send JWT cookie
    })
      .then((res) => res.json())
      .then((result) => {
        setData(result.data);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Failed to fetch home data:", err);
        setLoading(false);
      });
  }, []);

  if (loading)
    return (
      <div className="flex justify-center items-center h-screen text-blue-600 font-semibold text-lg">
        Loading Home Content...
      </div>
    );

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-100 via-blue-200 to-blue-300 flex flex-col justify-center items-center px-4">
      <div className="max-w-2xl w-full bg-white/60 backdrop-blur-lg shadow-xl rounded-2xl p-8 text-center border border-white/40 hover:shadow-2xl transition-all duration-300">
        <h1 className="text-3xl font-bold text-blue-700 mb-4">
          {realData?.title}
        </h1>
        <p className="text-gray-700 text-lg leading-relaxed">
          {realData?.content}
        </p>
      </div>
    </div>
  );
}
