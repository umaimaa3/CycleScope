import { Link } from "react-router-dom";

function Navbar() {
  return (
    <nav
      style={{
        display: "flex",
        gap: "2rem",
        padding: "1rem 2rem",
        background: "#f1f5f9",
        borderBottom: "1px solid #ddd"
      }}
    >
      <Link to="/">Dashboard</Link>
      <Link to="/calendar">Calendar</Link>
      <Link to="/nutrition">Nutrition</Link>
    </nav>
  );
}

export default Navbar;