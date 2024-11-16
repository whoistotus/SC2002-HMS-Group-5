import java.util.List;
import java.util.Scanner;
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
        view.viewMedicalRecord();
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

    public void updatePersonalInformation() {
        Scanner scanner = new Scanner(System.in);
    
        System.out.println("What would you like to update?");
        System.out.println("1. Contact Number");
        System.out.println("2. Email");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
    
        switch (choice) {
            case 1:
                System.out.print("Enter new contact number: ");
                String newContactNumber = scanner.nextLine();
                updateContactNumber(newContactNumber);
                break;
            case 2:
                System.out.print("Enter new email: ");
                String newEmail = scanner.nextLine();
                updateEmail(newEmail);
                break;
            case 3:
                System.out.println("Exiting...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    public void viewAvailableSlots() {
        List<DoctorAvailability> slots = DoctorAvailabilityCsvHelper.loadDoctorAvailability();
        view.viewAvailableSlots();
    }
    
    
    public void scheduleAppointment(String doctorID, String date, String time) {
        DoctorModel doctor = DoctorAvailabilityCsvHelper.getDoctorById(doctorID);
        if (doctor == null) {
            view.showMessage("Invalid Doctor ID!");
            return;
        }
    
        
        boolean success = appointmentManager.scheduleAppointment(model, doctor, date, time);
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
            view.viewScheduledAppointments();
        }
    }
    
    public void cancelAppointment(String appointmentID) 
    {
        boolean success = appointmentManager.cancelAppointment(appointmentID);
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
