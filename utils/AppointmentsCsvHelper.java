package utils;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import model.Appointment;
import model.DoctorModel;
import model.PatientModel;

public class AppointmentsCsvHelper {
    private static final String FILE_PATH = "data/Appointments.csv";
    

    // Loads all appointments from the CSV file

    public static List<Appointment> loadAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines
                Appointment appointment = parseAppointment(line);
                if (appointment != null) {
                    appointments.add(appointment);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    private static Appointment parseAppointment(String line) {
        String[] values = line.split(",");
        if (values.length < 6) {
            return null;
        }

        try {
            String appointmentID = values[0].trim();
            String patientID = values[1].trim();
            String doctorID = values[2].trim();
            String date = values[3].trim();
            String time = values[4].trim();
            Appointment.AppointmentStatus status = Appointment.AppointmentStatus.valueOf(values[5].trim().toUpperCase());

            PatientModel patient = PatientListCsvHelper.getPatientById(patientID);
            if (patient == null) {
                return null;
            }

            DoctorModel doctor = DoctorAvailabilityCsvHelper.getDoctorById(doctorID);
            if (doctor == null) {
                return null;
            }

            return new Appointment(appointmentID, patient, doctor, date, time, status);
        } catch (Exception e) {
            return null;
        }
    }
    

    // Retrieves an appointment by its ID
    public static Appointment getAppointmentById(String appointmentID) {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            br.readLine(); // Skip header row
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String currentAppointmentID = values[0].trim();
                
                // Only proceed if the appointment ID matches the one we're looking for
                if (currentAppointmentID.equals(appointmentID)) {
                    String patientID = values[1].trim();
                    String doctorID = values[2].trim();
                    String date = values[3].trim();
                    String time = values[4].trim();
    
                    Appointment.AppointmentStatus status;
                    try {
                        status = Appointment.AppointmentStatus.valueOf(values[5].trim().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Warning: Invalid status '" + values[5] + "' in appointment " + appointmentID);
                        return null;
                    }
    
                    PatientModel patient = PatientListCsvHelper.getPatientById(patientID);
                    DoctorModel doctor = DoctorAvailabilityCsvHelper.getDoctorById(doctorID);
    
                    // Check if patient and doctor exist
                    if (patient != null && doctor != null) {
                        return new Appointment(appointmentID, patient, doctor, date, time, status);
                    } else {
                        System.out.println("Could not load appointment: Patient or Doctor not found for " + appointmentID);
                        return null;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // Return null if no matching appointment was found
    }

    
    // Retrieves all pending appointments for a specific doctor
    public static List<Appointment> getPendingAppointmentsForDoctor(String doctorID) {
    List<Appointment> pendingAppointments = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader("data/Appointments.csv"))) {
        String line;
        br.readLine(); // Skip the header row

        while ((line = br.readLine()) != null) {
            line = line.trim(); // Remove leading/trailing spaces
            if (line.isEmpty()) {
                System.out.println("Skipping empty line in CSV.");
                continue; // Skip empty lines
            }

            String[] values = line.split(",");
            if (values.length < 6) { // Validate the number of columns
                System.out.println("Skipping malformed line: " + line);
                continue;
            }

            String currentDoctorID = values[2].trim(); // DoctorID at index 2
            if (currentDoctorID.equals(doctorID)) {
                try {
                    Appointment.AppointmentStatus status = Appointment.AppointmentStatus.valueOf(values[5].trim().toUpperCase());
                    if (status == Appointment.AppointmentStatus.PENDING) {
                        PatientModel patient = PatientListCsvHelper.getPatientById(values[1].trim()); // PatientID at index 1
                        DoctorModel doctor = DoctorAvailabilityCsvHelper.getDoctorById(currentDoctorID);

                        if (patient != null && doctor != null) {
                            pendingAppointments.add(new Appointment(
                                values[0].trim(), // AppointmentID
                                patient,
                                doctor,
                                values[3].trim(), // Date
                                values[4].trim(), // Time
                                status
                            ));
                        } else {
                            System.out.println("Skipping appointment due to missing patient or doctor: " + line);
                        }
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Skipping appointment with invalid status: " + line);
                }
            }
        }
    } catch (IOException e) {
        System.out.println("Error reading the CSV file: " + e.getMessage());
        e.printStackTrace();
    }

    return pendingAppointments;
}

    

    // Saves the entire list of appointments back to the CSV file
    /*public static void saveAllAppointments(List<Appointment> appointments) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            writer.println("AppointmentID,PatientID,DoctorID,Date,Time,Status");
            for (Appointment appointment : appointments) {
                writer.printf("%s,%s,%s,%s,%s,%s%n",
                    appointment.getAppointmentID(),
                    appointment.getPatientID(),
                    appointment.getDoctorID(),
                    appointment.getAppointmentDate(),
                    appointment.getAppointmentTime(),
                    appointment.getStatus()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public static boolean saveAllAppointments(List<Appointment> appointments) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            bw.write("AppointmentID,PatientID,DoctorID,Date,Time,Status");
            bw.newLine();
            for (Appointment appointment : appointments) {
                bw.write(appointment.toCsv());
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update a specific appointment in the CSV
    public static boolean updateAppointment(Appointment updatedAppointment) {
        List<Appointment> appointments = loadAppointments();
        boolean found = false;

        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID().equals(updatedAppointment.getAppointmentID())) {
                appointment.setAppointmentDate(updatedAppointment.getAppointmentDate());
                appointment.setAppointmentTime(updatedAppointment.getAppointmentTime());
                appointment.setStatus(updatedAppointment.getStatus());
                found = true;
                break;
            }
        }

        if (found) {
            return saveAllAppointments(appointments);
        }
        return false; // Return false if the appointment was not found
    }


    // Updates the status of a specific appointment and saves the changes
    public static void updateAppointmentStatus(String appointmentID, Appointment.AppointmentStatus newStatus) {
        List<Appointment> appointments = loadAppointments();
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID().equals(appointmentID)) {
                appointment.setStatus(newStatus);
                break;
            }
        }
        saveAllAppointments(appointments);  // Save updated appointments list back to the CSV
    }


    public static boolean updateAllAppointments(List<Appointment> appointments) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/Appointments.csv"))) {
            bw.write("AppointmentID,PatientID,DoctorID,Date,Time,Status");
            bw.newLine();
            for (Appointment appointment : appointments) {
                bw.write(appointment.toCsv());
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    

    public static boolean addAppointment(Appointment appointment) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/Appointments.csv", true))) {
            writer.write(appointment.toCsv());
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Appointment> getAppointmentsForPatient(String patientId) {
        List<Appointment> patientAppointments = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("data/Appointments.csv"))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                /*if (data[1].equals(patientId)) {
                    Appointment.AppointmentStatus status = Appointment.AppointmentStatus.valueOf(data[5].trim().toUpperCase());
                    patientAppointments.add(new Appointment(data[0], data[1], data[2], data[3], data[4], status));
                }*/

                if (data[1].equals(patientId)) { // match the patient ID
                    // fetch PatientModel and DoctorModel
                    PatientModel patient = PatientListCsvHelper.getPatientById(data[1]);
                    DoctorModel doctor = DoctorAvailabilityCsvHelper.getDoctorById(data[2]);
    
                    if (patient != null && doctor != null) {
                        // Convert status from String to AppointmentStatus enum
                        Appointment.AppointmentStatus status = Appointment.AppointmentStatus.valueOf(data[5].trim().toUpperCase());
    
                        // Create and add the Appointment object
                        patientAppointments.add(new Appointment(data[0], patient, doctor, data[3], data[4], status));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patientAppointments;
    }
    
    
}
