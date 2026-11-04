package model.people;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.certificate.VolunteerCertificate;
import model.event.Task;
import model.event.VolunteerAssignment;
import model.lookup.EventType;
import model.lookup.Skill;

public class Volunteer extends Person {
    private String availability;
    private double totalHours;
    private final List<VolunteerAssignment> assignments = new ArrayList<>();
    private final List<Skill> skills = new ArrayList<>();
    /** CR-06: event types this volunteer prefers to work at. */
    private final List<EventType> preferredEventTypes = new ArrayList<>();
    private final List<VolunteerCertificate> certificates = new ArrayList<>();

    public Volunteer(int personId, String fullName, String email, String phoneNumber, String address, String availability) {
        super(personId, fullName, email, phoneNumber, address);
        this.availability = availability;
    }

    public void updateAvailability(String availability) {
        this.availability = availability;
    }

    public void addSkill(Skill skill) {
        if (!skills.contains(skill)) skills.add(skill);
    }

    public void addPreferredEventType(EventType type) {
        if (!preferredEventTypes.contains(type)) preferredEventTypes.add(type);
    }

    public void addAssignment(VolunteerAssignment assignment) {
        assignments.add(assignment);
    }

    /** CR-06: a volunteer qualifies for a task only if they have every required skill. */
    public boolean isQualifiedFor(Task task) {
        return skills.containsAll(task.getRequiredSkills());
    }

    public boolean prefers(EventType type) {
        return preferredEventTypes.contains(type);
    }

    public List<Task> viewAssignedTasks() {
        List<Task> tasks = new ArrayList<>();
        for (VolunteerAssignment a : assignments) tasks.add(a.getTask());
        return tasks;
    }

    public double trackVolunteerHours() {
        double sum = 0;
        for (VolunteerAssignment a : assignments) sum += a.getHoursWorked();
        totalHours = sum;
        return totalHours;
    }

    public VolunteerCertificate generateCertificate(int certificateId) {
        VolunteerCertificate certificate =
                new VolunteerCertificate(certificateId, this, LocalDate.now(), trackVolunteerHours());
        certificates.add(certificate);
        return certificate;
    }

    public String getAvailability() { return availability; }
    public double getTotalHours() { return totalHours; }
    public List<Skill> getSkills() { return Collections.unmodifiableList(skills); }
    public List<EventType> getPreferredEventTypes() { return Collections.unmodifiableList(preferredEventTypes); }
    public List<VolunteerAssignment> getAssignments() { return Collections.unmodifiableList(assignments); }
    public List<VolunteerCertificate> getCertificates() { return Collections.unmodifiableList(certificates); }
}
