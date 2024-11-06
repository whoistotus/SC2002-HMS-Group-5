import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Doctor extends User {
    private List<Appointment> appointments = new ArrayList<>();
    private List<Date> availability = new ArrayList<>();

    public Doctor(String userId, String name) {
        super(userId, name, "Doctor");
    }

    public void viewPersonalSchedule() {
        List<Appointment> confirmedAppointments = appointments.stream()
                .filter(app -> "confirmed".equals(app.getStatus()))
                .collect(Collectors.toList());

        System.out.println("Personal Schedule for " + getName() + ":");
        for (Appointment appointment : confirmedAppointments) {
            System.out.println(appointment);
        }
    }

    public void setAvailability(Date date) {
        availability.add(date);
        System.out.println("Availability set for: " + date);
    }

    public void acceptAppointment(Appointment appointment) {
        appointment.setStatus("confirmed");
        System.out.println("Accepted appointment with patient: " + appointment.getPatient().getName());
    }

    public void declineAppointment(Appointment appointment) {
        appointment.setStatus("declined");
        System.out.println("Declined appointment with patient: " + appointment.getPatient().getName());
    }

    public void recordAppointmentOutcome(Appointment appointment, String serviceType, List<Medication> medicines, String notes) {
        AppointmentOutcomeRecord outcomeRecord = new AppointmentOutcomeRecord(serviceType, medicines, notes);
        appointment.setOutcome(outcomeRecord);

        MedicalRecord record = new MedicalRecord(serviceType, medicines, notes);
        appointment.getPatient().addMedicalRecord(record);

        System.out.println("Recorded outcome for appointment with patient: " + appointment.getPatient().getName());
    }

    // New method to add appointments
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }
}
