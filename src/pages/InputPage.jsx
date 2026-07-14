import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { saveCycleData, getCycleData } from "../services/cycleService";

function InputPage() {

  const navigate = useNavigate();

  const[cycleLength, setCycleLength] = useState(28);
  const[periodLength, setPeriodLength] = useState(5);
  const[lastPeriod, setLastPeriod] = useState("");

   useEffect(() => {
    async function loadCycleData() {
      const savedData = await getCycleData();

      if (savedData) {
        setCycleLength(savedData.cycleLength);
        setPeriodLength(savedData.periodLength);
        setLastPeriod(savedData.lastPeriod);
      }
    }

    loadCycleData();
  }, []);

  async function handleSubmit(e) {
    e.preventDefault();

    const cycleData = {
      cycleLength,
      periodLength,
      lastPeriod
    };

    await saveCycleData(cycleData);

    navigate("/");

  }

  return (
    <div style={{ padding: "2rem" }}>
      <h1>Cycle Setup</h1>

      <form onSubmit={handleSubmit}>

      <div style={{ marginBottom: "1rem" }}>
        <label>Cycle Length (days): </label>
        <input
          type="number"
          value={cycleLength}
          onChange={(e) => setCycleLength(e.target.value)}
        />
      </div>

      <div style={{ marginBottom: "1rem" }}>
        <label>Period Length (days): </label>
        <input
          type="number"
          value={periodLength}
          onChange={(e) => setPeriodLength(e.target.value)}
        />
      </div>

      <div style={{ marginBottom: "1rem"}}>
        <label>Last Period Start Date: </label>
        <input
          type="date"
          value={lastPeriod}
          onChange={(e) => setLastPeriod(e.target.value)}
        />
      </div>

      <button type="submit">Save Cycle</button>
      </form>

    </div>
  );
}

export default InputPage;
