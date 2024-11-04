package assignment;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;


public class Patient 
{
	private String name;
	private Date dob;
	private String gender;
	private String contactNumber;
	private String email;
	private String bloodType;
	private List<String> medicalRecord;
	
	
	public Patient(String name, Date dob, String gender, String contactNumber, String email, String bloodType)
	{
		this.name = name;
		this.dob = dob;
		this.gender = gender;
		this.contactNumber = contactNumber;
		this.email = email;
		this.bloodType = bloodType;
		this.medicalRecord = new ArrayList<>();
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

    public void setContactInfo(String contactNumber) 
    {
        this.contactNumber = contactNumber;
    }
    
    public void setEmail(String email)
    {
    	this.email = email;
    }

    public String getBloodType() 
    {
        return bloodType;
    }

    public List<String> getMedicalRecord() 
    {
        return medicalRecord;
    }

    public void viewMedicalRecord() 
    {
//    	System.out.println("Medical Record for Patient ID:", )
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
        if (newContactNumber != null) 
        {
            this.contactNumber = newContactNumber;
        }
        
        if (newEmail != null)
        {
        	this.email = newEmail;
        }
    }
    
    public void viewAvailAppointment()
    {
    	
    }
    
    public void scheduleAppointment()
    {
    		
    }
    
    public void rescheduleAppointment()
    {
    	
    }
    
    public void cancelAppointment()
    {
    	
    }
    
    public void viewScheduled()
    {
    	
    }
    
    public void viewPastOutcome()
    {
    	
    }
    
}
