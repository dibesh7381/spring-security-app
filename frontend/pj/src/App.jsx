import { BrowserRouter, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import SignupPage from "./pages/SignupPage";
import LoginPage from "./pages/LoginPage";
import HomePage from "./pages/HomePage";
import ProfilePage from "./pages/ProfilePage";
import CustomerHomePage from "./pages/CustomerHomePage"; // ✅ new page import
import BecomeSellerPage from "./pages/BecomeSellerPage";
import SellerHomePage from "./pages/SellerHomePage";

export default function App() {
  return (
    <BrowserRouter>
      <Navbar />
      <Routes>
        {/* Public routes */}
        <Route path="/" element={<HomePage />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route path="/login" element={<LoginPage />} />

        {/* Protected pages */}
        <Route path="/profile" element={<ProfilePage />} />
        <Route path="/customer/home" element={<CustomerHomePage />} /> {/* ✅ added */}
        <Route path="/become-seller" element={<BecomeSellerPage />} />
        <Route path="/seller/home" element={<SellerHomePage />} />
      </Routes>
    </BrowserRouter>
  );
}
