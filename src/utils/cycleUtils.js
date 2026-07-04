
export function getCycleInfo(cycleData, date = new Date()) {

  function normalizeDate(d) {
    return new Date(d.getFullYear(), d.getMonth(), d.getDate());
  }

  const cycleLength = Number(cycleData.cycleLength);

  const lastPeriodDate = normalizeDate(new Date(cycleData.lastPeriod));
  const today = normalizeDate(date);

  const diffTime = today - lastPeriodDate;
  const diffDays = Math.round(diffTime / (1000 * 60 * 60 * 24));

  const cycleDay = ((diffDays % cycleLength) + cycleLength) % cycleLength;
  const ovulationDay = Math.max(cycleLength - 14, 10);

  const nextPeriodDate = new Date(lastPeriodDate);

  while (nextPeriodDate <= today) {
    nextPeriodDate.setDate(nextPeriodDate.getDate() + cycleLength);
  }

  let phase;

  if (cycleDay <= 4) {
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
