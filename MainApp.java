package SC2002_Assignment;

import java.util.Scanner;

import SC2002_Assignment.roles.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;




public class MainApp
{
    private static Map<String, User> users = new HashMap<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args)
    {
        initialiseUsers();
    }


    private static void initialiseUsers()
    {
        users.put("admin1", new User("admin1", "password", "Administrator"));
        users.put("doctor1", new User("doctor1", "password", "Doctor"));
        users.put("patient1", new User("patient1", "password", "Patient"));
        users.put("pharmacist1", new User("pharmacist1", "password", "Pharmacist"));
    }
} 