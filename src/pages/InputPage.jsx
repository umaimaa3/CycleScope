import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { saveCycleData, getSavedCycleData } from "../services/cycleStorageService";

function InputPage() {

  const navigate = useNavigate();

  const[cycleLength, setCycleLength] = useState("");
  const[lastPeriod, setLastPeriod] = useState("");

  useEffect(() => {
    const savedData = getSavedCycleData();

    if (savedData) {
      setCycleLength(savedData.cycleLength);
      setLastPeriod(savedData.lastPeriod);
    }
  }, []);

  function handleSubmit(e) {
    e.preventDefault();

    const cycleData = {
      cycleLength,
      lastPeriod
    };

    saveCycleData(cycleData);

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
