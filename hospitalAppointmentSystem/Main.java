package hospitalAppointmentSystem;

import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    private static HospitalSystem hospitalSystem = new HospitalSystem();
    private static Scanner scanner = new Scanner(System.in);
    
    private static int readInt() {
        try {
            int value = scanner.nextInt();
            scanner.nextLine();
            return value;
        } catch (Exception e) {
            System.out.println("Invalid input! Please enter a number.");
            scanner.nextLine();
            return -1;
        }
    }

    public static void main(String[] args) {
    System.out.println("**************************************************");
    System.out.println("*                                                *");
    System.out.println("*   Welcome to the Hospital Appointment System   *");
    System.out.println("*                                                *");
    System.out.println("**************************************************");

    boolean running = true;
    while (running) {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1. Add Doctor");
        System.out.println("2. Add Patient");
        System.out.println("3. Edit Doctor");
        System.out.println("4. Edit Patient");
        System.out.println("5. Generate Available Appointments");
        System.out.println("6. Book Appointment");
        System.out.println("7. Cancel Appointment");
        System.out.println("8. View Doctors");
        System.out.println("9. View Patients");
        System.out.println("10. View All Appointments");
        System.out.println("11. View Booked Appointments for Doctor");
        System.out.println("12. View Available Appointments for Doctor");
        System.out.println("13. Exit");
        System.out.print("Enter your choice: ");
        int choice;
        try {
            choice = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("Invalid input! Please enter a number.");
            continue;
        }

        switch (choice) {
  case 1: {

    System.out.print("Enter Doctor Name: ");
    String name = scanner.nextLine();

    // ===== Contact Info =====
    String contactInfo;
    while (true) {
        System.out.print("Enter Doctor Contact Info (11 digits): ");
        contactInfo = scanner.nextLine();

        if (!contactInfo.matches("\\d{11}")) {
            System.out.println("Invalid contact number (must be 11 digits).");
            continue;
        }

        if (hospitalSystem.isContactUsed(contactInfo)) {
            System.out.println("Contact number already exists. Re-enter.");
            continue;
        }
        break;
    }

    System.out.print("Enter Doctor Specialization: ");
    String specialization = scanner.nextLine();

    System.out.print("Enter Doctor Time Slot (e.g. 09:00-17:00): ");
    String timeSlot = scanner.nextLine();

    // ===== National ID (AFTER time slot زي ما انت عايز) =====
    String nationalId;
    while (true) {
        System.out.print("Enter Doctor National ID (7 digits): ");
        nationalId = scanner.nextLine();

        if (!nationalId.matches("\\d{7}")) {
            System.out.println("Invalid National ID (must be 7 digits). Re-enter.");
            continue;
        }

        if (hospitalSystem.isNationalIdUsed(nationalId)) {
            System.out.println("National ID already exists. Re-enter.");
            continue;
        }
        break;
    }

    Doctor doctor = new Doctor(name, contactInfo, nationalId, specialization, timeSlot);
    hospitalSystem.addDoctor(doctor);
    System.out.println("Doctor added successfully!");

    break;
}

            
            case 2: {

    System.out.print("Enter Patient Name: ");
    String name = scanner.nextLine();

    // ===== Contact Info =====
    String contactInfo;
    while (true) {
        System.out.print("Enter Patient Contact Info (11 digits): ");
        contactInfo = scanner.nextLine();

        if (!contactInfo.matches("\\d{11}")) {
            System.out.println("Contact must be exactly 11 digits.");
            continue;
        }

        if (hospitalSystem.isContactUsed(contactInfo)) {
            System.out.println("Contact already exists, please re-enter.");
            continue;
        }
        break;
    }

    // ===== National ID =====
    String nationalId;
    while (true) {
        System.out.print("Enter Patient National ID (7 digits): ");
        nationalId = scanner.nextLine();

        if (!nationalId.matches("\\d{7}")) {
            System.out.println("National ID must be exactly 7 digits.");
            continue;
        }

        if (hospitalSystem.isNationalIdUsed(nationalId)) {
            System.out.println("National ID already exists, please re-enter.");
            continue;
        }
        break;
    }

    // ===== Medical History ID =====
    int medicalHistoryId;
    while (true) {
        System.out.print("Enter Patient Medical History ID (5 digits): ");
        medicalHistoryId = readInt();

        if (medicalHistoryId < 10000 || medicalHistoryId > 99999) {
            System.out.println("Medical History ID must be exactly 5 digits.");
            continue;
        }

        if (hospitalSystem.isMedicalHistoryIdUsed(medicalHistoryId)) {
            System.out.println("Medical History ID already exists. Re-enter.");
            continue;
        }
        break;
    }

    Patient patient = new Patient(name, contactInfo, nationalId, medicalHistoryId);
    hospitalSystem.addPatient(patient);
    System.out.println("Patient added successfully!");

    break;
}

         case 3: {
    ArrayList<Doctor> doctors = hospitalSystem.getDoctors();
    if (doctors.size() == 0) {
        System.out.println("No doctors found!");
        break;
    }

    System.out.println("Available Doctors:");
    for (Doctor doctor : doctors) {
        System.out.println(doctor.getId() + ". " + doctor.getName() + " - " + doctor.getSpecialization());
    }

    System.out.print("Choose a doctor ID to edit: ");
    int doctorId = readInt();
    Doctor doctor = hospitalSystem.getDoctor(doctorId);

    if (doctor == null) {
        System.out.println("Doctor not found!");
        break;
    }

    // ===== Name =====
    System.out.print("Enter new Doctor Name (current: " + doctor.getName() + "): ");
    String name = scanner.nextLine();

    // ===== Contact (with immediate validation) =====
    String contactInfo;
    while (true) {
        System.out.print("Enter new Doctor Contact Info (current: " + doctor.getContactInfo() + "): ");
        contactInfo = scanner.nextLine();

        boolean ok = hospitalSystem.editDoctor(
                doctorId,
                name,
                contactInfo,
                doctor.getSpecialization(),
                doctor.getTimeSlot()
        );

        if (!ok) {
            System.out.println("Contact already exists. Please re-enter.");
            continue;
        }
        break;
    }

    // ===== Specialization =====
    System.out.print("Enter new Doctor Specialization (current: " + doctor.getSpecialization() + "): ");
    String specialization = scanner.nextLine();

    // ===== Time Slot =====
    System.out.print("Enter new Doctor Time Slot (current: " + doctor.getTimeSlot() + "): ");
    String timeSlot = scanner.nextLine();

    // ===== Final update =====
    hospitalSystem.editDoctor(
            doctorId,
            name,
            contactInfo,
            specialization,
            timeSlot
    );

    System.out.println("Doctor updated successfully!");
    break;
}

           case 4: {
    ArrayList<Patient> patients = hospitalSystem.getPatients();
    if (patients.size() == 0) {
        System.out.println("No patients found!");
        break;
    }

    System.out.println("Available Patients:");
    for (Patient p : patients) {
        System.out.println(p.getId() + ". " + p.getName());
    }

    System.out.print("Choose a patient ID to edit: ");
    int patientId = readInt();
    Patient patient = hospitalSystem.getPatient(patientId);

    if (patient == null) {
        System.out.println("Patient not found!");
        break;
    }

    // ===== Name =====
    System.out.print("Enter new Patient Name (current: " + patient.getName() + "): ");
    String name = scanner.nextLine();

    // ===== Contact (immediate validation) =====
    String contactInfo;
    while (true) {
        System.out.print("Enter new Patient Contact Info (current: " + patient.getContactInfo() + "): ");
        contactInfo = scanner.nextLine();

        boolean ok = hospitalSystem.editPatient(
                patientId,
                name,
                contactInfo,
                patient.getMedicalHistoryId()
        );

        if (!ok) {
            System.out.println("Contact already exists. Please re-enter.");
            continue;
        }
        break;
    }

    // ===== Medical History ID (immediate validation) =====
    int medicalHistoryId;
    while (true) {
        System.out.print("Enter new Medical History ID (current: " + patient.getMedicalHistoryId() + "): ");
        medicalHistoryId = readInt();

        boolean ok = hospitalSystem.editPatient(
                patientId,
                name,
                contactInfo,
                medicalHistoryId
        );

        if (!ok) {
            System.out.println("Invalid or duplicate Medical History ID. Please re-enter.");
            continue;
        }
        break;
    }

    System.out.println("Patient updated successfully!");
    break;
}

            case 5: {
                ArrayList<Doctor> doctors = hospitalSystem.getDoctors();
                if (doctors.size() == 0) {
                    System.out.println("No doctors found!");
                    break;
                }
                System.out.println("Available Doctors:");
                for (Doctor doctor : doctors) {
                    System.out.println(doctor.getId() + ". " + doctor.getName() + " - " + doctor.getSpecialization());
                }
                System.out.print("Choose a doctor ID: ");
                int doctorId = readInt();
                Doctor doctor = hospitalSystem.getDoctor(doctorId);
                if (doctor == null) {
                    System.out.println("Doctor not found!");
                    break;
                }
                System.out.print("Enter Appointments Date (e.g. 2025-01-15): ");
                String date = scanner.nextLine().trim();
                hospitalSystem.generateAvailableAppointments(doctor, date);
                System.out.println("Available appointments generated successfully!");
                break;
            }
           case 6: {
    ArrayList<Appointment> appointments = hospitalSystem.getAppointments();
    if (appointments.size() == 0) {
        System.out.println("No appointments found!");
        break;
    }

    System.out.println("Available Appointments:");
    for (Appointment apt : appointments) {
        if (!apt.getStatus()) {
            Doctor d = hospitalSystem.getDoctor(apt.getDoctorId());
            System.out.println(
                apt.getId() + ". Dr." + d.getName()
                + " | " + apt.getDate()
                + " | " + apt.getTime()
            );
        }
    }

    System.out.print("Enter Appointment ID to book: ");
    int appointmentId = readInt();

    ArrayList<Patient> patients = hospitalSystem.getPatients();
    if (patients.size() == 0) {
        System.out.println("No patients found!");
        break;
    }

    System.out.println("Available Patients:");
    for (Patient p : patients) {
        System.out.println(p.getId() + ". " + p.getName());
    }

    System.out.print("Enter Patient ID: ");
    int patientId = readInt();

  String result = hospitalSystem.bookAppointmentById(appointmentId, patientId);

switch (result) {
    case "SUCCESS":
        System.out.println("Appointment booked successfully!");
        break;

    case "PATIENT_BUSY":
        System.out.println("Failed to book appointment: patient already has an appointment at the same time.");
        break;

    case "ALREADY_BOOKED":
        System.out.println("Failed to book appointment: this slot is already booked.");
        break;

    default:
        System.out.println("Failed to book appointment: invalid data.");
}
break;

}
case 7: {
    ArrayList<Appointment> appointments = hospitalSystem.getAppointments();
    if (appointments.size() == 0) {
        System.out.println("No appointments found!");
        break;
    }

    System.out.println("Booked Appointments:");
    for (Appointment apt : appointments) {
        if (apt.getStatus()) {
            Doctor d = hospitalSystem.getDoctor(apt.getDoctorId());
            System.out.println(
                apt.getId() + ". Dr." + d.getName()
                + " | " + apt.getDate()
                + " | " + apt.getTime()
            );
        }
    }

    System.out.print("Enter Appointment ID to cancel: ");
    int appointmentId = readInt();

    if (hospitalSystem.cancelAppointmentById(appointmentId)) {
        System.out.println("Appointment cancelled successfully!");
    } else {
        System.out.println("Failed to cancel appointment.");
    }
    break;
}

           
            case 8: {
                ArrayList<Doctor> doctors = hospitalSystem.getDoctors();
                if (doctors.size() == 0) {
                    System.out.println("No doctors found!");
                } else {
                    System.out.println("=== Doctors List ===");
                    for (Doctor doctor : doctors) {
                        System.out.println(doctor.getId() + ". " + doctor.getName() + " - " + doctor.getSpecialization());
                    }
                }
                break;
            }
            case 9: {
                ArrayList<Patient> patients = hospitalSystem.getPatients();
                if (patients.size() == 0) {
                    System.out.println("No patients found!");
                } else {
                    System.out.println("=== Patients List ===");
                    for (Patient patient : patients) {
                        System.out.println(patient.getId() + ". " + patient.getName());
                    }
                }
                break;
            }
            case 10: {
                ArrayList<Appointment> appointments = hospitalSystem.getAppointments();
                if (appointments.size() == 0) {
                    System.out.println("No appointments found!");
                } else {
                    System.out.println("=== Appointments List ===");
                    for (Appointment appointment : appointments) {
                        Doctor doctor = hospitalSystem.getDoctor(appointment.getDoctorId());
                        String doctorName = (doctor != null) ? doctor.getName() : "Unknown";
                        String status = appointment.getStatus() ? "Booked" : "Available";
                        System.out.println(appointment.getId() + ". Dr." + doctorName + " on " + appointment.getDate() + " at " + appointment.getTime() + " - " + status);
                    }
                }
                break;
            }
            case 11: {
                ArrayList<Doctor> doctors = hospitalSystem.getDoctors();
                if (doctors.size() == 0) {
                    System.out.println("No doctors found!");
                    break;
                }
                System.out.println("Available Doctors:");
                for (Doctor doctor : doctors) {
                    System.out.println(doctor.getId() + ". " + doctor.getName() + " - " + doctor.getSpecialization());
                }
                System.out.print("Choose a doctor ID: ");
                int doctorId = readInt();
                Doctor doctor = hospitalSystem.getDoctor(doctorId);
                if (doctor == null) {
                    System.out.println("Doctor not found!");
                    break;
                }
                ArrayList<Appointment> bookedAppointments = hospitalSystem.getBookedAppointmentsByDoctor(doctor);
                if (bookedAppointments.size() == 0) {
                    System.out.println("No booked appointments for Dr." + doctor.getName());
                } else {
                    System.out.println("=== Booked Appointments for Dr." + doctor.getName() + " ===");
                    for (Appointment appointment : bookedAppointments) {
                        Patient patient = hospitalSystem.getPatient(appointment.getPatientId());
                        String patientName = (patient != null) ? patient.getName() : "Unknown";
                        System.out.println("Date: " + appointment.getDate() + " | Time: " + appointment.getTime() + " | Patient: " + patientName);
                    }
                }
                break;
            }
            case 12: {
                ArrayList<Doctor> doctors = hospitalSystem.getDoctors();
                if (doctors.size() == 0) {
                    System.out.println("No doctors found!");
                    break;
                }
                System.out.println("Available Doctors:");
                for (Doctor doctor : doctors) {
                    System.out.println(doctor.getId() + ". " + doctor.getName() + " - " + doctor.getSpecialization());
                }
                System.out.print("Choose a doctor ID: ");
                int doctorId = readInt();
                Doctor doctor = hospitalSystem.getDoctor(doctorId);
                if (doctor == null) {
                    System.out.println("Doctor not found!");
                    break;
                }
                ArrayList<Appointment> availableAppointments = hospitalSystem.getAvailableAppointmentsByDoctor(doctor);
                if (availableAppointments.size() == 0) {
                    System.out.println("No available appointments for Dr." + doctor.getName());
                } else {
                    System.out.println("=== Available Appointments for Dr." + doctor.getName() + " ===");
                    for (Appointment appointment : availableAppointments) {
                        System.out.println("Date: " + appointment.getDate() + " | Time: " + appointment.getTime());
                    }
                }
                break;
            }
            case 13: {
                System.out.println("Thank you for using the Hospital Appointment System!");
                running = false;
                break;
            }
            default: {
                System.out.println("Invalid choice! Please try again.");
            }
        }
    }
    scanner.close();
   }
}

