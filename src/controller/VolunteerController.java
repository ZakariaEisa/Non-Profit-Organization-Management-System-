package controller;

import java.util.ArrayList;
import java.util.List;
import model.certificate.VolunteerCertificate;
import model.event.Event;
import model.event.Task;
import model.event.VolunteerAssignment;
import model.people.Volunteer;

public class VolunteerController extends BaseController {
    private final List<Volunteer> volunteers = new ArrayList<>();
    private int nextAssignmentId = 1;
    private int nextCertificateId = 1;

    public void registerVolunteer(Volunteer volunteer) {
        volunteers.add(volunteer);
    }

    /** @return the assignment, or null when the volunteer lacks a required skill (CR-06) */
    public VolunteerAssignment assignToTask(Volunteer volunteer, Task task) {
        return task.assignVolunteer(volunteer, nextAssignmentId++);
    }

    /** CR-06: qualified volunteers for a task, those who prefer this event type first. */
    public List<Volunteer> findSuitableVolunteers(Task task, Event event) {
        List<Volunteer> preferred = new ArrayList<>();
        List<Volunteer> others = new ArrayList<>();
        for (Volunteer v : volunteers) {
            if (!v.isQualifiedFor(task)) continue;
            if (v.prefers(event.getEventType())) preferred.add(v); else others.add(v);
        }
        preferred.addAll(others);
        return preferred;
    }

    public double trackHours(Volunteer volunteer) {
        return volunteer.trackVolunteerHours();
    }

    public VolunteerCertificate issueCertificate(Volunteer volunteer) {
        return volunteer.generateCertificate(nextCertificateId++);
    }
}
