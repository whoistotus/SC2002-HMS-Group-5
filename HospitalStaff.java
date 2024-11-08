package SC2002_Assignment;


public class HospitalStaff extends User
{
    private String name;
    private String gender;
    private String userRole;
    private String hospitalID;
    private int age;

    static int staffCounter = 0;

    public HospitalStaff(String hospitalID, String password, String userRole, String name, String gender, int age)
    {
        super(hospitalID, password, userRole);
        this.userRole = userRole;
        this.name = name;
        this.gender = gender;
        this.age = age;

    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        if (gender == null)
        {
            System.out.println("Invalid gender.");
            return;
        }
        this.gender = gender;
    }

    public String getuserRole()
    {
        return userRole;
    }

    public void setuserRole(String role)
    {
        if (role == null)
        {
            System.out.println("Invalid role.");
            return;
        }
        this.userRole = role;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        if (age > 0 && age <130)
        {
            this.age = age;
        }
        else
        {
            System.out.println("Input is an invalid age. Age has not been updated.");
        }
        
    }

    public String getHospitalID()
    {
        return hospitalID;
    }

    public void setHospitalID(String HospitalID)
    {
        this.hospitalID = HospitalID;
    }


}