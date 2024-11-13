package SC2002_Assignment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PatientView
{

	public void viewMedicalRecord(String patientId, String name, LocalDate dob, String gender, 
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

    
	public void viewAvailableSlots(Doctor doctor, List<LocalDateTime> availableSlots) 
	{
        System.out.println("Available slots for Dr. " + doctor.getName() + ":");

        if (availableSlots.isEmpty()) 
        {
            System.out.println("No available slots.");
        } 
        else 
        {
            for (LocalDateTime slot : availableSlots) 
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

