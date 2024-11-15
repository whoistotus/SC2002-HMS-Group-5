
import java.util.*;

public class PatientView
{
    PatientController controller;
    public void viewMedicalRecord(MedicalRecord record) {
        if (record == null) {
            System.out.println("No medical record found for the patient.");
            return;
        }
    
        System.out.println("Medical Record for Patient: " + record.getPatientID());
        System.out.println("==============================");
        System.out.println("Name: " + record.getName());
        System.out.println("Date of Birth: " + record.getDOB());
        System.out.println("Gender: " + record.getGender());
        System.out.println("Contact Number: " + record.getContactNumber());
        System.out.println("Blood Type: " + record.getBloodType());
        System.out.println("Email: " + record.getEmail());
    
        System.out.println("\nPast Diagnoses:");
        for (String diagnosis : record.getPastDiagnoses()) {
            System.out.println("- " + diagnosis);
        }
    
        System.out.println("\nPast Treatments:");
        for (String treatment : record.getPastTreatments()) {
            System.out.println("- " + treatment);
        }
    
        System.out.println("\nCurrent Diagnoses:");
        for (String diagnosis : record.getCurrentDiagnoses()) {
            System.out.println("- " + diagnosis);
        }
    
        System.out.println("\nCurrent Treatments:");
        for (String treatment : record.getCurrentTreatments()) {
            System.out.println("- " + treatment);
        }
    
        System.out.println("\nPrescriptions:");
        for (String prescription : record.getPrescriptions()) {
            System.out.println("- " + prescription);
        }
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
                controller.updateContactNumber(newContactNumber);
                break;
            case 2:
                System.out.print("Enter new email: ");
                String newEmail = scanner.nextLine();
                controller.updateEmail(newEmail);
                break;
            case 3:
                System.out.println("Exiting...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    

    /*public void viewAvailableSlots(DoctorModel doctor, List<String> availableSlots) 
	{
        System.out.println("Available slots for Dr. " + doctor.getName() + ":");

        if (availableSlots.isEmpty()) 
        {
            System.out.println("No available slots.");
        } 
        else 
        {
            for (String slot : availableSlots) 
            {
                System.out.println("Available on: " + slot);
            }
        }
    }*/

    public void viewAvailableSlots(List<DoctorAvailability> slots) {
        System.out.println("Available Appointment Slots:");
        if (slots.isEmpty()) {
            System.out.println("No available slots at the moment.");
        } else {
            for (DoctorAvailability slot : slots) {
                System.out.println(slot.toString());
            }
        }
    }
    
   
	public void viewScheduledAppointmentStatus(List<Appointment> appointments) 
	{
        System.out.println("Scheduled Appointments and Their Status:");

        if (appointments.isEmpty()) 
        {
            System.out.println("No scheduled appointments.");
        } 
        
        else 
        {
            for (Appointment appointment : appointments) 
            {
                System.out.println("Appointment with " + appointment.getDoctor().getName() +
                                   " on " + appointment.getAppointmentTime() +
                                   " - Status: " + appointment.getStatus());
            }
        }
    }
    
    public void viewAppointmentOutcome(AppointmentOutcomeRecord outcome) 
    {
        if (outcome == null) 
        {
            System.out.println("No outcome available for this appointment.");
        } 
        
        else 
        {
            System.out.println("Appointment Outcome:");
            System.out.println("Service: " + outcome.getServiceType());
            System.out.println("Outcome Description: " + outcome.getConsultationNotes());
            System.out.println("Prescription: " + outcome.getMedications());
        }
    }

    public void scheduleAppointment() {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter Doctor ID: ");
        String doctorID = scanner.nextLine();
    
        System.out.print("Enter appointment date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
    
        System.out.print("Enter appointment time (HH:MM): ");
        String time = scanner.nextLine();
    
        // Pass input back to controller
        controller.scheduleAppointment(doctorID, date, time);
    }
    
    

    public void rescheduleAppointment() {
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Enter Appointment ID to reschedule: ");
        String appointmentID = scanner.nextLine();
    
        System.out.print("Enter new appointment date (YYYY-MM-DD): ");
        String newDate = scanner.nextLine();
    
        System.out.print("Enter new appointment time (HH:MM): ");
        String newTime = scanner.nextLine();
    
        // Pass input back to controller
        controller.rescheduleAppointment(appointmentID, newDate, newTime);
    }

    
    public void showMessage(String message)
    {
        System.out.println(message);
    }
}

