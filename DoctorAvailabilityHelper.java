package SC2002_Assignment;

import java.io.*;
import java.util.*;

public class DoctorAvailabilityHelper {
    private static final String DOCTOR_AVAILABILITY_FILE_PATH = "data/DoctorAvailability.csv";

    // Method to get available times for a specific doctor and date
    public static List<String> getAvailableSlots(String doctorId, String date) {
        List<String> availableSlots = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(DOCTOR_AVAILABILITY_FILE_PATH))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(doctorId) && data[1].equals(date)) {
                    availableSlots.add(data[2]); // Add available time slot
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return availableSlots;
    }
}

