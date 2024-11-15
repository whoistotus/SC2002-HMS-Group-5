
import java.util.*;

public class PatientView
{
    /*private PatientModel patientModel;
    public void PatientMenu() {
        while (true) {
            System.out.println("Patient Menu for " + patientModel.getName());
            System.out.println("1. View Medical Records");
            System.out.println("2. Schedule Appointment");
            System.out.println("3. Reschedule Appointment");
            System.out.println("4. Cancel Appointment");
            System.out.println("5. View Available Appointment Slots");
            System.out.println("6. View Appointment Outcome");
            System.out.println("7. Record Appointment Outcome");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1 -> viewMedicalRecords();
                case 2 -> updatePatientMedicalRecords();
                case 3 -> setAvailability();
                case 4 -> viewPersonalSchedule();
                case 5 -> acceptAppointment();
                case 6 -> declineAppointment();
                case 7 -> recordAppointmentOutcome();
                case 8 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }*/

	public void viewMedicalRecord(String patientId, String name, String dob, String gender, 
            String contactNumber, String email, String bloodType, 
            List<String> medicalRecord) 
    {
    	System.out.println("Medical Record for Patient ID:" + patientId);
    	System.out.println("Name: " + name);
        System.out.println("Date of Birth: " + dob);
        System.out.println("Gender: " + gender);
        System.out.println("Contact Number: " + contactNumber);
        System.out.println("Email: " + email);
        System.out.println("Blood Type: " + bloodType);
        System.out.println("Medical Records: ");
        for (String record : medicalRecord) 
        {
            System.out.println(record);
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

    
	public void viewAvailableSlots(DoctorModel doctor, List<String> availableSlots) 
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
    
    public void showMessage(String message)
    {
        System.out.println(message);
    }
}

