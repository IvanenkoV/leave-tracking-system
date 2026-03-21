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
    // Methods will be added here

//    Next let's examine the practical related tasks you will need to complete:
//
//    Create an Employee class with appropriate attributes
//    Create a LeaveRequest class with start date, end date, and status attributes
//    Create instances (objects) of these classes to represent different employees and their leave requests


    // In the Employee class
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

    public void display(Employee employee){
        System.out.println("Employee ID: " + employee.getEmployeeId());
        System.out.println("Name: " + employee.getName());
        System.out.println("Department: " + employee.getDepartment());
        System.out.println("Email: " + employee.getEmail() + "\n");
    }
//    So as part of encapsulation, you'll perform the following practical tasks:
//
//    Add getters and setters for all attributes in Employee and LeaveRequest classes
//    Add validation in setters (to enable rejection of negative leave balances)
//    Create a method to calculate remaining leave balance after a request
    public static void main(String[] args) {
        Employee employee1 = new Employee();
        employee1.setEmployeeId(1);
        employee1.setName("John Doe");
        employee1.setDepartment("HR");
        employee1.setEmail("some1email@example.com");

        Employee employeeDefault = new Employee();

        Employee employee3 = new Employee(3,"Jane Smith", "IT", "some3email@example.com");

        employee1.display(employee1);
        employeeDefault.display(employeeDefault);
        employee3.display(employee3);

//        leaveRequest for the first employee
        LeaveRequest leaveRequest1 = new VacationLeaveRequest();
        leaveRequest1.setRequestId(1);
        leaveRequest1.setEmployee(employee1);
        leaveRequest1.setStartDate("14-04-2026");
        leaveRequest1.setEndDate("17-04-2026");
        leaveRequest1.setReason("Annual Vacation");

        leaveRequest1.display(leaveRequest1);

//        leaveRequest for the first employee

        LeaveRequest leaveRequest3 = new SickLeaveRequest(3, employee3, "19-05-2026","24-05-2026",true);

        leaveRequest3.display(leaveRequest3);
        leaveRequest3.remainingLeaveBalance(leaveRequest3);


        System.out.println("\n The new part related to polymorphism starts here: \n");
        LeaveRequest[] leaveRequestsArray = {new SickLeaveRequest(1,employee1,"14-04-2026","17-04-2026",true),
                new VacationLeaveRequest(2,employeeDefault,"19-05-2026","24-05-2026",false),
                new MaternityLeaveRequest(3,employee3,"19-05-2026","24-05-2026",true)};

        for(LeaveRequest request: leaveRequestsArray){
            System.out.println("Request ID: " + request.getRequestId());
            System.out.println("Employee: " + request.getEmployee().getName());
            System.out.println("Start Date: " + request.getStartDate());
            request.processRequest();
            request.display(request);
        }

//      Here was done a status change in request
        leaveRequestsArray[1].changeStatus("Approved", "John Doe");
        //    Create an array or list of different leave request types
        leaveRequestsArray[1].display(leaveRequestsArray[1]);

        for (LeaveRequest.StatusChange change : leaveRequestsArray[1].getStatusHistory()) {
            System.out.println("Old Status: " + change.getOldStatus() +
                    ", New Status: " + change.getNewStatus() +
                    ", Date: " + change.getChangeDate() +
                    ", Changed By: " + change.getChangedBy());
        }
//    Process all requests using polymorphism
    }

}

//When using collections, you will need to perform the following practical tasks:
//
//Identify which data in the system to store in collections
//Decide which collection types are appropriate for different data sets
//Import the necessary collection classes
//
//You'll want to complete the following practical tasks related to lists:
//
//Create an ArrayList to store leave requests for each employee
//Implement methods to add, retrieve, and search for leave requests
//Display all leave requests for a specific employee