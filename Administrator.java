package SC2002_Assignment;

import java.util.Scanner;
import java.util.List;



public class Administrator extends User
{
    private String name;
    private String staff;
    private String age;

    Scanner sc = new Scanner(System.in);
    private List<Staff> staffList;

    //Constructor
    public Administrator()
    {
        super("","","", 0); //name, gender, role, age = 0
        super.setRole("Administrator");

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

    public void updateStaffInfo(int staffID, String name, String role, String gender, int age)
    {
        for (Staff staff : staffList)
        {
            if (staff.getStaffID() == staffID)
            {
                staff.setName(name);
                staff.setRole(role);
                staff.setGender(gender);
                staff.setAge(age);
            }
        }
    }

    public void addStaff(String name, String role, String gender, int age)
    {
        Staff newStaff = new Staff(name, role, gender, age);
        staffList.add(newStaff);
    }

    public void removeStaff(int staffID)
    {
        for (Staff staff : staffList)
        {
            if (staff.getStaffID() == staffID)
            {
                staffList.remove(staffID);
                break;
            }
        }
    }

    public List<Appointments> viewScheduledAppointments(String patientID)
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

    public List <HospitalStaffs> manageHospitalStaffs(){

    }

    public void viewMedicationInventory() {

    }

    public List <ReplenishmentRequest> viewReplenishmentRequest(){

    }

    public void approveReplenishmentRequest(ReplenishmentRequest reqForm)


}