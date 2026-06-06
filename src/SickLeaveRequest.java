import java.time.LocalDate;

// Child class
public class SickLeaveRequest extends LeaveRequest {
    private boolean medicalCertificateProvided;

    public SickLeaveRequest(int requestId, Employee employee,
                            LocalDate startDate, LocalDate endDate, // Changed from String to LocalDate
                            boolean medicalCertificateProvided) {
        super(requestId, employee, startDate, endDate, "Sick Leave"); // Pass LocalDate to super
        this.medicalCertificateProvided = medicalCertificateProvided;
    }

    // Additional methods specific to sick leave
    public boolean isMedicalCertificateProvided() {
        return medicalCertificateProvided;
    }

    // In the SickLeaveRequest class
    @Override
    public boolean processRequest() {
        if (!medicalCertificateProvided && getDuration() > 2) {
            System.out.println("Sick leave longer than 2 days requires a medical certificate");
            return false;
        }
        System.out.println("Processing sick leave request...");
        return true;
    }

    @Override
    public int calculateLeaveDays() {
        return getDuration();
    }
}