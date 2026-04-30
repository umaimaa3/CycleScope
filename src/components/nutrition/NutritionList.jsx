function NutritionList({ phase }) {

  const nutritionMap = {
    Menstrual: [
      "Iron-rich foods (spinach, lentils)",
      "Warm soups",
      "Dark chocolate"
    ],
    Follicular: [
      "Leafy greens",
      "Eggs",
      "Fermented foods (yogurt, kimchi)"
    ],
    Ovulation: [
      "Fruits (berries)",
      "Light proteins",
      "Salads"
    ],
    Luteal: [
      "Whole grains",
      "Magnesium-rich foods (nuts, seeds)",
      "Root vegetables"
    ]
  };

  const nutritients = nutritionMap[phase] || [];

  return (
    <div style={{ marginTop: "2rem" }}>
      <h3>Nutrition Recommendations</h3>

      <ul>
        {nutritients.map((nutrition, index) => (
          <li key={index}>{nutrition}</li>
        ))}
      </ul>
    </div>
  );
}

export default NutritionList;