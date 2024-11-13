package SC2002_Assignment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import SC2002_Assignment.Appointment.AppointmentStatus;

import java.util.HashMap;

public class DoctorController {
    private DoctorModel doctorModel;
    private DoctorView doctorView;
    private AppointmentManager appointmentManager;

    public enum AppointmentStatus {
        PENDING,
        ACCEPTED,
        DECLINED,
        CANCELLED
    }

    public DoctorController(DoctorModel doctorModel, DoctorView doctorView, AppointmentManager appointmentManager) {
        this.doctorModel = doctorModel;
        this.doctorView = doctorView;
        this.appointmentManager = appointmentManager;
    }

    // Set availability
    public void setAvailability(String date, String startTime, String endTime) {
        DoctorAvailability availability = new DoctorAvailability(doctorModel.getDoctorID(), date, startTime, endTime);
        doctorModel.getAvailability().add(availability);
        System.out.println("Availability set for date: " + date + ", from " + startTime + " to " + endTime);
    }

    // Accept appointment
    public void acceptAppointment(Appointment appointment) {
        appointment.setStatus(Appointment.AppointmentStatus.ACCEPTED);
        if (!doctorModel.getAppointments().contains(appointment)) {
            doctorModel.getAppointments().add(appointment);
        }
        AppointmentsCsvHelper.saveAllAppointments(doctorModel.getAppointments());
        System.out.println("Appointment accepted: " + appointment);
    }
    
    public void declineAppointment(Appointment appointment) {
        appointment.setStatus(Appointment.AppointmentStatus.DECLINED);
        // Remove the appointment from the list if it's declined
        doctorModel.getAppointments().removeIf(app -> app.getAppointmentID().equals(appointment.getAppointmentID()));
        AppointmentsCsvHelper.saveAllAppointments(doctorModel.getAppointments());
        System.out.println("Appointment declined: " + appointment);
    }
    

    // Record outcome of an appointment
    public void recordAppointmentOutcome(Appointment appointment, AppointmentOutcomeRecord.ServiceType serviceType, List<Medication> meds, String notes) {
        AppointmentOutcomeRecord outcome = new AppointmentOutcomeRecord(
            appointment.getPatientID(),
            doctorModel.getDoctorID(),
            appointment.getAppointmentID(),
            new Date(),
            notes,
            serviceType
        );
        
        outcome.setMedications(new HashMap<>());
        for (Medication med : meds) {
            outcome.getMedications().put(med.getName(), med.getQuantity());
        }

        System.out.println("Appointment outcome recorded: " + outcome);
    }

    // Add diagnoses, prescriptions, and treatments
    public void addDiagnosis(MedicalRecord record, String diagnosis) {
        record.addNewDiagnosis(diagnosis);
    }

    public void addPrescription(MedicalRecord record, String prescription) {
        record.addNewPrescription(prescription);
    }

    public void addTreatmentPlans(MedicalRecord record, String treatment) {
        record.addNewTreatment(treatment);
    }
}