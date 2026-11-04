package model.event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.lookup.Skill;
import model.people.Volunteer;

/**
 * A job inside an event that volunteers can be assigned to.
 */
public class Task {
    private int taskId;
    private String taskName;
    private String description;
    private LocalDate deadline;
    private String status = "OPEN";
    /** CR-06: skills a volunteer must have to take this task. */
    private final List<Skill> requiredSkills = new ArrayList<>();
    private final List<VolunteerAssignment> assignments = new ArrayList<>();

    public Task(int taskId, String taskName, String description, LocalDate deadline) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.description = description;
        this.deadline = deadline;
    }

    public void addRequiredSkill(Skill skill) {
        if (!requiredSkills.contains(skill)) requiredSkills.add(skill);
    }

    /**
     * Assigns a volunteer if they are qualified (CR-06).
     * @return the new assignment, or null if the volunteer lacks a required skill
     */
    public VolunteerAssignment assignVolunteer(Volunteer volunteer, int assignmentId) {
        if (!volunteer.isQualifiedFor(this)) {
            return null;
        }
        VolunteerAssignment assignment = new VolunteerAssignment(assignmentId, volunteer, this);
        assignment.assign();
        assignments.add(assignment);
        volunteer.addAssignment(assignment);
        status = "ASSIGNED";
        return assignment;
    }

    public void updateTaskStatus(String newStatus) {
        this.status = newStatus;
    }

    public int getTaskId() { return taskId; }
    public String getTaskName() { return taskName; }
    public String getDescription() { return description; }
    public LocalDate getDeadline() { return deadline; }
    public String getStatus() { return status; }
    public List<Skill> getRequiredSkills() { return Collections.unmodifiableList(requiredSkills); }
    public List<VolunteerAssignment> getAssignments() { return Collections.unmodifiableList(assignments); }
}
