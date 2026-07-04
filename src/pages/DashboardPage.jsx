import { useState, useEffect } from "react";
import PhaseTimeline from "../components/PhaseTimeline";
import NutritionPlate from "../components/nutrition/NutritionPlate";
import { phaseInfo } from "../data/phaseInfo";
import { Link } from "react-router-dom";
import { getSavedCycleData } from "../services/cycleStorageService";
import { getCurrentCycleInfo } from "../services/cycleService";
import {
  getSymptomLogs,
  addSymptomLog,
  deleteSymptomLog
} from "../services/symptomLogService";

function DashboardPage() {

  const [cycleData, setCycleData] = useState(null);
  const [loading, setLoading] = useState(true);

  const [symptomLogs, setSymptomLogs] = useState([]);
  const [pendingSymptom, setPendingSymptom] = useState(null);
  const [editingLog, setEditingLog] = useState(null);

  const symptomOptions = [
    "Cramps",
    "Headache",
    "Fatigue",
    "Bloating",
    "Mood changes",
    "Back pain",
    "Acne",
    "Cravings",
    "Nausea"
  ];

  useEffect(() => {
    async function loadData() {
      const cycle = await getSavedCycleData();
      const symptoms = await getSymptomLogs();

      setCycleData(cycle);
      setSymptomLogs(symptoms);
      setLoading(false);
    }
    loadData();
  }, []);

  if (loading) {
    return <p>Loading cycle data...</p>;
  }

  if (!cycleData) {
    return <p>No cycle data found. Please enter your cycle first.</p>;
  }

  const { cycleDay, phase, cycleLength, nextPeriodDate} = getCurrentCycleInfo(cycleData);

  const currentPhaseInfo = phaseInfo[phase];

  function formatDate(dateString) {
    const [year, month, day] = dateString.split("-");
    return `${month}/${day}/${year}`;
  }

  const todayKey = new Date().toDateString();

  const todaysSymptoms = symptomLogs.filter(
    (log) => log.logDate === todayKey
  );

  async function handleAddSymptom(symptom, intensity) {
    const savedSymptom = await addSymptomLog(
        todayKey,
        symptom,
        intensity
    );

    setSymptomLogs([...symptomLogs, savedSymptom]);

    setPendingSymptom(null);
  }

  async function handleDeleteSymptom(id) {
    await deleteSymptomLog(id);

    setSymptomLogs(symptomLogs.filter((log) => log.id !== id));
  }

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

        {/* Info Card */}

        <div
          style={{
            flex: "1 1 0",
            background: "#f9fafb",
            padding: "1.5rem",
            borderRadius: "12px"
          }}
        >

          <p>Cycle Length: {cycleData.cycleLength} days</p>
          <p>Last Period Start Date: {formatDate(cycleData.lastPeriod)}</p>
          <p>
             Next Predicted Period:{" "}
            {nextPeriodDate instanceof Date
              ? nextPeriodDate.toLocaleDateString()
              : "Not available"}
          </p>

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

          <h2>Cycle Day {cycleDay + 1}</h2>
          <h2>Current Phase: {phase}</h2>

          <h3 style={{ marginTop: "1rem" }}>How you might feel today</h3>
          <ul>
            {currentPhaseInfo.symptoms.map((symptom, index) => (
              <li key={index}>{symptom}</li>
            ))}
          </ul>
          <p>{currentPhaseInfo.message}</p>

          <PhaseTimeline 
          cycleDay={cycleDay + 1} 
          cycleLength={cycleLength} 
          phase={phase}
          />
        </div>
        
        {/* Right Side*/}

        <div
          style={{
            flex: "1 1 0",
            display: "flex",
            flexDirection: "column",
            gap: "1.5rem"
          }}
        >

          {/* Symptoms Card*/}
          <div
            style={{
              background: "#f9fafb",
              padding: "1.5rem",
              borderRadius: "12px"
            }}
          >

            <h2>Today's Symptom Log</h2>

            {pendingSymptom && (
              <div style={{ marginBottom: "1rem" }}>
                <p style={{ fontWeight: "600", marginBottom: "0.5rem" }}>
                  How would you rate this today?
                </p>

                <div style={{ display: "flex", gap: "8px" }}>
                  {[1, 2, 3, 4, 5].map((level) => (
                    <button
                      key={level}
                      onClick={async () => {
                        // EDIT FLOW
                        if (editingLog) {
                          await deleteSymptomLog(editingLog.id);

                          const saved = await addSymptomLog(
                            todayKey,
                            pendingSymptom,
                            level
                          );

                          setSymptomLogs((prev) => [
                            ...prev.filter((l) => l.id !== editingLog.id),
                            saved
                          ]);
                        }

                        // ADD FLOW
                        else {
                          const saved = await addSymptomLog(
                            todayKey,
                            pendingSymptom,
                            level
                          );

                          setSymptomLogs((prev) => [...prev, saved]);
                        }

                        setPendingSymptom(null);
                        setEditingLog(null);
                      }}
                      style={{
                        border: "none",
                        borderRadius: "999px",
                        padding: "6px 10px",
                        cursor: "pointer",
                        background: "#e2e8f0"
                      }}
                    >
                      {level}
                    </button>
                  ))}
                </div>
              </div>
            )}

            <div style={{ display: "flex", flexWrap: "wrap", gap: "8px", marginTop: "1rem" }}>

              {symptomOptions.map((symptom) => {
                const existingLog = todaysSymptoms?.find(
                  (log) => log.symptom === symptom
                );

                const isSelected = Boolean(existingLog);

                return (
                  <button
                    key={symptom}
                      onClick={() => {
                        setPendingSymptom(symptom);
                        setEditingLog(existingLog || null);
                      }}
                      style={{
                        border: "none",
                        borderRadius: "999px",
                        padding: "8px 12px",
                        cursor: "pointer",
                        background: isSelected ? "#d8b4fe" : "#e2e8f0",
                        fontWeight: isSelected ? "600" : "400",
                        display: "flex",
                        alignItems: "center",
                        gap: "6px"
                      }}
                    >
                      {/* symptom label + intensity */}
                      <span>
                        {isSelected && existingLog?.intensity
                          ? `${symptom} • ${existingLog.intensity}`
                          : symptom}
                      </span>

                      {/* DELETE BUTTON */}
                      {isSelected && (
                        <span
                          onClick={(e) => {
                            e.stopPropagation(); // prevents opening edit modal
                            handleDeleteSymptom(existingLog.id);
                          }}
                          style={{
                            marginLeft: "4px",
                            fontSize: "14px",
                            cursor: "pointer",
                            opacity: 0.7
                          }}
                        >
                          ✕
                        </span>
                      )}
                    
                  </button>
                );
              })}
            </div>
          </div>

          {/* Nutrition Card*/}
          <div
            style={{
              background: "#f9fafb",
              padding: "1.5rem",
              borderRadius: "12px"
            }}
          >
              <NutritionPlate phase={phase} />
          </div>

        </div>

      </div>

    </div>

  );
}

export default DashboardPage;
