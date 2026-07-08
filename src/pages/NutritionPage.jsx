import { getPrediction } from "../services/predictionService";
import {
  getNutritionForPhase,
  getBenefitForNutrient,
  getCravingSuggestions
} from "../services/nutritionService";
import { useState, useEffect } from "react";


function NutritionPage() {

    const [prediction, setPrediction] = useState(null);
    const [loading, setLoading] = useState(true);

    const [craving, setCraving] = useState("");
    const [results, setResults] = useState([]);

    
    useEffect(() => {
        async function loadPrediction() {
            const data = await getPrediction();

            setPrediction(data);
            setLoading(false);
        }

        loadPrediction();
    }, []);


    if (loading) {
        return <p>Loading nutrition...</p>;
    }

    if (!prediction) {
        return <p>No cycle data found.</p>;
    }

    const phase = prediction.phase;
    const phaseData = getNutritionForPhase(phase);
    

    function handleSearch() {
        setResults(getCravingSuggestions(craving));
    }

    return (
        <div
            style={{
                display: "grid",
                gridTemplateColumns: "1fr 1fr",
                gap: "20px",
                marginTop: "20px"
            }}
        >

            {/* Card 1 */}
            <div
                style={{
                    background: "#f9fafb",
                    padding: "1.5rem",
                    borderRadius: "12px",
                }}
            >

                {/* Overview */}
                <h2>Phase Nutrition</h2>

                <p>
                    {phaseData.overview}
                </p>

                {/* Nutrients Section */}
                <div>
                    <h3>Recommended Nutrients</h3>
                    <div
                        style={{
                            display: "flex",
                            flexWrap: "wrap",
                            gap: "8px",
                            marginTop: "0.5rem"
                        }}
                    >
                        {phaseData.nutrients.map((nutrient) => (
                            <div 
                                key={nutrient}
                                
                                style={{
                                    background: "#f3d9fa",
                                    padding: "6px 12px",
                                    borderRadius: "999px",
                                    fontSize: "0.9rem"
                                }}
                            >
                                {nutrient}
                            </div>
                        ))}
                    </div>
                </div>

                {/* Ingredients Section */}
                <div>
                    <h3>Helpful Ingredients</h3>

                    <div
                        style={{
                            display: "flex",
                            flexWrap: "wrap",
                            gap: "8px",
                            marginTop: "0.5rem"
                        }}
                    >
                        {phaseData.ingredients.map((ingredient) => (
                            <div
                                key={ingredient}

                                style={{
                                    background: "#e0d9fa",
                                    padding: "6px 12px",
                                    borderRadius: "999px",
                                    fontSize: "0.9rem"
                                }}
                            >
                                {ingredient}
                            </div>
                        ))}
                    </div>
                </div>

            </div>

            {/* Card 2 */}
            <div
                style={{
                    background: "#f9fafb",
                    padding: "1.5rem",
                    borderRadius: "12px"
                }}
            >
                <h2>Why These Help</h2>

                <div
                    style={{
                        display: "flex",
                        flexDirection: "column",
                        gap: "0.5rem",
                        marginTop: "1rem"
                    }}
                >

                    {phaseData.nutrients.map((nutrient) => (

                        <div
                            key={nutrient}
                            style={{
                                background: "white",
                                padding: "0.65rem 0.85rem",
                                borderRadius: "10px"
                            }}
                        >

                            <h3
                                style={{
                                    margin: "0",
                                    fontSize: "1rem"
                                }}
                            >
                                {nutrient}
                            </h3>

                            <p
                                style={{
                                    marginTop: "0.35rem",
                                    marginBottom: "0",
                                    lineHeight: "1.4",
                                    fontSize: "0.95rem"
                                }}
                            >
                                {getBenefitForNutrient(nutrient)}
                            </p>

                        </div>

                    ))}

                </div>

            </div>

            {/* Card 3 */}

            <div
                style={{
                    background: "#f9fafb",
                    padding: "1.5rem",
                    borderRadius: "12px"
                }}
            >

                <h2>Meal Suggestions</h2>

                {Object.entries(phaseData.meals).map(([mealType, meals]) => (

                    <div
                        key={mealType}
                        style={{
                            marginTop: "1rem"
                        }}
                    >

                        <h3
                            style={{
                                marginBottom: "0.5rem"
                            }}
                        >
                            {mealType.charAt(0).toUpperCase() + mealType.slice(1)}
                        </h3>

                        <div
                            style={{
                                display: "flex",
                                flexWrap: "wrap",
                                gap: "8px"
                            }}
                        >

                            {meals.map((meal) => (

                                <div
                                    key={meal}
                                    style={{
                                        background: "white",
                                        padding: "8px 12px",
                                        borderRadius: "999px",
                                        fontSize: "0.95rem"
                                    }}
                                >
                                    {meal}
                                </div>

                            ))}

                        </div>

                    </div>

                ))}

            </div>


            
            {/* Card 4 */}
            <div
                style={{
                    background: "#f9fafb",
                    padding: "1.5rem",
                    borderRadius: "12px"
                }}
            >

                <h2>Cravings Assistant</h2>

                <p>
                    What are you craving today?
                </p>

                <input
                    type="text"
                    placeholder="Chocolate, salty, sweet..."
                    value={craving}
                    onChange={(e) => setCraving(e.target.value)}
                    style={{
                        width: "100%",
                        padding: "10px",
                        marginTop: "1rem",
                        borderRadius: "8px",
                        border: "1px solid #ccc"
                    }}
                />

                <button
                    onClick={handleSearch}

                    onMouseEnter={(e) => {
                        e.target.style.boxShadow =
                            "0 0 8px rgba(216, 180, 254, 0.5)";
                    }}

                    onMouseLeave={(e) => {
                        e.target.style.boxShadow = "none";
                    }}

                    style={{
                        marginTop: "1rem",
                        padding: "6px 12px",
                        borderRadius: "10px",
                        border: "none",
                        background: "#9c9cf4dd",
                        color: "#2e2e2e",
                        fontWeight: "600",
                        cursor: "pointer",
                        transition: "0.2s"
                    }}
                > 
                    Get Suggestions 
                </button>

                <div
                    style={{
                        marginTop: "1rem",
                        display: "flex",
                        flexDirection: "column",
                        gap: "0.5rem"
                    }}
                >

                    {results.map((item) => (

                        <div
                            key={item}
                            style={{
                                background: "white",
                                padding: "0.75rem",
                                borderRadius: "8px"
                            }}
                        >
                            {item}
                        </div>

                    ))}

                </div>

            </div>

        </div>
    );
    
}

export default NutritionPage;