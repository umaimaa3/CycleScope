import { useState, useEffect } from "react";

function NutritionPlate({phase}) {
    const recommendations = {
        Menstrual: {
            fruitsVeg: ["Spinach", "Kale", "Red beets", "Seaweed", "Avocado", "Bananas",
                "Broccoli", "Carrots", "Apples", "Sweet potatoes", "Bell peppers", "Oranges",
                "Tomatoes", "Blueberries", "Raspberries", "Strawberries", "Figs"],
            protein: ["Lentils", "Eggs", "Beef", "Flaxseeds" ,"Turkey", "Chickpeas",
                "Salmon", "Mackerel", "Walnuts", "Chia seeds", "Lamb", "Sardines", "Tofu",
                "Edamame", "Tempeh", "Hemp Seeds", "Pumpkin Seeds"],
            grains: ["Oats", "Quinoa", "Whole wheat bread", "Brown rice", "Buckwheat",
                "Whole wheat pasta", "Barley", "Rye"],
            drinks: ["Chamomile tea", "Lemon water", "Ginger tea", "Turmeric Milk", 
                "Pineapple juice", "Peppermint tea", "Almond milk", "Banana Smoothie",
                "Raspberry leaf tea"]
        },
        Follicular: {
            fruitsVeg: ["Broccoli", "Strawberries", "Carrots", "Blueberries", "Raspberries",
                "Oranges", "Grapefruit", "Apples", "Pears", "Cauliflower", "Brussel Sprouts",
                "Cabbage", "Spinach", "Kale", "Celery", "Cucumber", "Sweet potatoes", "Beets"],
            protein: ["Chicken breast", "Yogurt", "Turkey", "Cod", "Trout", "Salmon", "Tuna", 
                "Tofu", "Tempeh", "Chickpeas", "Edamames", "Lentils", "Pumpkin seeds", "Hemp seeds"],
            grains: ["Brown rice", "Whole wheat pasta","Farro", "Whole grain bread", "Quinoa",
                "Oats", "Millet", "Wild Rice"],
            drinks: ["Green tea", "Rosemary tea", "Pomegranate juice", "Dandelion root tea", 
                "Matcha", "Lemon mint water", "Coconut water", "Mixed-berry smoothie", 
                "Citrus spritzer"]
        },
        Ovulation: {
            fruitsVeg: ["Tomatoes", "Spinach", "Kale", "Broccoli", "Oranges", "Grapefruit", 
                "Blueberries", "Raspberries", "Strawberries", "Avocado", "Beetroot"],
            protein: ["Sardines", "Tofu", "Lentils", "Chickpeas", "Black beans", "Edamame", "Quinoa", 
                "Almonds", "Salmon", "Eggs", "Beef", "Lamb", "Shellfish", "Mackerel", "Cod"],
            grains: ["Quinoa", "Barley", "Oats", "Brown rice", "whole grain pasta", "Farro", 
                "Millet", "Corn", "Buckwheat"],
            drinks: ["Lemon water", "Pomegranate juice", "Ginger tea", "Orange juice", "Pineapple juice",
                "Coconut water", "Lemon mint tea", "Rose tea", "Citrus-berry smoothie", "Ginger mocktail"]
        },
        Luteal: {
            fruitsVeg: ["Sweet Potatoes", "Carrots", "Spinach", "Kale", "Swiss chard", "Butternut squash", 
                "Broccoli", "Cauliflower", "Brussel sprouts", "Blueberries", "Strawberries", 
                "Raspberries", "Pineapple", "Pears", "Prunes", "Kiwi", "Pineapple"],
            protein: ["Turkey", "Walnuts", "Chicken", "Eggs", "Black beans", "Chickpeas", "Almonds", 
                "Pumpkin seeds", "Greek yogurt", "Tofu", "Salmon", "Sardines"],
            grains: ["Oats", "Whole wheat pasta", "Quinoa", "Brown rice", "Whole wheat bread", 
                "Barley", "Farro", "Rye", "Millet"],
            drinks: ["Chamomile tea", "Lavender tea", "Turmeric milk", "Ginger tea", "Peppermint tea", 
                "Matcha", "Green tea", "Cacao latte", "Raspberry leaf tea", "Dandelion root tea", 
                "lemon water", "Bone broth", "Cinnamon tea"]
        },
    };


    const data = recommendations[phase];

    const [activeSection, setActiveSection] = useState(null);
    const [hoveredSection, setHoveredSection] = useState(null);

    const [deckState, setDeckState] = useState({
        fruitsVeg: { deck: [], index: 0 },
        protein: { deck: [], index: 0 },
        grains: { deck: [], index: 0 },
        drinks: { deck: [], index: 0 }
    });

    const [shownItems, setShownItems] = useState({
        fruitsVeg: [],
        protein: [],
        grains: [],
        drinks: []
    });

    useEffect(() => {
        setDeckState({
            fruitsVeg: { deck: [], index: 0 },
            protein: { deck: [], index: 0 },
            grains: { deck: [], index: 0 },
            drinks: { deck: [], index: 0 }
        });

         setShownItems({
            fruitsVeg: [],
            protein: [],
            grains: [],
            drinks: []
        });

        setActiveSection(null);
    }, [phase]);


    function drawFromDeck(arr, deckInfo, count) {

        let { deck, index } = deckInfo;

        // Initialize or reshuffle if needed
        if (deck.length === 0 || index + count > deck.length) {
            deck = shuffleArray(arr);
            index = 0;
        }

        const items = deck.slice(index, index + count);

        return {
            items,
            newDeckInfo: {
                deck,
                index: index + count
            }
        };

    }

    function handleMouseMove(e) {
        const rect = e.currentTarget.getBoundingClientRect();

        const x = e.clientX - rect.left;
        const y = e.clientY - rect.top;

        const midX = rect.width / 2;
        const midY = rect.height / 2;

        let section = null;

        if (x > midX) {
            section = "fruitsVeg";
        } else {
            if (y < midY) {
                section = "protein";
            } else {
                section = "grains";
            }
        }

        setHoveredSection(section);
    }



    function handlePlateClick(e) {
        const rect = e.currentTarget.getBoundingClientRect();

        const clickX = e.clientX - rect.left;
        const clickY = e.clientY - rect.top;

        const midX = rect.width / 2;
        const midY = rect.height / 2;

        let section = null;

        if (clickX > midX) {
            // right half 
            section = "fruitsVeg";
        } else {
            // left half
            if (clickY < midY) {
                // top left
                section = "protein";
            } else {
                // bottom left
                section = "grains";
            }
        }

        setActiveSection(prev => {
            const newSection = prev === section ? null : section;

            // generate suggestions immediately when opening
            if (newSection) {
                handleRefresh(newSection);
            }

            return newSection;
        });
    }

    function shuffleArray(arr) {
        const copy = [...arr];

        for (let i = copy.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [copy[i], copy[j]] = [copy[j], copy[i]];
        }

        return copy;
    }

    function handleRefresh(section) {

        let newShown = { ...shownItems };

        setDeckState(prev => {
            const updated = { ...prev };
            const newShown = {};

            if (section === "fruitsVeg") {
                const res = drawFromDeck(data.fruitsVeg, prev.fruitsVeg, 3);
                updated.fruitsVeg = res.newDeckInfo;
                newShown.fruitsVeg = res.items;
            }

            if (section === "protein") {
                const res = drawFromDeck(data.protein, prev.protein, 3);
                updated.protein = res.newDeckInfo;
                newShown.protein = res.items;
            }

            if (section === "grains") {
                const res = drawFromDeck(data.grains, prev.grains, 3);
                updated.grains = res.newDeckInfo;
                newShown.grains = res.items;
            }

            // drinks always tied to active section
            const drinks = drawFromDeck(data.drinks, prev.drinks, 3);
            updated.drinks = drinks.newDeckInfo;
            newShown.drinks = drinks.items;

            setShownItems(prevShown => ({
                ...prevShown,
                ...newShown
            }));

            return updated;
        });

    }

    return(
        <div style={{marginTop: "2rem"}}>
            <h3>Nutrition Reccomendations</h3>

            <p style={{ textAlign: "center", fontSize: "14px" }}>
                 Hover to explore • Click to view recommendations
            </p>

            <div
                onMouseMove={handleMouseMove}
                onMouseLeave={() => setHoveredSection(null)}
                onClick={handlePlateClick}
                style={{
                    width: "300px",
                    height: "300px",
                    borderRadius: "50%",
                    background: "conic-gradient(#86efac 0% 50%, #fde047 50% 75%, #93c5fd 75% 100%)",
                    position: "relative",
                    margin: "2rem auto",
                    cursor: "pointer"
                }}>
                
                <div
                    style={{
                        position: "absolute",
                        top: "45%",
                        left: "70%",
                        transform: "translateX(-50%)",
                        fontWeight: "bold",
                        fontSize: "12px",
                    }}>
                    🥦 Fruits & Veg
                </div>

                <div
                    style={{
                        position: "absolute",
                        top: "25%",
                        left: "15%",
                        fontWeight: "bold",
                        fontSize: "12px"
                    }}>
                    🍗 Protein
                </div>

                <div
                    style={{
                        position: "absolute",
                        bottom: "25%",
                        left: "15%",
                        fontWeight: "bold",
                        fontSize: "12px"
                    }}>
                    🌾 Grains
                </div>

                {hoveredSection === "fruitsVeg" && (
                    <div
                        style={{
                            position: "absolute",
                            top: 0,
                            right: 0,
                            width: "50%",
                            height: "100%",
                            background: "rgba(255,255,255,0.3)",
                            borderTopRightRadius: "150px",
                            borderBottomRightRadius: "150px",
                            pointerEvents: "none"
                        }}
                    />
                )}

                {hoveredSection === "protein" && (
                    <div
                        style={{
                            position: "absolute",
                            top: 0,
                            left: 0,
                            width: "50%",
                            height: "50%",
                            background: "rgba(255,255,255,0.3)",
                            borderTopLeftRadius: "150px",
                            pointerEvents: "none"
                        }}
                    />
                )}

                {hoveredSection === "grains" && (
                    <div
                        style={{
                            position: "absolute",
                            bottom: 0,
                            left: 0, 
                            width: "50%",
                            height: "50%",
                            background: "rgba(255, 255, 255, 0.3)",
                            borderBottomLeftRadius: "150px",
                            pointerEvents: "none"
                        }}
                    />
                )}

            </div>

            {activeSection && (
                <button 
                    onClick={() => handleRefresh(activeSection)}
                    style={{ display: "block", margin: "1rem auto" }}
                >
                    🔄 Refresh Suggestions
                </button>
            )}

            <div style={{ marginTop: "1rem" }}>
                {activeSection === "fruitsVeg" && (
                    <>
                        <h4>🥦 Fruits & Vegetables</h4>
                        <ul>
                            {shownItems.fruitsVeg.map((item, i) => (
                                <li key={i}>{item}</li>
                            ))}
                        </ul>
                    </>
                )}

                {activeSection === "protein" && (
                    <>
                        <h4>🍗 Protein</h4>
                        <ul>
                            {shownItems.protein.map((item, i) => (
                                <li key={i}>{item}</li>
                            ))}
                        </ul>
                    </>
                )}

                {activeSection === "grains" && (
                    <>
                        <h4>🌾 Whole Grains</h4>
                        <ul>
                            {shownItems.grains.map((item, i) => (
                                <li key={i}>{item}</li>
                            ))}
                        </ul>
                    </>
                )}

                {activeSection && (
                    <>
                        <h4>💧 Drinks</h4>
                        <ul>
                            {shownItems.drinks.map((item, i) => (
                                <li key={i}>{item}</li>
                            ))}
                        </ul>
                    </>
                )}

            </div>

        </div>
    );

}

export default NutritionPlate;