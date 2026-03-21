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

    // Methods to manage these collections
    // ...
}
//You will need to complete the following practical tasks:
//
//Determine which collection types work best for different aspects of the system
//Combine multiple collections to create a complete system
//Implement methods to maintain consistency across collections
//In real-world professional software development, collections are essential for efficient data management. Human resource systems use collections to organize employee records, track leave balances, and manage approval workflows.
//
//You'll want to complete the following practical, real-life tasks:
//
//Choose appropriate collection types based on data access patterns
//Use lists when order matters and duplicates are allowed
//Use sets when you need to track unique items
//Use maps when you need fast lookups by a key
//Use queues when processing items in a specific order
