import PhaseTimeline from "../components/PhaseTimeline";
import NutritionList from "../components/nutrition/NutritionList";
import NutritionPlate from "../components/nutrition/NutritionPlate";
import { phaseInfo } from "../data/phaseInfo";
import { getCycleInfo } from "../utils/cycleUtils";
import { Link } from "react-router-dom";

function DashboardPage() {
  const savedData = localStorage.getItem("cycleData");

  if (!savedData) {
    return <p>No cycle data found. Please enter your cycle first.</p>
  }

  const cycleData = JSON.parse(savedData);

  /*
  const cycleLength = Number(cycleData.cycleLength);
  const lastPeriodDate = new Date(cycleData.lastPeriod);
  const today = new Date();

  const diffTime = today - lastPeriodDate;
  const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));

  const cycleDay = (diffDays % cycleLength) + 1;

  let phase;
  const ovulationDay = cycleLength - 14; 

  if (cycleDay <= 5) {
    phase = "Menstrual";
  } else if (cycleDay < ovulationDay) {
    phase = "Follicular";
  } else if (cycleDay === ovulationDay) {
    phase = "Ovulation";
  } else {
    phase = "Luteal";
  }

  */

  const { cycleDay, phase, cycleLength } = getCycleInfo(cycleData);

  const currentPhaseInfo = phaseInfo[phase];

  return (
    <div style={{ 
      padding: "2rem" ,
      width: "100%"
      }}> 

      <h1>CycleScope Dashboard</h1>

      <Link to="/calendar">Go to Calendar</Link>
      
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

          <h2>Current Cycle Day: {cycleDay}</h2>
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
