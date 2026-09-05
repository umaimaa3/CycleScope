package backend.model;

/**
 * Represents the current state of a cycle within CycleScope's lifecycle
 *
 * The status is used to determine how the application should handle and
 * display a cycle as it moves from prediction to user confirmation and
 * eventual completion
*/

public enum CycleStatus {
    PREDICTED,
    WAITING_FOR_START_CONFIRMATION,
    ACTIVE,
    WAITING_FOR_END_CONFIRMATION,
    COMPLETED,
    START_CONFIRMATION_EXPIRED,
    END_CONFIRMATION_EXPIRED
}
