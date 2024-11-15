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
    
    
    public void scheduleAppointment(String doctorID, String date, String time) {
        DoctorModel doctor = DoctorAvailabilityCsvHelper.getDoctorById(doctorID);
        if (doctor == null) {
            view.showMessage("Invalid Doctor ID!");
            return;
        }
    
        String appointmentID = UUID.randomUUID().toString();
        boolean success = appointmentManager.scheduleAppointment(appointmentID, model, doctor, date, time);
        if (success) {
            view.showMessage("Appointment scheduled successfully with status 'Pending'.");
        } else {
            view.showMessage("Failed to schedule appointment. The selected time slot is unavailable.");
        }
    }
    
    
    
    public void rescheduleAppointment(String appointmentID, String newDate, String newTime) {
        // Fetch the appointment
        Appointment appointment = model.getAppointmentById(appointmentID);
        if (appointment == null) {
            view.showMessage("Appointment not found!");
            return;
        }
    
        // Attempt to reschedule
        boolean success = appointmentManager.rescheduleAppointment(appointmentID, newDate, newTime);
        if (success) {
            view.showMessage("Appointment rescheduled successfully!");
        } else {
            view.showMessage("Failed to reschedule. The new slot may be unavailable.");
        }
    }
    

    public void viewScheduledAppointments() {
        List<Appointment> appointments = AppointmentsCsvHelper.getAppointmentsForPatient(model.getHospitalID());
        if (appointments.isEmpty()) {
            view.showMessage("No scheduled appointments found.");
        } else {
            view.viewScheduledAppointments(appointments);
        }
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
