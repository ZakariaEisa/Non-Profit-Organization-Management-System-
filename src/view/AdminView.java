package view;

import controller.CommunicationController;
import controller.EventController;
import controller.LookupController;
import model.lookup.LookupEntity;

/**
 * MVC View: admin page (console version in Phase 1, GUI/web in Phase 2).
 */
public class AdminView {
    private final LookupController lookupController;
    private final EventController eventController;
    private final CommunicationController communicationController;

    public AdminView(LookupController lookupController, EventController eventController,
                     CommunicationController communicationController) {
        this.lookupController = lookupController;
        this.eventController = eventController;
        this.communicationController = communicationController;
    }

    public void showDashboard() {
        System.out.println("=== ADMIN DASHBOARD ===");
        System.out.println("Manage: donations | volunteers | events | beneficiaries | lookup tables");
    }

    /** CR-07: shows the current rows of one lookup table. */
    public void manageLookups(String tableName) {
        System.out.println("  Lookup table '" + tableName + "':");
        for (LookupEntity item : lookupController.list(tableName)) {
            System.out.println("    - " + item);
        }
    }
}
