
export function getCycleInfo(cycleData, date = new Date()) {
  const cycleLength = Number(cycleData.cycleLength);
  const lastPeriodDate = new Date(cycleData.lastPeriod);

  const diffTime = date - lastPeriodDate;
  const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));

  const cycleDay = ((diffDays % cycleLength) + cycleLength) % cycleLength + 1;
  const ovulationDay = Math.max(cycleLength - 14, 10);
  const daysRemaining = cycleLength - cycleDay;

  const nextPeriodDate = new Date(date);
  nextPeriodDate.setDate(
    nextPeriodDate.getDate() + daysRemaining
  );

  let phase;

  if (cycleDay <= 5) {
    phase = "Menstrual";
  } else if (cycleDay < ovulationDay) {
    phase = "Follicular";
  } else if (cycleDay === ovulationDay) {
    phase = "Ovulation";
  } else {
    phase = "Luteal";
  }

  return {
    cycleDay,
    phase,
    cycleLength,
    nextPeriodDate
  };
}
