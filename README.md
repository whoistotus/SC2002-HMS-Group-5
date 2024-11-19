# Hospital Management System (HMS)
## NTU SC/CE/CZ2002 Object-Oriented Design & Programming Assignment
## Group 5

A comprehensive hospital management system designed to automate and streamline hospital operations including patient management, appointment scheduling, staff management, and billing.

## Contributors
- Aniston Tan Wen Min
- Kathlyn Chin Wei Ting  
- Li Jiayi Jamie
- Tan Yi Jun
- Titus Lee Seng Keat

## Features

### User Authentication
- Secure login system with role-based access control
- Supports multiple user roles: Patient, Doctor, Pharmacist, Administrator
- Password change functionality after initial login

### Patient Portal
- View personal medical records
- Schedule/reschedule/cancel appointments
- View appointment history and outcomes
- Update personal contact information

### Doctor Interface
- Manage patient medical records
- Set availability for appointments
- Accept/decline appointment requests
- Record appointment outcomes and prescriptions
- View personal schedule

### Pharmacist Module
- View and fulfill prescriptions
- Monitor medication inventory
- Submit replenishment requests
- Update prescription status

### Administrator Dashboard
- Manage hospital staff (add/remove/update)
- View real-time appointment updates
- Manage medication inventory
- Process replenishment requests

## Technical Requirements

### Prerequisites
- Java Development Kit (JDK) version 8 or higher
- Any Java IDE (Eclipse, IntelliJ, etc.) or text editor
- Git (for version control)

### System Requirements
- Operating System: Windows/Mac/Linux
- Minimum RAM: 2GB
- Storage: 500MB free space

## Installation and Setup

1. Clone the repository:
```bash
git clone https://github.com/whoistotus/SC2002-HMS-Group-5
cd SC2002_Assignment
```

2. Compile the Java files:
```bash
javac -d bin src/**/*.java
```

3. Run the application:
```bash
java -cp bin Main
```

## Project Structure
```
[controller]
    ├── AdminController.java
    ├── AppointmentManager.java
    ├── DoctorController.java
    ├── InventoryController.java
    ├── LoginManager.java
    ├── PatientController.java
    ├── PharmacistController.java
    └── PharmacistReplenishRequestManager.java
[data]
    ├── AppointmentOutcomeRecords.csv
    ├── Appointments.csv
    ├── DoctorAvailability.csv
    ├── MedicalRecords.csv
    ├── MedicationList.csv
    ├── passwords.csv
    ├── PatientList.csv
    ├── ReplenishmentRequests.csv
    └── StaffList.csv
LICENSE
LoginResult.java
MainApp.java
[model]
    ├── AdminModel.java
    ├── Appointment.java
    ├── AppointmentOutcomeRecord.java
    ├── DoctorAvailability.java
    ├── DoctorModel.java
    ├── HospitalStaff.java
    ├── MedicalRecord.java
    ├── Medication.java
    ├── PatientModel.java
    ├── PharmacistPrescribeManager.java
    ├── ReplenishmentRequest.java
    ├── TimeSlot.java
    └── User.java
Password.java
README.md
[utils]
    ├── AdminCSVReader.java
    ├── AdminCSVWriter.java
    ├── AppointmentOutcomeRecordsCsvHelper.java
    ├── AppointmentsCsvHelper.java
    ├── DoctorAvailabilityCsvHelper.java
    ├── MedicalRecordsCsvHelper.java
    ├── MedicationCSVReader.java
    ├── PatientListCsvHelper.java
    └── ReplenishmentRequestCsvHelper.java
[view]
    ├── AdminView.java
    ├── DoctorView.java
    ├── InventoryView.java
    ├── IPrescriptionManager.java
    ├── IReplenishmentManager.java
    ├── PatientView.java
    ├── PharmacistView.java
    └── UserInterface.java
```

## Data Storage

The system uses CSV files for data persistence:
- `Appointments.csv`: Stores appointment information
- `MedicalRecords.csv`: Contains patient medical records
- `Medications.csv`: Tracks medication inventory
- `PatientList.csv`: Maintains patient information
- `StaffList.csv`: Stores staff details

## Testing

To run the test cases:
1. Navigate to the test directory
2. Run the test suite:
```bash
java -cp .:junit.jar org.junit.runner.JUnitCore TestSuite
```

## Documentation

Detailed documentation including:
- JavaDoc comments throughout the codebase
- UML diagrams in the `docs` directory
- User manual in `docs/manual.pdf`

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is part of the SC/CE/CZ2002 Object-Oriented Design & Programming course at Nanyang Technological University.

## Acknowledgments

- Course Instructors and TAs for guidance
- NTU School of Computer Science & Engineering
