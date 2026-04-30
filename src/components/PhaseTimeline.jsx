function PhaseTimeline({ cycleDay, cycleLength, phase }) {
  const percentage = (cycleDay / cycleLength) * 100;

  const ovulationDay = cycleLength - 14;
  const menstrualEnd = 5;
  const follicularEnd = ovulationDay - 1;
  const ovulation = ovulationDay;

  const menstrualWidth = (menstrualEnd / cycleLength) * 100;
  const follicularWidth = ((follicularEnd - menstrualEnd) / cycleLength) * 100;
  const ovulationWidth = (1 / cycleLength) * 100;
  const lutealWidth = 100 - (menstrualWidth + follicularWidth + ovulationWidth);

  return (
    <div style={{ marginTop: "2rem" }}>
      <h3>Cycle Progress</h3>

      <div  
        style={{
            position: "relative",
            display: "flex",
            height: "20px",
            borderRadius: "10px",
            overflow: "visible"
        }}
>
        <div style={{ width: `${menstrualWidth}%`, background: "#fca5a5" }} />
        <div style={{ width: `${follicularWidth}%`, background: "#86efac" }} />
        <div style={{ width: `${ovulationWidth}%`, background: "#fde047" }} />
        <div style={{ width: `${lutealWidth}%`, background: "#c4b5fd" }} />

        <div
            style={{
            position: "absolute",
            left: `${percentage}%`,
            top: "-25px",
            transform: "translateX(-50%)",
            fontSize: "12px",
            fontWeight: "bold"
            }}
        >
            {phase}
        </div>

        {/* Marker */}
        <div
            style={{
            position: "absolute",
            left: `${percentage}%`,
            top: "-5px",
            width: "10px",
            height: "30px",
            background: "black",
            transform: "translateX(-50%)"
            }}
         />
        </div>
 

      <p>Day {cycleDay} of {cycleLength}</p>
    </div>
  );
}

export default PhaseTimeline;