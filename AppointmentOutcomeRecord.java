package SC2002_Assignment;

import java.util.List;

public class AppointmentOutcomeRecord {
    private String serviceType;
    private List<Medication> medicines;
    private String notes;

    public AppointmentOutcomeRecord(String serviceType, List<Medication> medicines, String notes) {
        this.serviceType = serviceType;
        this.medicines = medicines;
        this.notes = notes;
    }

    public String getServiceType() {
        return serviceType;
    }

    public List<Medication> getMedicines() {
        return medicines;
    }

    public String getNotes() {
        return notes;
    }

    @Override
    public String toString() {
        return "Service: " + serviceType + ", Medicines: " + medicines + ", Notes: " + notes;
    }
}
