package SC2002_Assignment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
//nihao
public class PatientController 
{
    private PatientModel model;
    private PatientView view;
    private AppointmentManager appointmentManager;

    public PatientController(PatientModel model, PatientView view, AppointmentManager appointmentManager) 
    {
        this.model = model;
        this.view = view;
        this.appointmentManager = appointmentManager;
    }

    public void viewMedicalRecord() 
    {
        view.viewMedicalRecord(
        		model.getHospitalID(),
                model.getName(),
                model.getDob(),
                model.getGender(),
                model.getContactNumber(),
                model.getEmail(),
                model.getBloodType(),
                model.getMedicalRecord()
        );
    }

    public void updateContactNumber(String newContactNumber) 
    {
        model.setContactNumber(newContactNumber);
        view.showMessage("Contact number updated successfully.");
    }

    public void updateEmail(String newEmail) 
    {
        model.setEmail(newEmail);
        view.showMessage("Email updated successfully.");
    }


    /*public void viewMedicalRecord() {
        List<String> medicalRecord = model.getMedicalRecord();
        view.viewMedicalRecord(medicalRecord);
    }*/
    
    public void viewAvailableSlots(Doctor doctor) 
    {
        List<LocalDateTime> availableSlots = appointmentManager.getAvailableSlots(doctor);
        view.viewAvailableSlots(doctor, availableSlots);
    }

    public void scheduleAppointment(Doctor doctor, LocalDateTime dateTime) 
    {
    	String appointmentID = UUID.randomUUID().toString();
        boolean success = appointmentManager.scheduleAppointment(appointmentID, model, doctor, dateTime);
        if (success) 
        {
            view.showMessage("Appointment scheduled successfully with status 'Pending'.");
        } 
        
        else 
        {
            view.showMessage("Failed to schedule appointment. The doctor may not be available at this time.");
        }
    }
    
    
    public void rescheduleAppointment(Appointment appointment, LocalDateTime newDateTime) 
    {
        boolean success = appointmentManager.rescheduleAppointment(appointment, newDateTime);
        if (success) 
        {
            view.showMessage("Appointment rescheduled successfully with status 'Pending'.");
        } 
        
        else 
        {
            view.showMessage("Failed to reschedule appointment. The selected time slot is unavailable.");
        }
    }

    /*public void viewAppointments() 
    {
        List<Appointment> appointments = appointmentManager.getAppointmentsForPatient(model.getPatientId());
        
        for (Appointment appointment : appointments) 
        {
            System.out.println(appointment);
        }
    }*/
    
    public void viewScheduledAppointmentsStatus() 
    {
        List<Appointment> appointments = appointmentManager.getAppointmentsForPatient(model.getHospitalID());
        view.viewScheduledAppointmentStatus(appointments);
    }
    
    
    public void cancelAppointment(Appointment appointment) 
    {
        boolean success = appointmentManager.cancelAppointment(appointment.getAppointmentID());
        if (success) 
        {
            view.showMessage("Appointment has been successfully canceled.");
        } 
        
        else 
        {
            view.showMessage("Failed to cancel the appointment. Appointment not found.");
        }
    }
    
    public Appointment getAppointmentByPatientId() 
    {
        return appointmentManager.getAppointmentByPatientId(model.getHospitalID());
    }

}
