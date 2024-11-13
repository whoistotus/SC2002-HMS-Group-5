package SC2002_Assignment;

import java.time.LocalDateTime;

public class Appointment {
    private String appointmentID;
    private PatientModel patient;
    private DoctorModel doctor;
    private LocalDateTime appointmentTime;
    private AppointmentStatus status;

    public enum AppointmentStatus {
        PENDING,
        ACCEPTED,
        DECLINED,
        CANCELLED
    }
    
    // Constructor
    public Appointment(String appointmentID, PatientModel patient, DoctorModel doctor, LocalDateTime appointmentTime, AppointmentStatus status) {
        this.appointmentID = appointmentID;
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }

    // Getters and Setters
    public String getPatientID() {
        return patient.getHospitalID();
    }

    public PatientModel getPatient()
    {
        return patient;
    }

    public String getDoctorID() {
        return doctor.getDoctorID();
    }
    
    public DoctorModel getDoctor()
    {
        return doctor;
    }

    public LocalDateTime getAppointmentTime() 
    { 
    	return appointmentTime; 
    }

    public String getAppointmentID()
    {
    	return appointmentID;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public void setAppointmentTime(LocalDateTime newTime) 
    { 
    	this.appointmentTime = newTime; 
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentID='" + appointmentID + '\'' +
                ", patientID='" + patient.getHospitalID() + '\'' +
                ", doctorID='" + doctor.getDoctorID() + '\'' +
                ", appointmentTime=" + appointmentTime + '\'' +
                ", status=" + status +
                '}';
    }
}
