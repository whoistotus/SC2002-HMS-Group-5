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


    public void viewMedicalRecord(String patientID) {
        MedicalRecord record = MedicalRecordsCsvHelper.getMedicalRecordById(patientID);
        view.viewMedicalRecord(record);
    }

    public void updateContactNumber(String newContactNumber) 
    {
        model.setContactNumber(newContactNumber);
        PatientListCsvHelper.updatePatientContactInfo(model.getHospitalID(), newContactNumber, model.getEmail());
        MedicalRecordsCsvHelper.updatePatientContactInfo(model.getHospitalID(), newContactNumber, model.getEmail());
        view.showMessage("Contact number updated successfully.");
    }

    public void updateEmail(String newEmail) {
        model.setEmail(newEmail);
        PatientListCsvHelper.updatePatientContactInfo(model.getHospitalID(), model.getContactNumber(), newEmail);
        MedicalRecordsCsvHelper.updatePatientContactInfo(model.getHospitalID(), model.getContactNumber(), newEmail);
        view.showMessage("Email updated successfully.");
    }

    public void viewAvailableSlots() {
        List<DoctorAvailability> slots = DoctorAvailabilityCsvHelper.loadDoctorAvailability();
        view.viewAvailableSlots(slots);
    }
    
    

    /*public void scheduleAppointment(DoctorModel doctor, String date, String time) 
    {
    	String appointmentID = UUID.randomUUID().toString();
        boolean success = appointmentManager.scheduleAppointment(appointmentID, model, doctor, date, time);
        if (success) 
        {
            view.showMessage("Appointment scheduled successfully with status 'Pending'.");
        } 
        
        else 
        {
            view.showMessage("Failed to schedule appointment. The doctor may not be available at this time.");
        }
    }*/

    public void scheduleAppointment(DoctorModel doctor, String date, String time) {
        String appointmentID = UUID.randomUUID().toString();
        if (appointmentManager.scheduleAppointment(appointmentID, model, doctor, date, time)) {
            DoctorAvailabilityCsvHelper.updateDoctorAvailability(doctor.getHospitalID(), date, time);
            view.showMessage("Appointment scheduled successfully.");
        } else {
            view.showMessage("Failed to schedule appointment. The time slot may not be available.");
        }
    }
    
    
    public void rescheduleAppointment(Appointment appointment, String date, String time) 
    {
        boolean success = appointmentManager.rescheduleAppointment(appointment, date, time);
        if (success) 
        {
            view.showMessage("Appointment rescheduled successfully with status 'Pending'.");
        } 
        
        else 
        {
            view.showMessage("Failed to reschedule appointment. The selected time slot is unavailable.");
        }
    }

    public void viewScheduledAppointmentsStatus() {
        List<Appointment> appointments = AppointmentsCsvHelper.getAppointmentsForPatient(model.getHospitalID());
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
