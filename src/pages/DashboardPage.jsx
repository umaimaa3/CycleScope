import PhaseTimeline from "../components/PhaseTimeline";
import NutritionPlate from "../components/nutrition/NutritionPlate";
import { phaseInfo } from "../data/phaseInfo";
import { Link } from "react-router-dom";
import { getSavedCycleData } from "../services/cycleStorageService";
import { getCurrentCycleInfo } from "../services/cycleService";

function DashboardPage() {

  const cycleData = getSavedCycleData();

  if (!cycleData) {
    return <p>No cycle data found. Please enter your cycle first.</p>;
  }

  const { cycleDay, phase, cycleLength } = getCurrentCycleInfo(cycleData);

  const currentPhaseInfo = phaseInfo[phase];

  return (
    <div style={{ 
      padding: "2rem" ,
      width: "100%"
      }}> 

      <h1>CycleScope Dashboard</h1>
      
      <div
        style={{
          display: "flex",
          gap: "2rem",
          marginTop: "1.5rem",
          alignItems: "flex-start",
          width: "100%"
        }}
      >

        <div
          style={{
            flex: "1 1 0",
            background: "#f9fafb",
            padding: "1.5rem",
            borderRadius: "12px"
          }}
        >

          <p>Cycle Length: {cycleData.cycleLength} days</p>
          <p>Last Period Start Date: {cycleData.lastPeriod}</p>

          <Link to="/setup">
            <button
              style={{
                marginTop: "0.75rem",
                padding: "0.6rem 1rem",
                borderRadius: "10px",
                border: "none",
                background: "#d8b4fe",
                fontWeight: "600",
                cursor: "pointer"
              }}
            >
              Update Cycle Info
            </button>
          </Link>

          <h2>Cycle Day {cycleDay}</h2>
          <h2>Current Phase: {phase}</h2>

          <h3 style={{ marginTop: "1rem" }}>How you might feel today</h3>
          <ul>
            {currentPhaseInfo.symptoms.map((symptom, index) => (
              <li key={index}>{symptom}</li>
            ))}
          </ul>
          <p>{currentPhaseInfo.message}</p>

          <PhaseTimeline 
          cycleDay={cycleDay} 
          cycleLength={cycleLength} 
          phase={phase}
          />
        </div>

        <div
          style={{
            flex: "1 1 0",
            background: "#f9fafb",
            padding: "1.5rem",
            borderRadius: "12px"
          }}
        >

          <NutritionPlate phase={phase} />
        </div>

      </div>
    </div>

  );
}

export default DashboardPage;
