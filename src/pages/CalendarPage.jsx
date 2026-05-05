import { getCycleInfo } from "../utils/cycleUtils";
import { phaseInfo } from "../data/phaseInfo";

function CalendarPage() {
    const savedData = localStorage.getItem("cycleData");

    if (!savedData) {
        return <p>No cycle data found.</p>;
    }

    const cycleData = JSON.parse(savedData);

    const today = new Date();
    const year = today.getFullYear();
    const month = today.getMonth();

    // Get number of days in month
    const daysInMonth = new Date(year, month + 1, 0).getDate();

    // Generate array of days
    const days = Array.from({ length: daysInMonth }, (_, i) => i + 1);

    function getPhaseColor(phase) {
        switch (phase) {
            case "Menstrual":
                return "#fcd5ce";
            case "Follicular":
                return "#d0f4de";
            case "Ovulation":
                return "#fff3b0";
            case "Luteal":
                return "#e4c1f9";
            default:
                return "#eee";
        }
    }

    return (
    <div style={{ padding: "2rem" }}>
        <h1>Calendar</h1>

        <div
            style={{
                display: "grid",
                gridTemplateColumns: "repeat(7, 1fr)",
                gap: "10px",
                marginTop: "1.5rem"
            }}
        >
        {days.map((day) => {
            const date = new Date(year, month, day);
            const { phase } = getCycleInfo(cycleData, date);

            return (
                <div
                    key={day}
                    style={{
                        padding: "10px",
                        borderRadius: "8px",
                        textAlign: "center",
                        background: getPhaseColor(phase)
                    }}
                >
                    <strong>{day}</strong>
                    <div style={{ fontSize: "0.8rem" }}>{phase}</div>

                </div>
            );
        })}
      </div>
    </div>
  );
}

export default CalendarPage;

