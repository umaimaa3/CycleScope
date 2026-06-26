const API_URL = "http://localhost:8080/api/calendar-events";

export async function getCalendarEvents() {
  const response = await fetch(API_URL);
  return response.json();
}

export async function addCalendarEvent(eventDate, eventText) {
  const response = await fetch(API_URL, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ eventDate, eventText }),
  });

  return response.json();
}

export async function updateCalendarEvent(id, eventText) {
  const response = await fetch(`${API_URL}/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ eventText }),
  });

  return response.json();
}

export async function deleteCalendarEvent(id) {
  await fetch(`${API_URL}/${id}`, {
    method: "DELETE",
  });
}