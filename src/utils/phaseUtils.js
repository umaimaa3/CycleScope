export function getPhaseForDate(cycleData, date = new Date()) {

    function normalizeDate(d) {
        return new Date(
            d.getFullYear(),
            d.getMonth(),
            d.getDate()
        );
    }

    const cycleLength = Number(cycleData.cycleLength);
    const periodLength = Number(cycleData.periodLength);

    const [year, month, day] = cycleData.lastPeriod.split("-");

    const lastPeriodDate = new Date(
        Number(year),
        Number(month) - 1,
        Number(day)
    );

    const currentDate = normalizeDate(date);

    const diffTime = currentDate - lastPeriodDate;

    const diffDays = Math.round(
        diffTime / (1000 * 60 * 60 * 24)
    );


    const cycleDay =
        ((diffDays % cycleLength) + cycleLength) % cycleLength;


    const ovulationDay =
        Math.max(cycleLength - 14, 10);


    if (cycleDay < periodLength) {
        return "Menstrual";
    }

    if (cycleDay < ovulationDay) {
        return "Follicular";
    }

    if (cycleDay === ovulationDay) {
        return "Ovulation";
    }

    return "Luteal";
}