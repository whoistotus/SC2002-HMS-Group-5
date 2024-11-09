package SC2002_Assignment;
import java.util.Date;
import java.util.List;

public class MedicalRecord {
    private String patientID;
    private String name;
    private Date dob; 
    private String gender;
    private String contactNumber;
    private String bloodType;
    private String email;
    private List<String> pastDiagnoses;
    private List<String> pastTreatments;
    private List<String> currentDiagnoses;
    private List<String> currentTreatments;
    private List<String> prescriptions;

    public MedicalRecord(String patientID, String name, Date dob, String gender, 
                        String contactNumber, String bloodType, String email, 
                        List<String> pastDiagnoses, List<String> pastTreatments, 
                        List<String> currentDiagnoses, List<String> currentTreatments, 
                        List<String> prescriptions) {
        this.patientID = patientID;
        this.name = name;
        this.dob = dob;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.bloodType = bloodType;
        this.email = email;
        this.pastDiagnoses = pastDiagnoses;
        this.pastTreatments = pastTreatments;
        this.currentDiagnoses = currentDiagnoses;
        this.currentTreatments = currentTreatments;
        this.prescriptions = prescriptions;
    }

    // Getters
    public String getPatientID() {
        return patientID;
    }

    public String getName() {
        return name;
    }

    public Date getDOB() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getPastDiagnoses() {
        return pastDiagnoses;
    }

    public List<String> getPastTreatments() {
        return pastTreatments;
    }

    public List<String> getCurrentDiagnoses() {
        return currentDiagnoses;
    }

    public List<String> getCurrentTreatments() {
        return currentTreatments;
    }

    public List<String> getPrescriptions() {
        return prescriptions;
    }

    // Setters for non-medical information
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Protected methods for medical updates (as per UML)
    protected void addNewPrescription(String prescription) {
        this.prescriptions.add(prescription);
    }

    protected void addNewDiagnosis(String diagnosis) {
        this.currentDiagnoses.add(diagnosis);
    }

    protected void addNewTreatment(String treatment) {
        this.currentTreatments.add(treatment);
    }

    // Method to move current diagnosis to past diagnosis
    protected void moveToPastDiagnosis(String diagnosis) {
        if (currentDiagnoses.remove(diagnosis)) {
            pastDiagnoses.add(diagnosis);
        }
    }

    // Method to move current treatment to past treatment
    protected void moveToPastTreatment(String treatment) {
        if (currentTreatments.remove(treatment)) {
            pastTreatments.add(treatment);
        }
    }

    public void viewMedicalRecord() {
        System.out.println("Medical Record for Patient: " + patientID);
        System.out.println("==============================");
        System.out.println("Personal Information:");
        System.out.println("Name: " + name);
        System.out.println("Date of Birth: " + dob);
        System.out.println("Gender: " + gender);
        System.out.println("Contact Number: " + contactNumber);
        System.out.println("Blood Type: " + bloodType);
        System.out.println("Email: " + email);
        
        System.out.println("\nCurrent Medical Status:");
        System.out.println("Current Diagnoses: ");
        for (String diagnosis : currentDiagnoses) {
            System.out.println("- " + diagnosis);
        }
        
        System.out.println("\nCurrent Treatments: ");
        for (String treatment : currentTreatments) {
            System.out.println("- " + treatment);
        }
        
        System.out.println("\nCurrent Prescriptions: ");
        for (String prescription : prescriptions) {
            System.out.println("- " + prescription);
        }

        System.out.println("\nMedical History:");
        System.out.println("Past Diagnoses: ");
        for (String diagnosis : pastDiagnoses) {
            System.out.println("- " + diagnosis);
        }

        System.out.println("\nPast Treatments: ");
        for (String treatment : pastTreatments) {
            System.out.println("- " + treatment);
        }
    }
}