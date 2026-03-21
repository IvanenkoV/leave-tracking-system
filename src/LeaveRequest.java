import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public abstract class LeaveRequest implements Approvable {
    private int requestId;
    private Employee employee;
    private String startDate;
    private String endDate;
    private String status; // "Pending", "Approved", "Denied"
    private String reason;

    // Constructor
    public LeaveRequest(){
        this.requestId = 0;
        this.employee = new Employee();
        this.startDate = "UnknownStartDate";
        this.endDate = "UnknownEndDate";
        this.status = "Pending";
        this.reason = "UnknownReason";
    }
    public LeaveRequest(int requestId, Employee employee, String startDate,
                        String endDate, String reason) {
        this.requestId = requestId;
        this.employee = employee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = "Pending"; // Default status
        this.reason = reason;
    }


    @Override
    public boolean approve(String approverName){
        System.out.println("Approved by " + approverName);
        return true;
    }
    @Override
    public boolean deny(String approverName, String reason){
        System.out.println("Denied by " + approverName + ": " + reason);
        return false;
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


    public String getStartDate(){
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }


    public String getEndDate(){
        return endDate;
    }

    public void setEndDate(String endDate) {
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


    // Methods will be added here

    public void display(LeaveRequest leaveRequest){
        System.out.println("Request ID: " + leaveRequest.getRequestId());
        System.out.println("Employee: " + leaveRequest.getEmployee().getName());
        System.out.println("Start Date: " + leaveRequest.getStartDate());
        System.out.println("End Date: " + leaveRequest.getEndDate());
        System.out.println("Status: " + leaveRequest.getStatus());
        System.out.println("Reason: " + leaveRequest.getReason() + "\n");
    }

    public LocalDate stringToDate (String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            // Parse the string into a LocalDate object
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            System.out.println("Parsed LocalDate: " + localDate); // Output: 2019-09-24
            return localDate;
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing date: " + e.getMessage());
            return null;
        }
    }

    public int remainingLeaveBalance(LeaveRequest leaveRequest){
        long daysBetween = ChronoUnit.DAYS.between( stringToDate(getStartDate()),  stringToDate(getEndDate()));
        int remainingBalance = Math.toIntExact(employee.getLeaveBalance() - daysBetween);
        System.out.println("Remaining leave balance: " + remainingBalance);
        return remainingBalance;
    }

    // In the parent LeaveRequest class
    public boolean processRequest() {
        // Basic request processing logic
        System.out.println("Processing generic leave request...");
        return true;
    }

    public int getDuration(){
        int duration = Math.toIntExact(ChronoUnit.DAYS.between(stringToDate(getStartDate()), stringToDate(getEndDate())));
        return duration;
    }

//    You will complete the following practical tasks:
//
//    Create a hierarchy of leave types (SickLeaveRequest, VacationLeaveRequest, MaternityLeaveRequest)
//    Add specific attributes and methods to each leave type
//    Use the parent class constructor in child classes using super()


//    As part of implementing polymorphism, you'll complete the following practical tasks:
//
//    Override the processRequest() method in each leave type subclass
//    Create an array or list of different leave request types
//    Process all requests using polymorphism



    private ArrayList<StatusChange> statusHistory = new ArrayList<>();
    public ArrayList<StatusChange> getStatusHistory(){
        return statusHistory;
    }
    // Inner class to track status changes
    public class StatusChange {
        private String oldStatus;
        private String newStatus;
        private String changeDate;
        private String changedBy;

        public StatusChange(String oldStatus, String newStatus,
                            String changeDate, String changedBy) {
            this.oldStatus = oldStatus;
            this.newStatus = newStatus;
            this.changeDate = changeDate;
            this.changedBy = changedBy;
        }
    public String getOldStatus(){
            return oldStatus;
    }
    public String getNewStatus(){
        return newStatus;
    }
    public String getChangeDate(){
            return changeDate;
    }
    public String getChangedBy(){
        return changedBy;
    }    // Getters for the fields
        // ...
    }

    // Method to change status and record the change
    public void changeStatus(String newStatus, String changedBy) {
        String oldStatus = this.status;
        this.status = newStatus;

        // Create a new status change record
        StatusChange change = new StatusChange(
                oldStatus, newStatus, getCurrentDate(), changedBy);
        statusHistory.add(change);
    }

    private String getCurrentDate() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return date.format(formatter);
    }


}

//
//In this case study, you will use HashSet to track unique departments with pending leave requests. Check out the following code:
//You'll want to complete the following practical tasks:
//
//Create a HashSet to track unique departments with pending requests
//Add and remove departments from the set based on request status
//Use the set to quickly check if a department has pending requests


//You'll need to complete the following practical tasks:
//
//Create a HashMap to store employees by their ID
//Implement methods to add, retrieve, and remove employees from the map
//Use the map to quickly look up employee information