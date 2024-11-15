

public class User
{
    private String hospitalID;
    private String password;
    private String userRole;

    public User(String hospitalID, String password, String userRole)
    {
        this.hospitalID = hospitalID;
        this.userRole = userRole;
        this.password = password; 

        
    }

    // public boolean login(String enteredID, String enteredPassword)
    // {
    //     try
    //     {
    //         if (enteredID == null || enteredPassword == null) 
    //         {
    //             throw new IllegalArgumentException("User ID and password cannot be null.");
    //         }
    //         if (this.userID.equals(enteredID) && this.userPassword.equals(enteredPassword)) 
    //         {
    //             return true;
    //         }
    //         else 
    //         {
    //             throw new Exception("Invalid user ID or password.");
    //         }
    //     }

    //     catch (Exception e)
    //     {
    //         System.out.println("Login failed: " + e.getMessage());
    //         return false;
    //     }
    // }


    // public void changePassword(String newPassword)
    // {
    //     try
    //     {
    //         if (newPassword == null || newPassword.isEmpty()) 
    //         {
    //             throw new IllegalArgumentException("Password cannot be null or empty.");
    //         }
    //         if (newPassword.length() < 6) //can add additional conditions to newPassword 
    //         {
    //             throw new Exception("Password must be at least 6 characters long.");
    //         }
    //         this.userPassword = newPassword;
    //         System.out.println("Password changed successfully.");
    //     }
    //     catch (Exception e)
    //     {
    //         System.out.println("Password change failed: " + e.getMessage());
    //     }
    // }

    public String getHospitalID()
    {
        return hospitalID;
    }

    public String getPassword()
    {
        return password;
    }

    public String getUserRole()
    {
        return userRole;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
