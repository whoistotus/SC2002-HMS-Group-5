
import java.util.ArrayList;
import java.util.List;

import SC2002_Assignment.User;

public class DoctorModel extends User {
    private String name;
    private String specialization;
    private List<DoctorAvailability> availability;
    private List<Appointment> appointments;

    // Constructor
    public DoctorModel(String hospitalID, String password, String userRole, String name, String specialization) {
        super(hospitalID, password, userRole); // Call User constructor
        this.name = name;
        this.specialization = specialization;
        this.availability = new ArrayList<>();
        this.appointments = new ArrayList<>();
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
