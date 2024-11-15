

public class Password
{
    
    private String password;

    /* Password Constructor with the default password set */
    public Password()
    {
        password = "password";
    }

    /* Method to change the password ONLY
     * @param new password from user
     */
    public void changePassword(String newPassword)
    {
        password = newPassword;
    }

    /* Return true if the password matches
     * @param checking the string to see if it matches
     */
    public boolean checkPassword(String check)
    {
        return password.equals(check);
    }
}