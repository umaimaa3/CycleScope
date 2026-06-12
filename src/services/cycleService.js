import { getCycleInfo } from "../utils/cycleUtils";

export function getCurrentCycleInfo(cycleData, date = new Date()) {
  return getCycleInfo(cycleData, date);
}