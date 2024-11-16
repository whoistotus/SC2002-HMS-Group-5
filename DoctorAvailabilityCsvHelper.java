
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorAvailabilityCsvHelper {
    private static final String FILE_PATH = "data/DoctorAvailability.csv";

    public static List<DoctorAvailability> loadDoctorAvailability() {
        List<DoctorAvailability> availabilityList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }
    
                String[] values = line.split(",");
                if (values.length < 4) {
                    System.out.println("Warning: Skipping malformed line: " + line);
                    continue;
                }
    
                try {
                    String doctorID = values[0].trim();
                    String date = values[1].trim();
                    String startTime = values[2].trim();
                    String endTime = values[3].trim();
    
                    DoctorAvailability availability = new DoctorAvailability(doctorID, date, startTime, endTime);
                    availabilityList.add(availability);
                } catch (Exception e) {
                    System.out.println("Error parsing line: " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return availabilityList;
    }
    
    
    public static void saveDoctorAvailability(List<DoctorAvailability> availabilityList) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, false))) {
            writer.println("DoctorID,Date,StartTime,EndTime"); // Write header
            for (DoctorAvailability availability : availabilityList) {
                writer.printf("%s,%s,%s,%s%n",
                        availability.getDoctorID(),
                        availability.getDate(),
                        availability.getStartTime(),
                        availability.getEndTime());
            }
            System.out.println("Doctor availability successfully saved to CSV.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    


    public static boolean updateDoctorAvailability(String doctorID, String date, String time) {
        List<DoctorAvailability> availabilities = loadDoctorAvailability();
        boolean updated = false;
    
        for (DoctorAvailability availability : availabilities) {
            if (availability.getDoctorID().equals(doctorID) && availability.getDate().equals(date)) {
                if (availability.isTimeSlotAvailable(time)) {
                    availability.blockTimeSlot(time); // Block the specific time slot
                    updated = true;
                    break;
                }
            }
        }
    
        if (updated) {
            saveDoctorAvailability(availabilities); // Save updated availability back to the CSV
            System.out.println("Debug: Updated availability for doctor " + doctorID + " on " + date + " at " + time);
        } else {
            System.out.println("Debug: Failed to update availability for doctor " + doctorID + " on " + date + " at " + time);
        }
    
        return updated;
    }
    

    public static List<DoctorModel> loadDoctors() {
        List<DoctorModel> doctors = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("data/DoctorAvailability.csv"))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
    
                // Ensure the line has the expected number of columns (assuming 3 here)
                if (data.length < 3) {
                    System.out.println("Warning: Skipping malformed line: " + line);
                    continue;
                }
    
                String doctorID = data[0].trim();
                String name = data[1].trim();
                String specialization = data[2].trim();
    
                // Create and add DoctorModel object
                DoctorModel doctor = new DoctorModel(doctorID, "defaultPassword", "Doctor", name, specialization);
                doctors.add(doctor);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doctors;
    }
    

    public static DoctorModel getDoctorById(String doctorID) {
        try (BufferedReader br = new BufferedReader(new FileReader("data/StaffList.csv"))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].trim().equals(doctorID) && values[2].trim().equalsIgnoreCase("Doctor")) {
                    System.out.println("Debug: Found doctor with ID " + doctorID);
                    return new DoctorModel(values[0].trim(), values[5].trim(), values[2].trim(), values[1].trim(), "");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading StaffList.csv: " + e.getMessage());
        }
        System.out.println("Debug: Doctor with ID " + doctorID + " not found.");
        return null;
    }
    

    public static List<String> getAvailableSlots(String doctorID, String date) {
        List<DoctorAvailability> availabilities = loadDoctorAvailability();
        List<String> availableSlots = new ArrayList<>();
    
        for (DoctorAvailability availability : availabilities) {
            if (availability.getDoctorID().equals(doctorID) && availability.getDate().equals(date)) {
                int start = Integer.parseInt(availability.getStartTime().split(":")[0]);
                int end = Integer.parseInt(availability.getEndTime().split(":")[0]);
    
                for (int hour = start; hour < end; hour++) {
                    String slot = String.format("%02d:00", hour);
                    if (availability.isTimeSlotAvailable(slot)) {
                        availableSlots.add(slot);
                    }
                }
            }
        }
    
        return availableSlots;
    }
    
}
