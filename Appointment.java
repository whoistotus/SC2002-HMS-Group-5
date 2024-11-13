package SC2002_Assignment;
import java.util.Date;

public class Appointment {
    private String appointmentID;
    private String patientID;
    private String doctorID;
    private String date;
    private String time;
    private AppointmentStatus status;

    public enum AppointmentStatus {
        PENDING,
        ACCEPTED,
        DECLINED,
        CANCELLED
    }
    
    // Constructor
    public Appointment(String appointmentID, String patientID, String doctorID, String date, String time, AppointmentStatus status) {
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    // Getters and Setters
    public String getAppointmentID() {
        return appointmentID;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentID='" + appointmentID + '\'' +
                ", patientID='" + patientID + '\'' +
                ", doctorID='" + doctorID + '\'' +
                ", date=" + date +
                ", time='" + time + '\'' +
                ", status=" + status +
                '}';
    }
}
