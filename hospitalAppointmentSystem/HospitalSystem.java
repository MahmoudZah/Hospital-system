package hospitalAppointmentSystem;

import java.util.ArrayList;

public class HospitalSystem {
    private ArrayList<Doctor> doctors;
    private ArrayList<Patient> patients;
    private ArrayList<Appointment> appointments;
    private ArrayList<String> usedNationalIds = new ArrayList<>();
    private ArrayList<String> usedContacts = new ArrayList<>();
    private ArrayList<Integer> usedMedicalHistoryIds = new ArrayList<>();

    public HospitalSystem() {
        this.doctors = new ArrayList<>();
        this.patients = new ArrayList<>();
        this.appointments = new ArrayList<>();
    }

public boolean addDoctor(Doctor doctor) {

    if (usedNationalIds.contains(doctor.getNationalId())) {
        return false; // national id duplicate
    }

    if (usedContacts.contains(doctor.getContactInfo())) {
        throw new IllegalArgumentException("Contact number already exists");
    }

    usedNationalIds.add(doctor.getNationalId());
    usedContacts.add(doctor.getContactInfo());
    doctors.add(doctor);
    return true;
}




public boolean addPatient(Patient patient) {

    if (usedNationalIds.contains(patient.getNationalId()) ||
        usedContacts.contains(patient.getContactInfo()) ||
        usedMedicalHistoryIds.contains(patient.getMedicalHistoryId())) {
        return false;
    }

    usedNationalIds.add(patient.getNationalId());
    usedContacts.add(patient.getContactInfo());
    usedMedicalHistoryIds.add(patient.getMedicalHistoryId());
    patients.add(patient);
    return true;
}




    public void addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
    }

    public void removeDoctor(Doctor doctor) {
        this.doctors.remove(doctor);
    }

    public void removePatient(Patient patient) {
        this.patients.remove(patient);
    }

    public void removeAppointment(Appointment appointment) {
        this.appointments.remove(appointment);
    }

    public Doctor getDoctor(int id) {
        for (Doctor doctor : doctors) {
            if (doctor.getId() == id) {
                return doctor;
            }
        }
        return null;
    }

    public Patient getPatient(int id) {
        for (Patient patient : patients) {
            if (patient.getId() == id) {
                return patient;
            }
        }
        return null;
    }

    public Appointment getAppointment(int id) {
        for (Appointment appointment : appointments) {
            if (appointment.getId() == id) {
                return appointment;
            }
        }
        return null;
    }

