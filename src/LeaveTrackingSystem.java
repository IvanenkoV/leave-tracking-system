import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LeaveTrackingSystem {

    private File employeeDataFile = new File("employees.csv");
    private File leaveRequestsFile = new File("leave_requests.csv");


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
        employeeById.put(employee.getEmployeeId(), employee);
    }

    public Employee getEmployeeById(int employeeId) {
        return employeeDirectory.get(employeeId);
    }

    public boolean removeEmployee(int employeeId) {
        if (employeeDirectory.containsKey(employeeId)) {
            employeeDirectory.remove(employeeId);
            employeeById.remove(employeeId);
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

    public void saveEmployeeImage(Employee employee, File imageFile) {
        // Ensure the directory exists before saving
        File directory = new File("images");
        if (!directory.exists()) {
            directory.mkdir();
        }

        try (FileInputStream in = new FileInputStream(imageFile);
             FileOutputStream out = new FileOutputStream(
                     "images/" + employee.getEmployeeId() + ".jpg")) {

            byte[] buffer = new byte[1024];
            int length;

            // Copy the file content byte by byte
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            System.out.println("Employee image saved successfully: images/" + employee.getEmployeeId() + ".jpg");
        } catch (IOException e) {
            System.out.println("Error saving employee image: " + e.getMessage());
        }
    }

    public byte[] loadEmployeeImage(Employee employee) {
        File file = new File("images/" + employee.getEmployeeId() + ".jpg");
        if (!file.exists()) return null;
        
        byte[] imageData = new byte[(int) file.length()];

        try (FileInputStream in = new FileInputStream(file)) {
            in.read(imageData); 
            return imageData;
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public void saveEmployeeData() {
        try (FileWriter writer = new FileWriter(employeeDataFile)) {
            writer.write("ID,Name,Department,Email,LeaveBalance\n");

            for (Employee employee : employeeDirectory.values()) {
                writer.write(
                        employee.getEmployeeId() + "," +
                                employee.getName() + "," +
                                employee.getDepartment() + "," +
                                employee.getEmail() + "," +
                                employee.getLeaveBalance() + "\n"
                );
            }

            System.out.println("Employee data saved successfully");
        } catch (IOException e) {
            System.out.println("Error saving employee data: " + e.getMessage());
        }
    }

    public void loadEmployeeData() {
        if (!employeeDataFile.exists()) {
            System.out.println("No employee data file found");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(employeeDataFile))) {
            String line = reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String department = parts[2];
                    String email = parts[3];
                    int leaveBalance = Integer.parseInt(parts[4]);

                    Employee employee = new Employee(id, name, department, email);
                    employee.setLeaveBalance(leaveBalance);

                    employeeDirectory.put(id, employee);
                    employeeById.put(id, employee);
                }
            }

            System.out.println("Employee data loaded successfully");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading employee data: " + e.getMessage());
        }
    }

    public void initializeFileStructure() {
        File dataDir = new File("leavetracker_data");
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        File imagesDir = new File("images");
        if (!imagesDir.exists()) {
            imagesDir.mkdir();
            System.out.println("Created images directory");
        }

        File[] subdirs = {
                new File(dataDir, "employees"),
                new File(dataDir, "requests"),
                new File(dataDir, "reports"),
                new File(dataDir, "backups")
        };

        for (File dir : subdirs) {
            if (!dir.exists()) {
                if (dir.mkdir()) {
                    System.out.println("Created directory: " + dir.getName());
                }
            }
        }
    }

    public void saveLeaveRequest(LeaveRequest request) {
        File requestFile = new File(
                "leavetracker_data/requests/" +
                        request.getRequestId() + ".txt");

        try (PrintWriter writer = new PrintWriter(new FileWriter(requestFile))) {
            writer.println("RequestID: " + request.getRequestId());
            writer.println("EmployeeID: " + request.getEmployee().getEmployeeId());
            writer.println("StartDate: " + request.getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE)); // Use formatted LocalDate
            writer.println("EndDate: " + request.getEndDate().format(DateTimeFormatter.ISO_LOCAL_DATE));     // Use formatted LocalDate
            writer.println("Status: " + request.getStatus());
            writer.println("Reason: " + request.getReason());
            writer.println("CreationTimestamp: " + request.getCreationTimestampFormatted()); // Add creation timestamp

            if (request instanceof SickLeaveRequest) {
                SickLeaveRequest slr = (SickLeaveRequest) request;
                writer.println("Type: Sick");
                writer.println("MedicalCertificate: " +
                        slr.isMedicalCertificateProvided());
            } else if (request instanceof VacationLeaveRequest) { // Added for completeness
                VacationLeaveRequest vlr = (VacationLeaveRequest) request;
                writer.println("Type: Vacation");
                writer.println("ManagerApproval: " + vlr.isManagerApproval());
            } else if (request instanceof MaternityLeaveRequest) { // Added for completeness
                MaternityLeaveRequest mlr = (MaternityLeaveRequest) request;
                writer.println("Type: Maternity");
                writer.println("MedicalCertificate: " + mlr.isMedicalCertificateProvided());
            } else {
                writer.println("Type: Regular");
            }

            // Add Status History
            writer.println("\n--- Status History ---");
            if (request.getStatusHistory().isEmpty()) {
                writer.println("No status changes recorded.");
            } else {
                for (LeaveRequest.StatusChange change : request.getStatusHistory()) {
                    writer.println("  - " + change.getChangeTimestampFormatted() +
                                   ": " + change.getOldStatus() + " -> " + change.getNewStatus() +
                                   " by " + change.getChangedBy());
                }
            }
            writer.println("--------------------");


            System.out.println("Leave request saved: " + request.getRequestId());
        } catch (IOException e) {
            System.out.println("Error saving leave request: " + e.getMessage());
        }
    }

    public void backupData() {
        File source = employeeDataFile;
        File dest = new File("leavetracker_data/backups/employees_backup.csv");
        
        if (!source.exists()) return;

        try (FileInputStream in = new FileInputStream(source);
             FileOutputStream out = new FileOutputStream(dest)) {
            
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            System.out.println("Backup created successfully in backups folder.");
        } catch (IOException e) {
            System.out.println("Error creating backup: " + e.getMessage());
        }
    }

    public void listRequestsForEmployee(int employeeId) {
        File requestsDir = new File("leavetracker_data/requests");
        if (!requestsDir.exists() || !requestsDir.isDirectory()) return;

        System.out.println("Files for Employee ID " + employeeId + ":");
        File[] files = requestsDir.listFiles();
        if (files != null) {
            for (File file : files) {
                // In a real scenario, you'd parse the file to check the EmployeeID
                // For now, we just list all files in the requests directory
                System.out.println("- " + file.getName());
            }
        }
    }

    public void deleteLeaveRequestFile(int requestId) {
        File requestFile = new File("leavetracker_data/requests/" + requestId + ".txt");
        if (requestFile.exists()) {
            if (requestFile.delete()) {
                System.out.println("Request file " + requestId + " deleted.");
            }
        }
    }
}