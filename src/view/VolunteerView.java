package view;

import controller.VolunteerController;
import model.event.Task;
import model.people.Volunteer;

/**
 * MVC View: volunteer pages.
 */
public class VolunteerView {
    private final VolunteerController volunteerController;

    public VolunteerView(VolunteerController volunteerController) {
        this.volunteerController = volunteerController;
    }

    public void showTasks(Volunteer volunteer) {
        System.out.println("  Tasks of " + volunteer.getFullName() + ":");
        for (Task t : volunteer.viewAssignedTasks()) {
            System.out.println("    - " + t.getTaskName() + " (" + t.getStatus() + ")");
        }
    }

    public void showHours(Volunteer volunteer) {
        System.out.println("  " + volunteer.getFullName() + " worked " + volunteerController.trackHours(volunteer) + " hours");
    }
}
