import java.util.ArrayList;
import java.util.List;

public class DoctorModel {
    private String doctorID;
    private String name;
    private String specialization;
    private List<DoctorAvailability> availability;
    private List<Appointment> appointments;

    // Constructor
    public DoctorModel(String doctorID, String name, String specialization) {
        this.doctorID = doctorID;
        this.name = name;
        this.specialization = specialization;
        this.availability = new ArrayList<>();
        this.appointments = new ArrayList<>();
    }

    // Getters and Setters
    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public List<DoctorAvailability> getAvailability() {
        return availability;
    }

    public void setAvailability(List<DoctorAvailability> availability) {
        this.availability = availability;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}
