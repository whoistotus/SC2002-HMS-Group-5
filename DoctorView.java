import java.util.List;

public class DoctorView {

    public void viewPersonalSchedule() {
        System.out.println("Displaying personal schedule...");
    }

    public void viewUpcomingAppointments(List<Appointment> appointments) {
        System.out.println("Upcoming Appointments:");
        for (Appointment appointment : appointments) {
            System.out.println(appointment);
        }
    }

    public void viewPatientMedicalRecords(List<MedicalRecord> records) {
        System.out.println("Patient Medical Records:");
        for (MedicalRecord record : records) {
            System.out.println(record);
        }
    }
}
