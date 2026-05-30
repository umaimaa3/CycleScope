import { nutritionData } from "../data/nutritionData";
import nutrientBenefits from "../data/nutrientBenefits";
import cravingData from "../data/cravingData";

export function getNutritionForPhase(phase) {
  return nutritionData[phase];
}

export function getBenefitForNutrient(nutrient) {
  return nutrientBenefits[nutrient];
}

export function getCravingSuggestions(craving) {
  const search = craving.toLowerCase().trim();

  return cravingData[search] || ["No suggestions found"];
}