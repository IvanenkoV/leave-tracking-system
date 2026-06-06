import java.time.LocalDate;

public class MaternityLeaveRequest extends LeaveRequest{
    private boolean medicalCertificateProvided;

    public MaternityLeaveRequest(int requestId, Employee employee,
                                 LocalDate startDate, LocalDate endDate, // Changed from String to LocalDate
                                 boolean medicalCertificateProvided) {
        super(requestId, employee, startDate, endDate, "Maternity Leave"); // Pass LocalDate to super
        this.medicalCertificateProvided = medicalCertificateProvided;
    }

    public boolean isMedicalCertificateProvided() {
        return medicalCertificateProvided;
    }

    @Override
    public boolean processRequest() {
        if (!medicalCertificateProvided) {
            System.out.println("Maternity leave requires a medical certificate");
            return false;
        }
        System.out.println("Processing maternity leave request...");
        return true;
    }
    @Override
    public int calculateLeaveDays() {
        return getDuration();
    }

}