package model.certificate;

import java.time.LocalDate;
import model.people.Volunteer;

/**
 * Certificate of appreciation generated from a volunteer's tracked hours.
 */
public class VolunteerCertificate {
    private int certificateId;
    private Volunteer volunteer;
    private LocalDate issueDate;
    private double totalHours;

    public VolunteerCertificate(int certificateId, Volunteer volunteer, LocalDate issueDate, double totalHours) {
        this.certificateId = certificateId;
        this.volunteer = volunteer;
        this.issueDate = issueDate;
        this.totalHours = totalHours;
    }

    public String generateCertificate() {
        return "=== CERTIFICATE OF APPRECIATION #" + certificateId + " ===\n"
                + "Awarded to " + volunteer.getFullName() + " for " + totalHours
                + " hours of volunteer service.\nIssued on " + issueDate;
    }

    public int getCertificateId() { return certificateId; }
    public Volunteer getVolunteer() { return volunteer; }
    public LocalDate getIssueDate() { return issueDate; }
    public double getTotalHours() { return totalHours; }
}
