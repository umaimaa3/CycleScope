import { getCycleInfo } from "../utils/cycleUtils";
import { phaseInfo } from "../data/phaseInfo";
import { useState, useEffect } from "react";
import {
  getCalendarEvents,
  addCalendarEvent,
  updateCalendarEvent,
  deleteCalendarEvent
} from "../services/calendarStorageService";
import { getSavedCycleData } from "../services/cycleStorageService";

function CalendarPage() {
   
    const [cycleData, setCycleData] = useState(null);
    const [loading, setLoading] = useState(true);

    const [selectedDate, setSelectedDate] = useState(new Date());
    const [events, setEvents] = useState([]);
    const [newEvent, setNewEvent] = useState("");
    const [editingIndex, setEditingIndex] = useState(null);
    const [editText, setEditText] = useState("");

    useEffect(() => {
        async function loadData() {
            const cycle = await getSavedCycleData();
            const calendarEvents = await getCalendarEvents();

            setCycleData(cycle);
            setEvents(calendarEvents);
            setLoading(false);
        }

        loadData();
    }, []);

    if (loading) {
        return <p>Loading calendar...</p>;
    }

    if (!cycleData) {
        return <p>No cycle data found.</p>;
    }

    const today = new Date();
    const year = today.getFullYear();
    const month = today.getMonth();

    const daysInMonth = new Date(year, month + 1, 0).getDate();
    const days = Array.from({ length: daysInMonth }, (_, i) => i + 1);

    const weekdays = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
    const firstDayOfMonth = new Date(year, month, 1).getDay();


    function getPhaseColor(phase) {
        switch (phase) {
            case "Menstrual":
                return "#fcd5ce";
            case "Follicular":
                return "#d0f4de";
            case "Ovulation":
                return "#fff3b0";
            case "Luteal":
                return "#e4c1f9";
            default:
                return "#eee";
        }
    }

    function getEventsForDate(date) {
        const key = date.toDateString();
        return events.filter((event) => event.eventDate === key);
    }
  
    async function handleAddEvent() {
        if (!newEvent || !selectedDate) return;

        const key = selectedDate.toDateString();

        const savedEvent = await addCalendarEvent(key, newEvent);

        setEvents([...events, savedEvent]);
        setNewEvent("");
    }
   
    async function handleDeleteEvent(id) {
        await deleteCalendarEvent(id);

        setEvents(events.filter((event) => event.id !== id));
    }

    async function handleEditEvent(id) {
        const updatedEvent = await updateCalendarEvent(id, editText);

        setEvents(
            events.map((event) =>
                event.id === id ? updatedEvent : event
            )
        );

        setEditingIndex(null);
        setEditText("");
    }

    return (
    <div style={{ padding: "2rem" }}>
        <h1>Calendar</h1>

        {/* TOP SECTION */}
        <div
            style={{
                display: "flex",
                gap: "2rem",
                alignItems: "flex-start",
                marginTop: "1.5rem",
                flexWrap: "wrap"
            }}
        >
        
            {/* Calendar */}
            <div style={{ flex: "3" }}>

                {/* Weekday Header */}
                <div
                    style={{
                        display: "grid",
                        gridTemplateColumns: "repeat(7, 1fr)",
                        gap: "10px",
                        marginTop: "1.5rem",
                        marginBottom: "10px",
                        textAlign: "center",
                        fontWeight: "bold"
                    }}
                >
                    {weekdays.map((day) => (
                        <div key={day}>{day}</div>
                    ))}
                </div>

                <div
                    style={{
                        display: "grid",
                        gridTemplateColumns: "repeat(7, 1fr)",
                        gap: "10px",
                        marginTop: "1.5rem"
                    }}
                >
                    {Array.from({ length: firstDayOfMonth }).map((_, i) => (
                        <div key={`empty-${i}`}></div>
                    ))}

                    {days.map((day) => {
                        const date = new Date(year, month, day);
                        const { phase } = getCycleInfo(cycleData, date);
                        const isSelected =
                            selectedDate &&
                            selectedDate.toDateString() === date.toDateString();
                        const dateKey = date.toDateString();
                        const dayEvents = getEventsForDate(date);

                        return (
                            <div
                                key={day}
                                onClick={() => setSelectedDate(date)}   
                                style={{
                                    cursor: "pointer",
                                    padding: "10px",
                                    borderRadius: "8px",
                                    textAlign: "left",
                                    background: getPhaseColor(phase),
                                    boxShadow: isSelected ? "0 0 0 2px rgba(0, 0, 0, 0.55)" : "none",
                                    transition: "0.2s"
                                }}
                                
                            >
                                <strong>{day}</strong>
                                <div style={{ fontSize: "0.8rem" }}>{phase}</div>

                                <ul
                                    style={{
                                        marginTop: "5px",
                                        padding: 0,
                                        listStyle: "none",
                                        fontSize: "0.7rem"
                                    }}
                                >
                                    {dayEvents.slice(0, 2).map((event, i) => (
                                        <li
                                            key={i}
                                            style={{
                                                display: "flex",
                                                alignItems: "flex-start",
                                                gap: "4px",
                                                marginBottom: "2px"
                                            }}
                                        >
                                            <span>•</span>

                                            <span
                                                style={{
                                                    wordBreak: "break-word",
                                                    lineHeight: "1.2"
                                                }}
                                            >
                                                {event.eventText}
                                            </span>
                                        </li>
                                    ))}

                                    {dayEvents.length > 2 && <li>...</li>}
                                </ul>
                            </div>
                        );
                    })}
                </div>
            </div>

            {/* Task Panel */}

            <div
                style={{
                    flex: "0.8",
                    minWidth: "250px",
                    background: "#f9fafb",
                    padding: "1rem",
                    borderRadius: "12px"
                }}
            >
                {selectedDate && (
                    <div style={{ marginTop: "2rem", padding: "1rem", background: "#f9fafb", borderRadius: "12px" }}>
                        <h2>{selectedDate.toDateString()}</h2>

                        <div
                            style={{
                            display: "flex",
                            gap: "8px",
                            marginTop: "1rem"
                            }}
                        >
                            <input
                                value={newEvent}
                                onChange={(e) => setNewEvent(e.target.value)}
                                placeholder="Add a task..."
                                style={{
                                    flex: 1,
                                    padding: "0.5rem",
                                    borderRadius: "6px",
                                    border: "1px solid #ccc"
                                }}
                            />

                            <button
                                onClick={handleAddEvent}
                                style={{
                                    border: "none",
                                    background: "#e2e8f0",
                                    borderRadius: "6px",
                                    padding: "0 12px",
                                    cursor: "pointer",
                                    flexShrink: 0
                                }}
                            >
                                Add
                            </button>
                        </div>

                        <ul 
                            style={{ 
                                marginTop: "1rem",
                                padding: 0,
                                listStyle: "none",
                                maxHeight: "300px",
                                overflowY: "auto",
                                paddingRight: "5px"
                            }}
                        >
                            {getEventsForDate(selectedDate).map((event, i) => (
                                <li
                                    key={i}
                                    style={{
                                        display: "flex",
                                        alignItems: "flex-start",
                                        gap: "10px",
                                        marginBottom: "10px",
                                        padding: "8px",
                                        background: "white",
                                        borderRadius: "8px"
                                    }}
                                >
                                    {editingIndex === i ? (
                                        <>
                                            <input
                                                value={editText}
                                                onChange={(e) => setEditText(e.target.value)}
                                                style={{
                                                    flex: 1,
                                                    minWidth: 0,
                                                    padding: "0.5rem",
                                                    borderRadius: "6px",
                                                    border: "1px solid #cbd5e1",
                                                    fontSize: "0.9rem"
                                                }}
                                            />

                                            <div
                                                style={{
                                                    display: "flex",
                                                    gap: "5px",
                                                    flexShrink: 0
                                                }}
                                            >
                                                <button
                                                    onClick={() => handleEditEvent(event.id)}
                                                    style={{
                                                        border: "none",
                                                        background: "#dbeafe",
                                                        borderRadius: "6px",
                                                        cursor: "pointer",
                                                        padding: "6px 10px",
                                                        fontSize: "0.85rem"
                                                    }}
                                                >
                                                    Save
                                                </button>
                                            </div>
                                        </>
                                    ) : (
                                        <>
                                            {/* TEXT */}
                                            <span
                                                style={{
                                                    flex: 1,
                                                    wordBreak: "break-word",
                                                    fontSize: "0.9rem"
                                                }}
                                            >
                                                {event.eventText}
                                            </span>

                                            {/* BUTTONS */}
                                            <div
                                                style={{
                                                    display: "flex",
                                                    gap: "5px",
                                                    flexShrink: 0
                                                }}
                                            >
                                                <button
                                                    onClick={() => {
                                                        setEditingIndex(i);
                                                        setEditText(event.eventText);
                                                    }}
                                                    style={{
                                                        border: "none",
                                                        background: "#f1f5f9",
                                                        borderRadius: "6px",
                                                        cursor: "pointer",
                                                        padding: "4px 6px"
                                                    }}
                                                >
                                                    ✎
                                                </button>

                                                <button
                                                    onClick={(e) => {
                                                        e.stopPropagation();
                                                        handleDeleteEvent(event.id);
                                                    }}
                                                    style={{
                                                        border: "none",
                                                        background: "#f1f5f9",
                                                        borderRadius: "6px",
                                                        cursor: "pointer",
                                                        padding: "4px 6px"
                                                    }}
                                                >
                                                    ×
                                                </button>
                                            </div>
                                        </>
                                    )}
                                </li>
                            ))}
                        </ul>
                    </div>
                )}
            </div>
        </div>

        <div
            style={{
                marginTop: "2rem",
                padding: "1.5rem",
                background: "#f8fafc",
                borderRadius: "16px",
                boxShadow: "0 2px 8px rgba(0,0,0,0.05)"
            }}
        >

            <h2 style={{ marginBottom: "1rem" }}>Cycle Phase Guide</h2>

            <div
                style={{
                    display: "grid",
                    gridTemplateColumns: "repeat(auto-fit, minmax(200px, 1fr))",
                    gap: "1rem"
                }}
            >
                {Object.entries(phaseInfo).map(([phase, info]) => (
                    <div
                        key={phase}
                        style={{
                            padding: "1rem",
                            borderRadius: "12px",
                            background: "white",
                            border: `3px solid ${getPhaseColor(phase)}`
                        }}
                    >
                        {/* Color + Title */}
                        <div style={{ display: "flex", alignItems: "center", gap: "8px" }}>
                            <div
                                style={{
                                    width: "12px",
                                    height: "12px",
                                    borderRadius: "50%",
                                    background: getPhaseColor(phase)
                                }}
                            />
                            <strong>{phase}</strong>
                        </div>

                        {/* Message */}
                        <p style={{ marginTop: "0.5rem", fontSize: "0.9rem" }}>
                            {info.message}
                        </p>
                    </div>
                ))}
            </div>
        </div>
    </div>
  );
}

export default CalendarPage;

