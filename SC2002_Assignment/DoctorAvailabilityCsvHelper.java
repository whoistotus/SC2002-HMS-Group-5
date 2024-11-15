package SC2002_Assignment;

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
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            writer.println("DoctorID,Date,StartTime,EndTime"); // Header
            for (DoctorAvailability availability : availabilityList) {
                writer.printf("%s,%s,%s,%s%n",
                    availability.getDoctorID(),
                    availability.getDate(),
                    availability.getStartTime(),
                    availability.getEndTime()
                );
            }
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

    public static DoctorModel getDoctorById(String doctorID) {
        List<DoctorModel> doctors = loadDoctors();
        for (DoctorModel doctor : doctors) {
            if (doctor.getHospitalID().equals(doctorID)) {
                return doctor;
            }
        }
        return null;
    }
    
}
