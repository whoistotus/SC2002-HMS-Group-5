package SC2002_Assignment;

import java.util.Scanner;


public class Staff
{
    private String name;
    private String gender;
    private String role;
    private int staffID;
    private int age;

    static int staffCounter = 0;

    public Staff(String name, String gender, String role, int age)
    {
        this.name = name;
        this.gender = gender;
        this.role = role;
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
        this.gender = gender;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public int getStaffID()
    {
        return staffID;
    }

    public void setStaffID(int staffID)
    {
        this.staffID = staffID;
    }


}