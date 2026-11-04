package model.beneficiary;

import java.time.LocalDate;
import model.lookup.ResourceType;
import model.people.Beneficiary;

/**
 * One delivery of resources to a beneficiary, optionally as part of a Program.
 */
public class ResourceDistribution {
    private int distributionId;
    private LocalDate distributionDate;
    private ResourceType resourceType;
    private int quantity;
    private Program program;

    public ResourceDistribution(int distributionId, ResourceType resourceType, int quantity, Program program) {
        this.distributionId = distributionId;
        this.resourceType = resourceType;
        this.quantity = quantity;
        this.program = program;
    }

    public void distributeResource(Beneficiary beneficiary) {
        distributionDate = LocalDate.now();
        beneficiary.receive(this);
    }

    public String getDistributionDetails() {
        return "Distribution #" + distributionId + ": " + quantity + " x " + resourceType.getName()
                + " on " + distributionDate + (program == null ? "" : " (program: " + program.getName() + ")");
    }

    public int getDistributionId() { return distributionId; }
    public LocalDate getDistributionDate() { return distributionDate; }
    public ResourceType getResourceType() { return resourceType; }
    public int getQuantity() { return quantity; }
    public Program getProgram() { return program; }
}