public boolean editDoctor(int id, String name, String contactInfo,
                          String specialization, String timeSlot) {

    Doctor doctor = getDoctor(id);
    if (doctor == null) {
        return false;
    }

    // check contact only if changed
    if (!doctor.getContactInfo().equals(contactInfo)) {
        if (isContactUsed(contactInfo)) {
            return false; 
        }
        usedContacts.remove(doctor.getContactInfo());
        usedContacts.add(contactInfo);
        doctor.setContactInfo(contactInfo);
    }

    doctor.setName(name);
    doctor.setSpecialization(specialization);
    doctor.setTimeSlot(timeSlot);

    return true; 
}


 public boolean editPatient(int id, String name, String contactInfo,
                           int medicalHistoryId) {

    Patient patient = getPatient(id);
    if (patient == null) {
        return false;
    }

    // ===== Contact validation =====
    if (!patient.getContactInfo().equals(contactInfo)) {
        if (isContactUsed(contactInfo)) {
            return false;
        }
        usedContacts.remove(patient.getContactInfo());
        usedContacts.add(contactInfo);
        patient.setContactInfo(contactInfo);
    }

    // ===== Medical History ID validation =====
    if (patient.getMedicalHistoryId() != medicalHistoryId) {

        if (medicalHistoryId < 10000 || medicalHistoryId > 99999) {
            return false;
        }

        if (usedMedicalHistoryIds.contains(medicalHistoryId)) {
            return false;
        }

       usedMedicalHistoryIds.remove(Integer.valueOf(patient.getMedicalHistoryId()));
        usedMedicalHistoryIds.add(medicalHistoryId);
        patient.setMedicalHistoryId(medicalHistoryId);
    }

    patient.setName(name);
    return true;
}


    public Appointment editAppointment(int id, String date, Boolean status) {
        Appointment appointment = getAppointment(id);
        if (appointment != null) {
            appointment.setDate(date);
            appointment.setStatus(status);
            return appointment;
        }
        return null;
    }

    public Appointment addAvailableSlot(int doctorId, String date, String time) {
        Doctor doctor = getDoctor(doctorId);
        if (doctor == null) {
            return null;
        }

        for (Appointment appointment : this.appointments) {
            if (appointment.getDoctorId() == doctorId
                && appointment.getDate().equals(date)
                && appointment.getTime().equals(time)) {
                return null;
            }
        }

        Appointment appointment = new Appointment(doctorId, null, date, time);
        this.appointments.add(appointment);
        return appointment;
    }

    public boolean bookAppointment(int doctorId, String date, String time, int patientId) {
        Doctor doctor = getDoctor(doctorId);
        Patient patient = getPatient(patientId);
        if (doctor == null || patient == null) {
            return false;
        }
        for (Appointment appointment : this.appointments) {
            if (appointment.getDoctorId() == doctorId
                && appointment.getDate().equals(date)
                && appointment.getTime().equals(time)
                && Boolean.FALSE.equals(appointment.getStatus())) {
                appointment.setPatientId(patientId);
                return true;
            }
        }
        return false;
    }

    public boolean cancelAppointment(int doctorId, String date, String time) {
        Doctor doctor = getDoctor(doctorId);
        if (doctor == null) {
            return false;
        }
        for (Appointment appointment : this.appointments) {
            if (appointment.getDoctorId() == doctorId
                && appointment.getDate().equals(date)
                && appointment.getTime().equals(time)
                && Boolean.TRUE.equals(appointment.getStatus())) {
                appointment.setPatientId(null);
                appointment.setStatus(false);
                return true;
            }
        }
        return false;
    }

    public ArrayList<Appointment> getAppointmentsByDoctor(Doctor doctor) {
        ArrayList<Appointment> result = new ArrayList<>();
        for (Appointment appointment : this.appointments) {
            if (appointment.getDoctorId() == doctor.getId()) {
                result.add(appointment);
            }
        }
        return result;
    }

    public ArrayList<Appointment> getBookedAppointmentsByDoctor(Doctor doctor) {
        ArrayList<Appointment> result = new ArrayList<>();
        for (Appointment appointment : this.appointments) {
            if (appointment.getDoctorId() == doctor.getId()
                && Boolean.TRUE.equals(appointment.getStatus())) {
                result.add(appointment);
            }
        }
        return result;
    }

    public ArrayList<Appointment> getAvailableAppointmentsByDoctor(Doctor doctor) {
        ArrayList<Appointment> result = new ArrayList<>();
        for (Appointment appointment : this.appointments) {
            if (appointment.getDoctorId() == doctor.getId()
                && Boolean.FALSE.equals(appointment.getStatus())) {
                result.add(appointment);
            }
        }
        return result;
    }

    public void generateAvailableAppointments(Doctor doctor, String date) {
        for (Appointment apt : appointments) {
            if (apt.getDoctorId() == doctor.getId()
                && apt.getDate().equals(date)) {
                return;
            }
        }

        String[] times = doctor.getTimeSlot().split("-");
        if (times.length < 2) {
            return;
        }
        String startTime = times[0];
        String endTime   = times[1];

        int startHour = Integer.parseInt(startTime.split(":")[0]);
        int startMin  = Integer.parseInt(startTime.split(":")[1]);
        int endHour   = Integer.parseInt(endTime.split(":")[0]);
        int endMin    = Integer.parseInt(endTime.split(":")[1]);

        int currentHour = startHour;
        int currentMin  = startMin;

        while (currentHour < endHour ||
              (currentHour == endHour && currentMin < endMin)) {

            String timeSlot = String.format("%02d:%02d", currentHour, currentMin);

            Appointment appointment = new Appointment(doctor.getId(), null, date, timeSlot);
            this.appointments.add(appointment);

            currentMin += 30;
            if (currentMin >= 60) {
                currentMin = 0;
                currentHour++;
            }
        }
    }

    public ArrayList<Doctor> getDoctors() {
        return doctors;
    }

    public ArrayList<Patient> getPatients() {
        return patients;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }
public boolean isNationalIdUsed(String nationalId) {
    for (Doctor d : doctors)
        if (d.getNationalId().equals(nationalId))
            return true;

    for (Patient p : patients)
        if (p.getNationalId().equals(nationalId))
            return true;

    return false;
}

public boolean isContactUsed(String contact) {
    for (Doctor d : doctors)
        if (d.getContactInfo().equals(contact))
            return true;

    for (Patient p : patients)
        if (p.getContactInfo().equals(contact))
            return true;

    return false;
}
public boolean isMedicalHistoryIdUsed(int id) {
    return usedMedicalHistoryIds.contains(id);
}
public String bookAppointmentById(int appointmentId, int patientId) {

    Appointment appointment = getAppointment(appointmentId);
    Patient patient = getPatient(patientId);

    if (appointment == null || patient == null) {
        return "INVALID_DATA";
    }

    if (appointment.getStatus()) {
        return "ALREADY_BOOKED";
    }

    // prevent double booking (same patient, same date & time)
    for (Appointment apt : appointments) {
        if (apt.getPatientId() != null
            && apt.getPatientId() == patientId
            && apt.getDate().trim().equals(appointment.getDate().trim())
            && apt.getTime().trim().equals(appointment.getTime().trim())) {

            return "PATIENT_BUSY";
        }
    }

    appointment.setPatientId(patientId);
    return "SUCCESS";
}

public boolean cancelAppointmentById(int appointmentId) {

    Appointment appointment = getAppointment(appointmentId);

    if (appointment == null || !appointment.getStatus()) {
        return false;
    }

    appointment.setPatientId(null);
    return true;
}




}
