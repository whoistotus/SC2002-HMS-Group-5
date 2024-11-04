import java.util.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Patient 
{
	private final String patientId;
	private String name;
	private Date dob;
	private String gender;
	private String contactNumber;
	private String email;
	private String bloodType;
	private List<String> medicalRecord;
	private AppointmentManager appointmentManager;
	
	
	public Patient(String patientId, String name, Date dob, String gender, String contactNumber, String email, String bloodType)
	{
		this.patientId = patientId;
		this.name = name;
		this.dob = dob;
		this.gender = gender;
		this.contactNumber = contactNumber;
		this.email = email;
		this.bloodType = bloodType;
		this.medicalRecord = new ArrayList<>();
	}
	
	
	
	
	public String getPatientId()
	{
		return patientId;
	}

    public String getName()
    {
    	return name;
    }
	
	public Date getDob() 
    {
        return dob;
    }

    public String getGender() 
    {
        return gender;
    }

    public String getContactNumber() 
    {
        return contactNumber;
    }
    
    public String getEmail()
    {
    	return email;
    }
    
    public String getBloodType() 
    {
        return bloodType;
    }

    public List<String> getMedicalRecord() 
    {
        return medicalRecord;
    }

    
    
    
    public void setContactNumber(String contactNumber) 
    {
        this.contactNumber = contactNumber;
    }
    
    public void setEmail(String email)
    {
    	this.email = email;
    }

    

    public void viewMedicalRecord() 
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
    
    public void updatePersonalInfo(String newContactNumber, String newEmail) 
    {
        setContactNumber(newContactNumber);
        setEmail(newEmail);
        System.out.println("Contact information updated successfully.");
        
    }
    
    
    
    
    
    public void viewAvailableSlots(Doctor doctor)
    {
    	List<LocalDateTime> availableSlots = appointmentManager.viewAvailableSlots(doctor);
    	System.out.println("Available slots for Dr. " + doctor + ":");
    	for (LocalDateTime slot : availableSlots)
    	{
    		System.out.println(slot);
    	}
    }
    
    public void scheduleAppointment(Doctor doctor, LocalDateTime dateTime)
    {
    	try
    	{
    		appointmentManager.scheduleAppointment(this, doctor, dateTime);
    	}
    	catch(InvalidAppointmentException e)
    	{
    		System.out.println(e.getMessage());
    	}
    }
    
    public void rescheduleAppointment(Appointment appointment, LocalDateTime newDateTime)
    {
    	try
    	{
    		appointmentManager.rescheduleAppointment(this,  appointment,  newDateTime);
    	}
    	catch (InvalidAppointmentException e)
    	{
    		System.out.println(e.getMessage());
    	}
    }
    
    public void cancelAppointment(Appointment appointment)
    {
    	appointmentManager.cancelAppointment(this, appointment);
    }
    
    public void viewScheduled()
    {
    	
    }
    
    public void viewPastOutcome()
    {
    	
    }
    
}
