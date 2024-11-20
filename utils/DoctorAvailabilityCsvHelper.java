package utils;

import java.io.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import model.DoctorAvailability;
import model.DoctorModel;

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

                    DoctorModel doctor = DoctorAvailabilityCsvHelper.getDoctorById(doctorID);
                    if (doctor == null) {
                        return null;
                    }
    
                    DoctorAvailability availability = new DoctorAvailability(doctor, date, startTime, endTime);
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) { // Use true for append mode
            for (DoctorAvailability availability : availabilityList) {
                writer.write(String.format("%s,%s,%s,%s%n",
                        availability.getDoctorID(),
                        availability.getDate(),
                        availability.getStartTime(),
                        availability.getEndTime()));
            }
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

    public static boolean removeSlot(String doctorID, String date, String time) {
        List<DoctorAvailability> availabilityList = loadDoctorAvailability();
        List<DoctorAvailability> updatedList = new ArrayList<>();
    
        for (DoctorAvailability availability : availabilityList) {
            if (availability.getDoctorID().equals(doctorID) && availability.getDate().equals(date)) {
                // Check if the slot matches the scheduled time
                if (availability.getStartTime().equals(time)) {
                    // Skip this slot to "remove" it
                    continue;
                }
            }
            updatedList.add(availability);
        }
    
        // Save the updated availability back to the CSV
        saveDoctorAvailability(updatedList);
        return true;
    }

    public static boolean addSlot(String doctorID, String date, String startTime, String endTime) {
        List<DoctorAvailability> availabilityList = loadDoctorAvailability();
        DoctorModel doctor = getDoctorById(doctorID);
        if (doctor == null) {
            return false;
        }
    
        // Check for overlap or adjacent slots and merge
        for (DoctorAvailability availability : availabilityList) {
            if (availability.getDoctorID().equals(doctorID) && availability.getDate().equals(date)) {
                LocalTime newStart = LocalTime.parse(startTime);
                LocalTime newEnd = LocalTime.parse(endTime);
                LocalTime existingStart = LocalTime.parse(availability.getStartTime());
                LocalTime existingEnd = LocalTime.parse(availability.getEndTime());
    
                if ((newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart)) ||
                    newStart.equals(existingStart) || newEnd.equals(existingEnd)) {
                    availability.setStartTime(newStart.isBefore(existingStart) ? startTime : availability.getStartTime());
                    availability.setEndTime(newEnd.isAfter(existingEnd) ? endTime : availability.getEndTime());
                    saveDoctorAvailability(availabilityList); // Save the updated availability
                    return true;
                }
            }
        }
    
        // Add a new availability slot if no overlap
        DoctorAvailability newSlot = new DoctorAvailability(doctor, date, startTime, endTime);
        availabilityList.add(newSlot);
        saveDoctorAvailability(availabilityList);
        return true;
    }
    
}
