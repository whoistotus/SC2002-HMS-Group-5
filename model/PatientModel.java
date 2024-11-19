package model;


import java.util.ArrayList;
import java.util.List;

import utils.AppointmentsCsvHelper;


public class PatientModel extends User 
{
    private String name;
    private String dob;
    private String gender;
    private String contactNumber;
    private String email;
    private String bloodType;
    private List<String> medicalRecord;

   
    public PatientModel(String hospitalID, String name, String dob, String gender, String bloodType, String email, String contactNumber, String password, String userRole) 
    {
        super(hospitalID, password, userRole);
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.email = email;
        this.bloodType = bloodType;
        this.medicalRecord = new ArrayList<>();
    }

    
    
    
    /*public String getPatientId() 
    {
    	return patientId; 
    }*/
    
    public String getName() 
    { 
    	return name; 
    }
    
    public String getDob() 
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



    public Appointment getAppointmentById(String appointmentID) {
        return AppointmentsCsvHelper.getAppointmentById(appointmentID);
    }
    
    public String toCsv() {
        return String.join(",",
                getHospitalID(),
                getName(),
                getDob(),
                getGender(),
                getBloodType(),
                getEmail(),
                getContactNumber()
        );
    }
    
    
}
    
