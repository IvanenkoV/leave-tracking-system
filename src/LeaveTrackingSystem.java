import java.util.*;

public class LeaveTrackingSystem {
    // Maps for quick lookup
    private HashMap<Integer, Employee> employeeById = new HashMap<>();
    private HashMap<Integer, LeaveRequest> requestById = new HashMap<>();

    // Lists for ordered access
    private ArrayList<LeaveRequest> allRequests = new ArrayList<>();

    // Sets for unique collections
    private HashSet<String> leaveTypes = new HashSet<>();

    // Queues for processing
    private Queue<LeaveRequest> pendingApprovals = new LinkedList<>();

    private HashSet<String> departmentsWithPendingRequests = new HashSet<>();

    public void updateDepartmentsWithPendingRequests() {
        departmentsWithPendingRequests.clear();

        for (LeaveRequest request : allRequests) {
            if (request.getStatus().equals("Pending")) {
                departmentsWithPendingRequests.add(
                        request.getEmployee().getDepartment());
            }
        }
    }

    public boolean hasPendingRequests(String department) {
        return departmentsWithPendingRequests.contains(department);
    }

    private HashMap<Integer, Employee> employeeDirectory = new HashMap<>();

    public void addEmployee(Employee employee) {
        employeeDirectory.put(employee.getEmployeeId(), employee);
    }

    public Employee getEmployeeById(int employeeId) {
        return employeeDirectory.get(employeeId);
    }

    public boolean removeEmployee(int employeeId) {
        if (employeeDirectory.containsKey(employeeId)) {
            employeeDirectory.remove(employeeId);
            return true;
        }
        return false;
    }

    // Method to add a request and update all collections
    public void addLeaveRequest(LeaveRequest request) {
        // 1. Add to the main list
        allRequests.add(request);

        // 2. Add to the lookup map
        requestById.put(request.getRequestId(), request);

        // 3. If status is Pending, add to the processing queue
        if (request.getStatus().equals("Pending")) {
            pendingApprovals.add(request);
        }

        // 4. Track unique leave reasons/types
        leaveTypes.add(request.getReason());

        // 5. Update departments with pending requests
        updateDepartmentsWithPendingRequests();
    }

    // Method to process the next request in the queue
    public void processNextRequest() {
        if (!pendingApprovals.isEmpty()) {
            LeaveRequest request = pendingApprovals.poll(); // Retrieves and removes the head
            System.out.println("Processing request ID: " + request.getRequestId());
            request.processRequest(); // Execute polymorphic logic

            // Update departments logic again as status might have changed (conceptually)
            updateDepartmentsWithPendingRequests();
        } else {
            System.out.println("No pending requests to process.");
        }
    }
}