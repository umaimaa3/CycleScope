import { Link } from "react-router-dom";

function Navbar() {

  const linkStyle = {
    textDecoration: "none",
    color: "#7c7cf9",
    fontWeight: "500"
  };

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
      <Link to="/" style={linkStyle}>Dashboard</Link>
      <Link to="/calendar" style={linkStyle}>Calendar</Link>
      <Link to="/nutrition" style={linkStyle}>Nutrition</Link>
    </nav>
  );
}

export default Navbar;