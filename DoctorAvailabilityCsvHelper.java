
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
            //System.out.println("Doctor availability successfully saved to CSV.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    


    public static boolean updateDoctorAvailability(String doctorId, String date, String time) {
        List<DoctorAvailability> availabilityList = loadDoctorAvailability();
        boolean updated = false;
    
        for (DoctorAvailability availability : availabilityList) {
            if (availability.getDoctorID().equals(doctorId) && availability.getDate().equals(date)) {
                // Block the booked time slot
                if (availability.isTimeSlotAvailable(time)) {
                    availability.blockTimeSlot(time); // Mark slot as unavailable
                    updated = true;
                }
            }
        }
    
        if (updated) {
            // Save the updated availability back to the CSV
            saveDoctorAvailability(availabilityList);
        } else {
        }
    
        return updated;
    }
    
    
    public static List<String> getAvailableSlots(String doctorID, String date) {
        List<String> availableSlots = new ArrayList<>();
        List<DoctorAvailability> availabilityList = loadDoctorAvailability();
    
        for (DoctorAvailability availability : availabilityList) {
            if (availability.getDoctorID().equals(doctorID) && availability.getDate().equals(date)) {
                int start = Integer.parseInt(availability.getStartTime().split(":")[0]);
                int end = Integer.parseInt(availability.getEndTime().split(":")[0]);
                for (int i = start; i < end; i++) {
                    availableSlots.add(String.format("%02d:00", i));
                }
            }
        }
        return availableSlots;
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
                    return new DoctorModel(values[0].trim(), values[5].trim(), values[2].trim(), values[1].trim(), "");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading StaffList.csv: " + e.getMessage());
        }
        return null;
    }
    
}
