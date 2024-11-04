import java.util.Date;

public class Appointment {
    private String appointmentId;
    private Patient patient;
    private Doctor doctor;
    private Date dateTime;
    private String status;
    private AppointmentOutcomeRecord outcome;

    public Appointment(String appointmentId, Doctor doctor, Patient patient, Date dateTime) {
        this.appointmentId = appointmentId;
        this.doctor = doctor;
        this.patient = patient;
        this.dateTime = dateTime;
        this.status = "pending";
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOutcome(AppointmentOutcomeRecord outcome) {
        this.outcome = outcome;
    }

    @Override
    public String toString() {
        return "Appointment with " + patient.getName() + " on " + dateTime + " - Status: " + status;
    }
}
