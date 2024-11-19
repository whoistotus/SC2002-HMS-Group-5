package model;
import java.util.Arrays;

public class DoctorAvailability {
    private DoctorModel doctor;
    private String date;
    private String startTime;
    private String endTime;
    private boolean[] timeSlots; // Array to represent each hourly slot as available/unavailable

    public DoctorAvailability(DoctorModel doctor, String date, String startTime, String endTime) {
        this.doctor = doctor;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        initializeTimeSlots();
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    // Initialize each hour in the available range as true (available)
    private void initializeTimeSlots() {
        timeSlots = new boolean[24]; // Representing hours from 0 to 23
        int start = Integer.parseInt(startTime.split(":")[0]);
        int end = Integer.parseInt(endTime.split(":")[0]);
        Arrays.fill(timeSlots, start, end, true);
    }

    // Check if a specific hour slot is available
    public boolean isTimeSlotAvailable(String time) {
        int hour = Integer.parseInt(time.split(":")[0]);
        return timeSlots[hour];
    }

    // Block a specific hour slot after booking
    public void blockTimeSlot(String time) {
        int hour = Integer.parseInt(time.split(":")[0]);
        timeSlots[hour] = false;
    }

    

    // Getters
    public String getDoctorID() { return doctor.getHospitalID(); }
    public String getDoctorName(){return doctor.getName();}
    public DoctorModel getDoctor() {return doctor;}
    public String getDate() { return date; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }

    @Override
    public String toString() {
        return "DoctorAvailability{" +
            "doctorID='" + doctor.getHospitalID() + '\'' +
            ", date='" + date + '\'' +
            ", startTime='" + startTime + '\'' +
            ", endTime='" + endTime + '\'' +
            '}';
    }

}
