package SC2002_Assignment;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;



public class Administrator extends HospitalStaff
{
    // private String name;
    // private String staff;
    // private String age;

    Scanner sc = new Scanner(System.in);
    private List<HospitalStaff> staffList;

    //Constructor
    public Administrator(String hospitalID, String password, String userRole, String name, String gender, int age)
    {
        super(hospitalID, password, userRole, name, gender, age);
        super.setuserRole("Administrator");

        System.out.println("Enter Administrator name: ");
        String adminName = sc.nextLine();
        super.setName(adminName);

        System.out.println("Enter Administrator gender: ");
        String adminGender = sc.nextLine();
        super.setGender(adminGender);

        System.out.println("Enter Administrator age: ");
        int adminAge = sc.nextInt();
        super.setAge(adminAge);
        
    }

    public void updateStaffInfo(String hospitalID, String password, String userRole, String name, String gender, int age)
    {
        for (HospitalStaff staff : staffList)
        {
            if (staff.getHospitalID() == hospitalID)
            {
                staff.setName(name);
                staff.setuserRole(userRole);
                staff.setGender(gender);
                staff.setAge(age);
            }
        }
    }

    public void addStaff(String hospitalID, String password, String userRole, String name, String gender, int age)
    {
        HospitalStaff newStaff = new HospitalStaff(hospitalID, password, userRole, name, gender, age);
        staffList.add(newStaff);
    }

    public void removeStaff(String hospitalID)
    {
        for (HospitalStaff staff : staffList)
        {
            if (staff.getHospitalID() == hospitalID)
            {
                staffList.remove(hospitalID);
                break;
            }
        }
    }

    public List<Appointment> viewScheduledAppointments(String patientID)
    {

    }

    public List<Medication> addMedication(Medication medication, int removeAmount)
    {
        
    }

    public List<Medication> removeMedication(Medication medication, int removeAmount)
    {
        
    }
    
    public void viewHospitalStaffs(){

    }

    public List <HospitalStaff> manageHospitalStaffs(){

    }

    public void viewMedicationInventory() {

    }

    public List <ReplenishmentRequest> viewReplenishmentRequest(){

    }

    public void approveReplenishmentRequest(ReplenishmentRequest reqForm)
    {

    }


}