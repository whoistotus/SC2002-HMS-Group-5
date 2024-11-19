package controller;
import java.util.ArrayList;
import java.util.List;

import model.Appointment;
import model.DoctorModel;
import model.PatientModel;
import model.Appointment.AppointmentStatus;
import utils.AppointmentsCsvHelper;
import utils.DoctorAvailabilityCsvHelper;

public class AppointmentManager 
{
    private List<Appointment> appointments;
    private static int appointmentCounter = 0;
    

    public AppointmentManager() 
    {
        this.appointments = new ArrayList<>();
    }

    // get available slots for a specific doctor
    public List<String> getAvailableSlots(DoctorModel doctor, String date) 
    {
        List<String> availableSlots = new ArrayList<>();
        availableSlots.add("10:00");
        availableSlots.add("11:00");
        // remove slots that are already taken by the doctor's appointments
        for (Appointment appointment : appointments) 
        {
            if (appointment.getDoctor().equals(doctor) && availableSlots.contains(appointment.getAppointmentTime())) 
            {
                availableSlots.remove(appointment.getAppointmentTime());
            }
        }
        
        return availableSlots;
    }

    private static synchronized String generateAppointmentID() {
        appointmentCounter++;
        return String.format("AP%03d", appointmentCounter);
    }
    
    public boolean scheduleAppointment(PatientModel patient, DoctorModel doctor, String date, String time) {
        // Get available slots for the doctor
        List<String> availableSlots = DoctorAvailabilityCsvHelper.getAvailableSlots(doctor.getHospitalID(), date);
        if (!availableSlots.contains(time)) {
            System.out.println("Time slot " + time + " on " + date + " is unavailable for doctor " + doctor.getHospitalID());
            return false; // Slot unavailable
        }
    
        // Check for conflicts with existing appointments
        for (Appointment appointment : appointments) {
            if (appointment.getDoctor().getHospitalID().equals(doctor.getHospitalID()) &&
                appointment.getAppointmentDate().equals(date) &&
                appointment.getAppointmentTime().equals(time)) {
                System.out.println("Appointment conflict for " + time + " on " + date);
                return false; // Conflict with an existing appointment
            }
        }
    
        // Generate a new appointment ID
        String appointmentID = generateAppointmentID();
    
        // Create and add a new appointment
        Appointment newAppointment = new Appointment(appointmentID, patient, doctor, date, time, Appointment.AppointmentStatus.PENDING);
        appointments.add(newAppointment);
    
        // Update Doctor's availability and CSV
        DoctorAvailabilityCsvHelper.updateDoctorAvailability(doctor.getHospitalID(), date, time);
        AppointmentsCsvHelper.addAppointment(newAppointment);
        return true;
    }
    
    public boolean rescheduleAppointment(String appointmentID, String newDate, String newTime) {
        Appointment appointment = AppointmentsCsvHelper.getAppointmentById(appointmentID);
        if (appointment == null) {
            System.out.println("Appointment not found!");
            return false;
        }
    
        String doctorID = appointment.getDoctorID();
        String oldDate = appointment.getAppointmentDate();
        String oldStartTime = appointment.getAppointmentTime();
        
        // Calculate the end time for the original old slot (hardcoded to be 1 hour after start time)
        String[] timeParts = oldStartTime.split(":");
        int hour = Integer.parseInt(timeParts[0]) + 1; // Add 1 hour
        String oldEndTime = String.format("%02d:%s", hour, timeParts[1]);
    
        // Free the old time slot
        boolean oldSlotAdded = DoctorAvailabilityCsvHelper.addSlot(doctorID, oldDate, oldStartTime, oldEndTime);
    
        // Check if the new slot is available
        List<String> availableSlots = DoctorAvailabilityCsvHelper.getAvailableSlots(doctorID, newDate);
        if (!availableSlots.contains(newTime)) {
            System.out.println("New time slot is unavailable!");
            return false;
        }
    
        // Update the appointment details
        appointment.setAppointmentDate(newDate);
        appointment.setAppointmentTime(newTime);
        appointment.setStatus(Appointment.AppointmentStatus.PENDING);
    
        // Mark the new time slot as unavailable
        boolean newSlotRemoved = DoctorAvailabilityCsvHelper.removeSlot(doctorID, newDate, newTime);
    
        // Update the appointment in the CSV
        AppointmentsCsvHelper.updateAppointment(appointment);
    
        if (oldSlotAdded && newSlotRemoved) {
            System.out.println("Appointment rescheduled successfully! Old slot restored and new slot booked.");
            return true;
        } else {
            System.out.println("Appointment rescheduled, but there was an issue updating the availability.");
            return true;
        }
    }
    

