package SC2002_Assignment;

import java.time.LocalDateTime;

public class Appointment {
    private String appointmentID;
    private PatientModel patient;
    private DoctorModel doctor;
    private String appointmentDate;
    private String appointmentTime;
    private AppointmentStatus status;

    public enum AppointmentStatus {
        PENDING,
        ACCEPTED,
        DECLINED,
        CANCELLED,
        COMPLETED
    }
    
    // Constructor
    public Appointment(String appointmentID, PatientModel patient, DoctorModel doctor, String AppointmentDate, String appointmentTime, AppointmentStatus status) {
        this.appointmentID = appointmentID;
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }

    public String toCsv() {
        return String.join(",",
                appointmentID,
                patient.getHospitalID(),
                doctor.getDoctorID(),
                appointmentDate,
                appointmentTime,
                status.toString()
        );
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

    public String getAppointmentTime() 
    { 
    	return appointmentTime; 
    }

    public String getAppointmentDate() 
    { 
    	return appointmentDate; 
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

    public void setAppointmentDate(String newDate) 
    { 
    	this.appointmentDate = newDate; 
    }

    public void setAppointmentTime(String newTime) 
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
