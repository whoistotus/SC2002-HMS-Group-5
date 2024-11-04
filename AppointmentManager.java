package assignment;


import java.time.LocalDateTime;
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
     */
    public void scheduleAppointment(Patient patient, Doctor doctor, LocalDateTime dateTime) throws InvalidAppointmentException {
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
     */
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
     */
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
     */
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
}

