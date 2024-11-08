package SC2002_Assignment;
import java.util.Date;
import java.util.List;

public class MedicalRecord
{
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

    public MedicalRecord(String patientID, String name, Date dob, String gender, String contactNumber, String bloodType, String email, List<String> pastDiagnoses, List<String> pastTreatments, List<String> currentDiagnoses, List<String> currentTreatments, List<String> prescriptions)
    {
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



    protected void addNewPrescription(String prescription)
    {
        this.prescriptions.add(prescription);
    }

    protected void addNewDiagnosis(String diagnosis)
    {
        this.currentDiagnoses.add(diagnosis);
    }

    public List<String> getDiagnosis()
    {
        return currentDiagnoses;
    }

    public List<String> getPrescription()
    {
        return prescriptions;
    }

    public List<String> getTreatment()
    {
        return currentDiagnoses;
    }

    public void viewMedicalRecord()
    {
        System.out.println("Patient ID: " + patientID);
        System.out.println("Name: " + name);
        System.out.println("DOB: " + dob);
        System.out.println("Gender: " + gender);
        System.out.println("Contact Number: " + contactNumber);
        System.out.println("Blood Type: " + bloodType);
        System.out.println("Email: " + email);
        System.out.println("Current Diagnoses: " + currentDiagnoses);
        System.out.println("Current Treatments: " + currentTreatments);
        System.out.println("Prescriptions: " + prescriptions);

        System.out.println("Past Diagnoses: " + pastDiagnoses);
        for (int i = 0; i < pastDiagnoses.size(); i++)
        {
            System.out.println(pastDiagnoses.get(i));
        }

        System.out.println("Past Treatments: " + pastTreatments);
        for (int i = 0; i< pastTreatments.size(); i++)
        {
            System.out.println(pastTreatments.get(i));
        }
    }


}