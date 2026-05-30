export function getCalendarEvents() {
  const savedEvents = localStorage.getItem("calendarEvents");

  if (!savedEvents) {
    return {};
  }

  return JSON.parse(savedEvents);
}

export function saveCalendarEvents(events) {
  localStorage.setItem("calendarEvents", JSON.stringify(events));
}