import java.util.ArrayList;

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
            this.employeeId = leaveBalance;
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
            request.display();
            }
        }


    public static void main(String[] args) {
        // 1. Create 3 new Employees
        System.out.println("--- Creating Employees ---");
        Employee emp1 = new Employee(101, "Alice Johnson", "Engineering", "alice@example.com");
        Employee emp2 = new Employee(102, "Bob Smith", "Marketing", "bob@example.com");
        Employee emp3 = new Employee(103, "Charlie Brown", "HR", "charlie@example.com");

        emp1.display();
        emp2.display();
        emp3.display();

        // 2. Initialize the LeaveTrackingSystem
        System.out.println("--- Initializing Leave Tracking System ---");
        LeaveTrackingSystem trackingSystem = new LeaveTrackingSystem();
        
        // Add employees to the system (HashMap usage)
        trackingSystem.addEmployee(emp1);
        trackingSystem.addEmployee(emp2);
        trackingSystem.addEmployee(emp3);
        System.out.println("Employees added to the system directory.");

        // 3. Generate 5 different leave requests for them
        System.out.println("\n--- Generating Leave Requests ---");
        
        // Request 1: Sick Leave for Alice (Pending)
        LeaveRequest req1 = new SickLeaveRequest(1, emp1, "01-06-2026", "03-06-2026", true);
        
        // Request 2: Vacation for Bob (Pending)
        LeaveRequest req2 = new VacationLeaveRequest(2, emp2, "10-07-2026", "20-07-2026", true);
        
        // Request 3: Maternity Leave for Charlie (Pending)
        LeaveRequest req3 = new MaternityLeaveRequest(3, emp3, "01-08-2026", "01-11-2026", true);
        
        // Request 4: Another Sick Leave for Alice (Pending - but no certificate)
        LeaveRequest req4 = new SickLeaveRequest(4, emp1, "15-06-2026", "16-06-2026", false);
        
        // Request 5: Vacation for Bob (Approved - manually set status to show history later)
        LeaveRequest req5 = new VacationLeaveRequest(5, emp2, "25-12-2026", "01-01-2027", true);
        // We'll add this one too, it will be pending initially
        
        // Add requests to employee history (ArrayList usage)
        emp1.addLeaveRequest(req1);
        emp2.addLeaveRequest(req2);
        emp3.addLeaveRequest(req3);
        emp1.addLeaveRequest(req4);
        emp2.addLeaveRequest(req5);

        // Add requests to the main tracking system (Queue, HashSet, HashMap usage)
        trackingSystem.addLeaveRequest(req1);
        trackingSystem.addLeaveRequest(req2);
        trackingSystem.addLeaveRequest(req3);
        trackingSystem.addLeaveRequest(req4);
        trackingSystem.addLeaveRequest(req5);
        
        System.out.println("All requests added to the system.");

        // 4. Test HashSet: Check departments with pending requests
        System.out.println("\n--- Testing HashSet (Departments with Pending Requests) ---");
        System.out.println("Does Engineering have pending requests? " + trackingSystem.hasPendingRequests("Engineering"));
        System.out.println("Does Marketing have pending requests? " + trackingSystem.hasPendingRequests("Marketing"));
        System.out.println("Does Sales have pending requests? " + trackingSystem.hasPendingRequests("Sales")); // Should be false

        // 5. Test HashMap: Retrieve employee by ID
        System.out.println("\n--- Testing HashMap (Employee Lookup) ---");
        Employee retrievedEmp = trackingSystem.getEmployeeById(102);
        if (retrievedEmp != null) {
            System.out.println("Retrieved Employee 102: " + retrievedEmp.getName());
        } else {
            System.out.println("Employee not found.");
        }

        // 6. Test Queue: Process requests
        System.out.println("\n--- Testing Queue (Processing Pending Requests) ---");
        
        // Process 1st request (Sick Leave for Alice)
        System.out.println("Processing next request...");
        trackingSystem.processNextRequest(); 
        
        // Process 2nd request (Vacation for Bob)
        System.out.println("Processing next request...");
        trackingSystem.processNextRequest();
        
        // Change status manually to test history
        req5.changeStatus("Approved", "System Admin");
        
        System.out.println("\n--- Testing Status History for Request #5 ---");
        for (LeaveRequest.StatusChange change : req5.getStatusHistory()) {
             System.out.println("Old Status: " + change.getOldStatus() +
                    ", New Status: " + change.getNewStatus() +
                    ", Date: " + change.getChangeDate() +
                    ", Changed By: " + change.getChangedBy());
        }
        
        System.out.println("\n--- Final Employee Leave History Check ---");
        System.out.println("Leave History for Alice:");
        emp1.displayLeaveHistory();
    }
}