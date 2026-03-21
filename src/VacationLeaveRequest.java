public class VacationLeaveRequest extends LeaveRequest {
    private boolean managerApproval;

    public VacationLeaveRequest(int requestId, Employee employee,
                                String startDate, String endDate,
                                boolean managerApproval) {
        super(requestId, employee, startDate, endDate, "Vacation Leave");
        this.managerApproval = managerApproval;
    }
    public VacationLeaveRequest() {
        super();
    }

    public boolean isManagerApproval() {
        return managerApproval;
    }

    @Override
    public boolean processRequest() {
        if (!managerApproval) {
            System.out.println("Vacation leave requires manager approval");
            return false;
        }
        if (getDuration() > getEmployee().getLeaveBalance()) {
            System.out.println("Insufficient leave balance for vacation");
            return false;
        }
        System.out.println("Processing vacation leave request...");
        return true;
    }
    @Override
    public int calculateLeaveDays() {
        return getDuration();
    }

}
