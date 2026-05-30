export function getSavedCycleData() {
  const savedData = localStorage.getItem("cycleData");

  if (!savedData) {
    return null;
  }

  return JSON.parse(savedData);
}

export function saveCycleData(cycleData) {
  localStorage.setItem("cycleData", JSON.stringify(cycleData));
}