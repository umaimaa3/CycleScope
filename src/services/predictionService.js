const API_URL = "http://localhost:8080/api/prediction";

export async function getPrediction() {
    const response = await fetch(API_URL);

    if (!response.ok) {
        throw new Error("Failed to fetch prediction");
    }

    return await response.json();
}