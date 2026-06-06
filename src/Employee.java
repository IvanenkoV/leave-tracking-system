import java.time.LocalDate; // Import LocalDate
import java.util.ArrayList;
import java.io.File;

public class Employee {
    // Properties (attributes)
    private int employeeId;
    private String name;
    private String department;
    private String email;

    // Constructor
    public Employee(){
        this.employeeId = 0;
        this.name = "UnknownName";
        this.department = "UnknownDepartment";
        this.email = "UnknownEmail";
    }

    public Employee(int employeeId, String name, String department, String email) {
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.email = email;
    }


    private int leaveBalance = 20; // Annual leave balance in days
    // Getter method
    public int getLeaveBalance() {
        return leaveBalance;
    }
    // Setter method with validation
    public void setLeaveBalance(int leaveBalance) {
        if (leaveBalance >= 0) {
            this.leaveBalance = leaveBalance;
        } else {
            System.out.println("Leave balance cannot be negative.");
        }
    }

    public int getEmployeeId(){
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        if (employeeId >= 0) {
            this.employeeId = employeeId;
        } else {
            System.out.println("employeeId cannot be negative.");
        }
    }


    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment(){
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }


    public String getEmail(){
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void display(){
        System.out.println("Employee ID: " + getEmployeeId());
        System.out.println("Name: " + getName());
        System.out.println("Department: " + getDepartment());
        System.out.println("Email: " + getEmail() + "\n");
    }

    private ArrayList<LeaveRequest> leaveHistory = new ArrayList<>();

    public void addLeaveRequest(LeaveRequest request) {
        leaveHistory.add(request);
    }

    public ArrayList<LeaveRequest> getLeaveHistory() {
        return leaveHistory;
    }
    public LeaveRequest getLeaveRequestById(int requestId) {
        for (LeaveRequest request : leaveHistory) {
            if (request.getRequestId() == requestId) {
                return request;
            }
        }
        return null; // Request not found
    }

    public void displayLeaveHistory(){
        for (LeaveRequest request : leaveHistory) {
            request.display(); // Corrected: no argument needed
            }
        }


    public static void main(String[] args) {
        System.out.println("=== LEAVE TRACKING SYSTEM - FILE MANAGEMENT TEST ===\n");

        LeaveTrackingSystem trackingSystem = new LeaveTrackingSystem();

        // 1. Test Directory Initialization
        System.out.println("1. Initializing File Structure...");
        trackingSystem.initializeFileStructure();

        // 2. Create Employees
        System.out.println("\n2. Creating and Saving Employee Data...");
        Employee emp1 = new Employee(101, "Alice Johnson", "Engineering", "alice@example.com");
        Employee emp2 = new Employee(102, "Bob Smith", "Marketing", "bob@example.com");
        
        trackingSystem.addEmployee(emp1);
        trackingSystem.addEmployee(emp2);
        
        // Save to CSV
        trackingSystem.saveEmployeeData();

        // 3. Test Loading Data
        System.out.println("\n3. Testing Data Loading (Clearing and reloading)...");
        // We create a new system to prove it loads from file
        LeaveTrackingSystem newSystem = new LeaveTrackingSystem();
        newSystem.loadEmployeeData();
        Employee loadedEmp = newSystem.getEmployeeById(101);
        if (loadedEmp != null) {
            System.out.println("Successfully loaded: " + loadedEmp.getName());
        }

        // 4. Test Saving Leave Requests to Files (CRUD - Create)
        System.out.println("\n4. Saving Individual Leave Requests to Files...");
        // Using LocalDate.of() for dates
        LeaveRequest req1 = new SickLeaveRequest(1, emp1, LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 3), true);
        LeaveRequest req2 = new VacationLeaveRequest(2, emp2, LocalDate.of(2026, 7, 10), LocalDate.of(2026, 7, 20), true);
        
        trackingSystem.saveLeaveRequest(req1);
        trackingSystem.saveLeaveRequest(req2);

        // 5. Test Directory Listing
        System.out.println("\n5. Listing Request Files for Employees...");
        trackingSystem.listRequestsForEmployee(101);

        // 6. Test Backup (Byte Streams)
        System.out.println("\n6. Creating Data Backup...");
        trackingSystem.backupData();

        // 7. Test Image Handling (Simulation)
        // Note: For this to work without error, you would need a real 'profile.jpg' file
        /*
        System.out.println("\n7. Testing Image Save (Simulated)...");
        File dummyImage = new File("profile.jpg"); 
        if(dummyImage.exists()) {
            trackingSystem.saveEmployeeImage(emp1, dummyImage);
        } else {
            System.out.println("Skip image test: 'profile.jpg' not found.");
        }
        */

        System.out.println("\n=== ALL TESTS COMPLETED ===");
    }
}