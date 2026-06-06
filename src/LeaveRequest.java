import java.time.LocalDate;
import java.time.LocalDateTime; // Keep for now, might be removed later if not used
import java.time.ZonedDateTime;
import java.time.ZoneOffset; // For UTC
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public abstract class LeaveRequest implements Approvable {
    private int requestId;
    private Employee employee;
    private LocalDate startDate; // Changed from String to LocalDate
    private LocalDate endDate;   // Changed from String to LocalDate
    private String status; // "Pending", "Approved", "Denied"
    private String reason;
    private ZonedDateTime createdAt; // Changed from LocalDateTime to ZonedDateTime

    // Constructor
    public LeaveRequest(){
        this.requestId = 0;
        this.employee = new Employee();
        this.startDate = LocalDate.now(); // Default to current date
        this.endDate = LocalDate.now().plusDays(1); // Default to next day
        this.status = "Pending";
        this.reason = "UnknownReason";
        this.createdAt = ZonedDateTime.now(ZoneOffset.UTC); // Initialize with UTC
    }

    public LeaveRequest(int requestId, Employee employee, LocalDate startDate, // Changed type
                        LocalDate endDate, String reason) { // Changed type
        this.requestId = requestId;
        this.employee = employee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = "Pending"; // Default status
        this.reason = reason;
        this.createdAt = ZonedDateTime.now(ZoneOffset.UTC); // Initialize with UTC

        // Basic validation for dates
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }
    }

    @Override
    public boolean approve(String approverName){
        if (!this.status.equals("Approved")) { // Only change if not already approved
            changeStatus("Approved", approverName);
            System.out.println("Request " + requestId + " Approved by " + approverName);
            return true;
        }
        System.out.println("Request " + requestId + " is already Approved.");
        return false;
    }

    @Override
    public boolean deny(String approverName, String denyReason){ // Renamed reason to denyReason to avoid conflict
        if (!this.status.equals("Denied")) { // Only change if not already denied
            changeStatus("Denied", approverName);
            this.reason = denyReason; // Update the reason for denial
            System.out.println("Request " + requestId + " Denied by " + approverName + ": " + denyReason);
            return false;
        }
        System.out.println("Request " + requestId + " is already Denied.");
        return true; // Still return true for the action of denying, but false for the request itself
    }

    // Abstract method that subclasses must implement
    public abstract int calculateLeaveDays();

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDate getStartDate(){ // Changed return type
        return startDate;
    }

    public void setStartDate(LocalDate startDate) { // Changed parameter type
        this.startDate = startDate;
        // Re-validate if endDate is already set
        if (this.endDate != null && this.endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }
    }

    public LocalDate getEndDate(){ // Changed return type
        return endDate;
    }

    public void setEndDate(LocalDate endDate) { // Changed parameter type
        // Validation
        if (this.startDate != null && endDate.isBefore(this.startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ZonedDateTime getCreatedAt() { // Changed return type
        return createdAt;
    }

    public String getCreationTimestampFormatted() { // New method for formatted output
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        return createdAt.format(formatter);
    }

    public void display(){
        System.out.println("Request ID: " + getRequestId());
        System.out.println("Employee: " + getEmployee().getName());
        System.out.println("Start Date: " + getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE)); // Formatted output
        System.out.println("End Date: " + getEndDate().format(DateTimeFormatter.ISO_LOCAL_DATE));     // Formatted output
        System.out.println("Status: " + getStatus());
        System.out.println("Reason: " + getReason());
        System.out.println("Creation Timestamp: " + getCreationTimestampFormatted() + "\n"); // Formatted output
    }

    // Removed stringToDate as it's no longer needed

    public int remainingLeaveBalance(){
        // This method needs to be re-evaluated as it depends on how leave balance is managed
        // For now, it will use the new getDuration()
        long daysBetween = getDuration(); // Use the new duration calculation
        int remainingBalance = Math.toIntExact(employee.getLeaveBalance() - daysBetween);
        System.out.println("Remaining leave balance: " + remainingBalance);
        return remainingBalance;
    }

    public boolean processRequest() {
        System.out.println("Processing generic leave request...");
        return true;
    }

    public int getDuration(){
        // Calculate duration using LocalDate
        return (int) ChronoUnit.DAYS.between(startDate, endDate) + 1; // +1 to include both start and end day
    }

    private ArrayList<StatusChange> statusHistory = new ArrayList<>();

    public ArrayList<StatusChange> getStatusHistory(){
        return statusHistory;
    }

    // Inner class to track status changes
    public class StatusChange {
        private String oldStatus;
        private String newStatus;
        private ZonedDateTime changeTimestamp; // Changed from String to ZonedDateTime
        private String changedBy;

        public StatusChange(String oldStatus, String newStatus,
                            ZonedDateTime changeTimestamp, String changedBy) { // Changed type
            this.oldStatus = oldStatus;
            this.newStatus = newStatus;
            this.changeTimestamp = changeTimestamp;
            this.changedBy = changedBy;
        }

        public String getOldStatus(){
            return oldStatus;
        }
        public String getNewStatus(){
            return newStatus;
        }
        public ZonedDateTime getChangeTimestamp(){ // Changed return type
            return changeTimestamp;
        }
        public String getChangedBy(){
            return changedBy;
        }

        public String getChangeTimestampFormatted() { // New method for formatted output
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
            return changeTimestamp.format(formatter);
        }

        @Override
        public String toString() { // Override toString for better display
            return "StatusChange{" +
                   "oldStatus='" + oldStatus + '\'' +
                   ", newStatus='" + newStatus + '\'' +
                   ", changeTimestamp=" + getChangeTimestampFormatted() +
                   ", changedBy='" + changedBy + '\'' +
                   '}';
        }
    }

    // Method to change status and record the change
    public void changeStatus(String newStatus, String changedBy) {
        String oldStatus = this.status;
        this.status = newStatus;

        // Create a new status change record with ZonedDateTime
        StatusChange change = new StatusChange(
                oldStatus, newStatus, ZonedDateTime.now(ZoneOffset.UTC), changedBy);
        statusHistory.add(change);
    }
}