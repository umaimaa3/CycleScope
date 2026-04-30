import { useState, useEffect } from "react";

function InputPage() {
  const[cycleLength, setCycleLength] = useState("");
  const[lastPeriod, setLastPeriod] = useState("");

  useEffect(() => {
    const savedData = localStorage.getItem("cycleData");

    if (savedData) {
      const parsedData = JSON.parse(savedData);

      setCycleLength(parsedData.cycleLength);
      setLastPeriod(parsedData.lastPeriod);
    }
  }, []);

  function handleSubmit(e) {
    e.preventDefault();

    const cycleData = {
      cycleLength,
      lastPeriod
    };

    localStorage.setItem("cycleData", JSON.stringify(cycleData));

    console.log(cycleData);
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

      <hr/>

      <h3>Debug View:</h3>
      <p>Cycle Length: {cycleLength}</p>
      <p>Last Period: {lastPeriod}</p>

    </div>
  );
}

export default InputPage;
