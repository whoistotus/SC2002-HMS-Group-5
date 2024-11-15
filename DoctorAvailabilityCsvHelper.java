
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
                    // Skip empty lines
                    continue;
                }
                
                String[] values = line.split(",");
                
                // Ensure the line has the correct number of columns
                if (values.length < 4) {
                    System.out.println("Warning: Skipping malformed line: " + line);
                    continue;
                }
    
                String doctorID = values[0].trim();
                String date = values[1].trim();
                String startTime = values[2].trim();
                String endTime = values[3].trim();
    
                DoctorAvailability availability = new DoctorAvailability(doctorID, date, startTime, endTime);
                availabilityList.add(availability);
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
    


    public static boolean updateDoctorAvailability(String doctorId, String date, String time) {
        List<String[]> updatedEntries = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("data/DoctorAvailability.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (!(data[0].equals(doctorId) && data[1].equals(date) && data[2].equals(time))) {
                    updatedEntries.add(data);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/DoctorAvailability.csv"))) {
            for (String[] entry : updatedEntries) {
                bw.write(String.join(",", entry));
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
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
        List<DoctorModel> doctors = loadDoctors();
        for (DoctorModel doctor : doctors) {
            if (doctor.getHospitalID().equals(doctorID)) {
                return doctor;
            }
        }
        return null;
    }

    public static List<String> getAvailableSlots(String doctorID, String date) {
        List<String> availableSlots = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("data/DoctorAvailability.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(doctorID) && data[1].equals(date)) {
                    availableSlots.add(data[2].trim()); // Add time slot
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return availableSlots;
    }
    
}
