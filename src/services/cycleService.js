const API_URL = "http://localhost:8080/api/cycle";

export async function getCycleData() {
  const response = await fetch(API_URL);

  if (!response.ok) {
    throw new Error("Failed to fetch cycle data");
  }

  return await response.json();
}

export async function saveCycleData(cycleData) {
  const response = await fetch(API_URL, {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(cycleData)
  });

  if (!response.ok) {
    throw new Error("Failed to save cycle data");
  }

  return await response.json();
}