package model.event;

import java.time.LocalDate;
import model.people.Volunteer;

/**
 * Link class between Volunteer and Task (Many-to-Many). Stores the hours worked.
 */
public class VolunteerAssignment {
    private int assignmentId;
    private LocalDate assignedDate;
    private String completionStatus = "NEW";
    private double hoursWorked;
    private Volunteer volunteer;
    private Task task;

    public VolunteerAssignment(int assignmentId, Volunteer volunteer, Task task) {
        this.assignmentId = assignmentId;
        this.volunteer = volunteer;
        this.task = task;
    }

    public void assign() {
        assignedDate = LocalDate.now();
        completionStatus = "ASSIGNED";
    }

    public void completeAssignment(double hours) {
        updateHours(hours);
        completionStatus = "COMPLETED";
    }

    public void updateHours(double hours) {
        if (hours < 0) throw new IllegalArgumentException("hours cannot be negative");
        this.hoursWorked = hours;
    }

    public int getAssignmentId() { return assignmentId; }
    public LocalDate getAssignedDate() { return assignedDate; }
    public String getCompletionStatus() { return completionStatus; }
    public double getHoursWorked() { return hoursWorked; }
    public Volunteer getVolunteer() { return volunteer; }
    public Task getTask() { return task; }
}
