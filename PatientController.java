package assignment;

import java.time.LocalDateTime;
import java.util.List;

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
        		model.getPatientId(),
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
        boolean success = appointmentManager.scheduleAppointment(model, doctor, dateTime);
        if (success) 
        {
            view.showMessage("Appointment scheduled successfully with status 'Pending'.");
        } 
        
        else 
        {
            view.showMessage("Failed to schedule appointment. The doctor may not be available at this time.");
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
        List<Appointment> appointments = appointmentManager.getAppointmentsForPatient(model.getPatientId());
        view.viewScheduledAppointmentStatus(appointments);
    }
    
    
    public void cancelAppointment(Appointment appointment) 
    {
        boolean success = appointmentManager.cancelAppointment(appointment);
        if (success) 
        {
            view.showMessage("Appointment has been successfully canceled.");
        } 
        
        else 
        {
            view.showMessage("Failed to cancel the appointment. Appointment not found.");
        }
    }
}
