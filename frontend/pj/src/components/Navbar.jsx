import { Link, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { Menu, X } from "lucide-react"; // icons

export default function Navbar() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [loading, setLoading] = useState(true);
  const [userRole, setUserRole] = useState("");
  const [menuOpen, setMenuOpen] = useState(false);
  const navigate = useNavigate();

  // ✅ Check if user is logged in
  const checkAuth = async () => {
    try {
      const res = await fetch("http://localhost:8080/api/auth/profile", {
        method: "GET",
        credentials: "include",
      });

      if (!res.ok) {
        setIsLoggedIn(false);
        setLoading(false);
        return;
      }

      const data = await res.json();
      if (data.status === true) {
        setIsLoggedIn(true);
        setUserRole(data.data?.role || "");
      } else {
        setIsLoggedIn(false);
        setUserRole("");
      }
    } catch (err) {
      console.error("Auth check failed:", err);
      setIsLoggedIn(false);
      setUserRole("");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    checkAuth();
    window.addEventListener("authChanged", checkAuth);
    return () => window.removeEventListener("authChanged", checkAuth);
  }, []);

  // ✅ Logout function
  const handleLogout = async () => {
    try {
      await fetch("http://localhost:8080/api/auth/logout", {
        method: "POST",
        credentials: "include",
      });
      setIsLoggedIn(false);
      setUserRole("");
      window.dispatchEvent(new Event("authChanged"));
      alert("Logged out successfully!");
      navigate("/login");
      setMenuOpen(false);
    } catch (err) {
      console.error("Logout failed:", err);
    }
  };

  if (loading) return null;

  // ✅ Guest (unauthenticated) links
  const guestLinks = (
    <>
      <Link
        to="/signup"
        onClick={() => setMenuOpen(false)}
        className="hover:bg-blue-700 px-3 py-1 rounded transition-colors block md:inline-block"
      >
        Signup
      </Link>
      <Link
        to="/login"
        onClick={() => setMenuOpen(false)}
        className="hover:bg-blue-700 px-3 py-1 rounded transition-colors block md:inline-block"
      >
        Login
      </Link>
    </>
  );

  // ✅ Customer links
  const customerLinks = (
    <>
      <Link
        to="/profile"
        onClick={() => setMenuOpen(false)}
        className="hover:bg-blue-700 px-3 py-1 rounded transition-colors block md:inline-block"
      >
        Profile
      </Link>
      <Link
        to="/customer/home"
        onClick={() => setMenuOpen(false)}
        className="hover:bg-blue-700 px-3 py-1 rounded transition-colors block md:inline-block"
      >
        Customer Home
      </Link>
      <Link
        to="/become-seller"
        onClick={() => setMenuOpen(false)}
        className="bg-yellow-500 hover:bg-yellow-600 text-black px-3 py-1 rounded transition font-medium block md:inline-block"
      >
        Become Seller
      </Link>
      <button
        onClick={handleLogout}
        className="bg-red-500 hover:bg-red-600 px-3 py-1 rounded transition text-white block md:inline-block"
      >
        Logout
      </button>
    </>
  );

  // ✅ Seller links
  const sellerLinks = (
    <>
      <Link
        to="/profile"
        onClick={() => setMenuOpen(false)}
        className="hover:bg-blue-700 px-3 py-1 rounded transition-colors block md:inline-block"
      >
        Profile
      </Link>
      <Link
        to="/seller/home"
        onClick={() => setMenuOpen(false)}
        className="hover:bg-blue-700 px-3 py-1 rounded transition-colors block md:inline-block"
      >
        Seller Home
      </Link>
      <button
        onClick={handleLogout}
        className="bg-red-500 hover:bg-red-600 px-3 py-1 rounded transition text-white block md:inline-block"
      >
        Logout
      </button>
    </>
  );

  return (
    <nav className="bg-blue-600 text-white px-6 py-3 shadow-md flex justify-between items-center">
      {/* ✅ Brand */}
      <Link
        to="/"
        className="text-lg font-bold tracking-wide flex items-center gap-1"
      >
        🔐 Security Demo
      </Link>

      {/* ✅ Desktop Menu */}
      <div className="hidden md:flex gap-4 items-center">
        {/* ✅ Always visible Home */}
        <Link
          to="/"
          onClick={() => setMenuOpen(false)}
          className="hover:bg-blue-700 px-3 py-1 rounded transition-colors block md:inline-block"
        >
          Home
        </Link>

        {!isLoggedIn && guestLinks}
        {isLoggedIn && userRole === "CUSTOMER" && customerLinks}
        {isLoggedIn && userRole === "SELLER" && sellerLinks}
      </div>

      {/* ✅ Mobile Menu Button */}
      <button
        className="md:hidden p-2 rounded hover:bg-blue-700 transition"
        onClick={() => setMenuOpen(!menuOpen)}
      >
        {menuOpen ? <X size={24} /> : <Menu size={24} />}
      </button>

      {/* ✅ Mobile Drawer */}
      <div
        className={`fixed top-0 right-0 h-full w-64 bg-blue-700 text-white transform ${
          menuOpen ? "translate-x-0" : "translate-x-full"
        } transition-transform duration-300 ease-in-out shadow-lg z-50 flex flex-col p-6 gap-4`}
      >
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-xl font-bold">Menu</h2>
          <button onClick={() => setMenuOpen(false)}>
            <X size={24} />
          </button>
        </div>

        {/* ✅ Always visible Home link in mobile */}
        <Link
          to="/"
          onClick={() => setMenuOpen(false)}
          className="hover:bg-blue-600 px-3 py-1 rounded transition-colors block"
        >
          Home
        </Link>

        {!isLoggedIn && guestLinks}
        {isLoggedIn && userRole === "CUSTOMER" && customerLinks}
        {isLoggedIn && userRole === "SELLER" && sellerLinks}
      </div>

      {/* ✅ Background overlay */}
      {menuOpen && (
        <div
          onClick={() => setMenuOpen(false)}
          className="fixed inset-0 bg-black opacity-40 z-40"
        />
      )}
    </nav>
  );
}




