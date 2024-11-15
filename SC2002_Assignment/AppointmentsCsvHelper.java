package SC2002_Assignment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentsCsvHelper {
    private static final String FILE_PATH = "data/Appointments.csv";
    

    // Loads all appointments from the CSV file
    public static List<Appointment> loadAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            br.readLine(); // Skip header row
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String appointmentID = values[0];
                String patientID = values[1];
                String doctorID = values[2];
                String date = values[3]; // Keep date as a String
                String time = values[4];

                // Trim and check if status is valid
                Appointment.AppointmentStatus status;
                try {
                    status = Appointment.AppointmentStatus.valueOf(values[5].trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Warning: Invalid status '" + values[5] + "' in appointment " + appointmentID);
                    continue; // Skip this appointment if status is invalid
                }

                Appointment appointment = new Appointment(appointmentID, patientID, doctorID, date, time, status);
                appointments.add(appointment);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    // Retrieves an appointment by its ID
    public static Appointment getAppointmentById(String appointmentID) {
        List<Appointment> appointments = loadAppointments();
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID().equals(appointmentID)) {
                return appointment;
            }
        }
        return null;
    }

    // Retrieves all pending appointments for a specific doctor
    public static List<Appointment> getPendingAppointmentsForDoctor(String doctorID) {
        List<Appointment> appointments = loadAppointments();
        List<Appointment> pendingAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getDoctorID().equals(doctorID) && appointment.getStatus() == Appointment.AppointmentStatus.PENDING) {
                pendingAppointments.add(appointment);
            }
        }
        return pendingAppointments;
    }

    // Saves the entire list of appointments back to the CSV file
    public static void saveAllAppointments(List<Appointment> appointments) {
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
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data/Appointments.csv", true))) {
            bw.write(appointment.toCsv());
            bw.newLine();
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
                if (data[1].equals(patientId)) {
                    Appointment.AppointmentStatus status = Appointment.AppointmentStatus.valueOf(data[5].trim().toUpperCase());
                    patientAppointments.add(new Appointment(data[0], data[1], data[2], data[3], data[4], status));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patientAppointments;
    }
    
    
}