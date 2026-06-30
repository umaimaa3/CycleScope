const API_URL = "http://localhost:8080/api/symptoms";

export async function getSymptomLogs() {
  const response = await fetch(API_URL);
  return response.json();
}

export async function addSymptomLog(logDate, symptom, intensity) {
  const response = await fetch(API_URL, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ logDate, symptom, intensity }),
  });

  return response.json();
}

export async function deleteSymptomLog(id) {
  await fetch(`${API_URL}/${id}`, {
    method: "DELETE",
  });
}