    // for the doctor to update appointment status
    public void updateAppointmentStatus(Appointment appointment, Appointment.AppointmentStatus newStatus) 
    {
        appointment.setStatus(newStatus);
    }

    public boolean cancelAppointment(String appointmentID) {
        Appointment appointment = AppointmentsCsvHelper.getAppointmentById(appointmentID);
        if (appointment == null) {
            System.out.println("Appointment not found for ID: " + appointmentID);
            return false;
        }
    
        DoctorAvailabilityCsvHelper.updateDoctorAvailability(
            appointment.getDoctor().getHospitalID(),
            appointment.getAppointmentDate(),
            appointment.getAppointmentTime()
        );
    
        List<Appointment> allAppointments = AppointmentsCsvHelper.loadAppointments();
        allAppointments.removeIf(a -> a.getAppointmentID().equals(appointmentID));
        AppointmentsCsvHelper.saveAllAppointments(allAppointments);
    
        return true;
    }
    

    // appointments for a specific patient
    public List<Appointment> getAppointmentsForPatient(String patientId) 
    {
        List<Appointment> patientAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) 
        {
            if (appointment.getPatient().getHospitalID().equals(patientId)) 
            {
                patientAppointments.add(appointment);
            }
        }
        return patientAppointments;
    }


    public Appointment getAppointmentByPatientId(String patientId) 
    {
        for (Appointment appointment : appointments) 
        {
            if (appointment.getPatientID().equals(patientId)) 
            {
                return appointment;
            }
        }
        return null; // Return null if no appointment is found
    }

    
}

/*import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentManager {
    private List<Appointment> appointments;

    public AppointmentManager() {
        this.appointments = new ArrayList<>();
    }

    /**
     * Schedules a new appointment for the patient with the specified doctor and time.
     * @param patient The patient requesting the appointment
     * @param doctor The doctor for the appointment
     * @param dateTime The date and time of the appointment
     * @throws InvalidAppointmentException if the slot is unavailable
     
    public void scheduleAppointment(PatientModel patient, Doctor doctor, LocalDateTime dateTime) throws InvalidAppointmentException {
        if (isSlotUnavailable(doctor, dateTime)) {
            throw new InvalidAppointmentException("The requested time slot is unavailable.");
        }
        Appointment appointment = new Appointment(patient, doctor, dateTime);
        appointments.add(appointment);
        System.out.println("Appointment scheduled successfully.");
    }

    /**
     * Reschedules an existing appointment to a new time.
     * @param patient The patient requesting the reschedule
     * @param appointment The existing appointment to reschedule
     * @param newDateTime The new date and time for the appointment
     * @throws InvalidAppointmentException if the slot is unavailable
     
    public void rescheduleAppointment(Patient patient, Appointment appointment, LocalDateTime newDateTime) throws InvalidAppointmentException {
        if (!appointments.contains(appointment)) {
            throw new InvalidAppointmentException("The appointment does not exist.");
        }
        if (isSlotUnavailable(appointment.getDoctor(), newDateTime)) {
            throw new InvalidAppointmentException("The new time slot is unavailable.");
        }
        appointment.setAppointmentTime(newDateTime);
        System.out.println("Appointment rescheduled successfully.");
    }

    /**
     * Cancels an existing appointment.
     * @param patient The patient requesting the cancellation
     * @param appointment The appointment to cancel
     
    public void cancelAppointment(Patient patient, Appointment appointment) {
        if (appointments.remove(appointment)) {
            System.out.println("Appointment canceled successfully.");
        } else {
            System.out.println("Appointment not found.");
        }
    }

    /**
     * Retrieves available slots for a specified doctor.
     * @param doctor The doctor to check for availability
     * @return A list of available date and time slots
     
    public List<LocalDateTime> viewAvailableSlots(Doctor doctor) {
        List<LocalDateTime> availableSlots = doctor.getAvailableSlots();
        List<LocalDateTime> bookedSlots = appointments.stream()
                .filter(appt -> appt.getDoctor().equals(doctor))
                .map(Appointment::getDateTime)
                .collect(Collectors.toList());
        
        // Return slots that are available (not booked)
        return availableSlots.stream()
                .filter(slot -> !bookedSlots.contains(slot))
                .collect(Collectors.toList());
    }

    private boolean isSlotUnavailable(Doctor doctor, LocalDateTime dateTime) {
        return appointments.stream()
                .anyMatch(appt -> appt.getDoctor().equals(doctor) && appt.getDateTime().equals(dateTime));
    }
}*/


