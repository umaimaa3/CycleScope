package backend.dto;
import java.time.LocalDate;

// Request DTO containing the date provided when a user confirms their period end
public record ConfirmPeriodEndRequest (LocalDate actualEndDate) {

}
