import { BrowserRouter, Routes, Route } from "react-router-dom";
import DashboardPage from "./pages/DashboardPage";
import CalendarPage from "./pages/CalendarPage";
import NutritionPage from "./pages/NutritionPage";
import InputPage from "./pages/InputPage";
import Navbar from "./components/Navbar";
import "./App.css";

function App() {
  return (
    <BrowserRouter>
      <Navbar />
      <Routes>
        <Route path="/" element={<DashboardPage />} />
        <Route path="/calendar" element={<CalendarPage />} />
        <Route path="/nutrition" element={<NutritionPage />} />
        <Route path="/setup" element={<InputPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;

