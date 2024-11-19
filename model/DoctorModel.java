package model;
import java.util.ArrayList;
import java.util.List;


public class DoctorModel extends User {
    private String name;
    private String specialization;
    private List<DoctorAvailability> availabilityList;
    private List<Appointment> appointments;

    // Constructor
    public DoctorModel(String hospitalID, String password, String userRole, String name, String specialization) {
        super(hospitalID, password, userRole); // Call User constructor
        this.name = name;
        this.specialization = specialization;
        this.availabilityList = new ArrayList<>();
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
        return availabilityList;
    }

    public void addAvailability(DoctorAvailability availability) {
        availabilityList.add(availability);
        // Optionally merge overlapping or adjacent slots here
        availabilityList = mergeAvailabilitySlots(availabilityList);
    }

    private List<DoctorAvailability> mergeAvailabilitySlots(List<DoctorAvailability> availabilityList) {
        // Implement merging logic here to handle overlapping time slots
        // You can use the merge logic we discussed earlier
        // Return the merged list
        return availabilityList;
    }

    public List<DoctorAvailability> getAvailabilityList() {
        return availabilityList;
    }

    public void setAvailability(List<DoctorAvailability> availability) {
        this.availabilityList = availability;
    }

    public void addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }


}
