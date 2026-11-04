package model.beneficiary;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.people.Beneficiary;

/**
 * A service program (e.g. "School Support 2026") that beneficiaries are enrolled in.
 * Used to monitor and evaluate the impact of the organization's work.
 */
public class Program {
    private int programId;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private final List<Beneficiary> enrolledBeneficiaries = new ArrayList<>();
    private final List<ImpactEvaluation> evaluations = new ArrayList<>();

    public Program(int programId, String name, String description, LocalDate startDate, LocalDate endDate) {
        this.programId = programId;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void enroll(Beneficiary beneficiary) {
        if (!enrolledBeneficiaries.contains(beneficiary)) enrolledBeneficiaries.add(beneficiary);
    }

    public ImpactEvaluation evaluate(int evaluationId, Beneficiary beneficiary, int impactScore, String notes) {
        ImpactEvaluation evaluation = new ImpactEvaluation(evaluationId, this, beneficiary, impactScore, notes);
        evaluations.add(evaluation);
        return evaluation;
    }

    public double getAverageImpactScore() {
        if (evaluations.isEmpty()) return 0;
        double sum = 0;
        for (ImpactEvaluation e : evaluations) sum += e.getImpactScore();
        return sum / evaluations.size();
    }

    public String getImpactReport() {
        return "Program '" + name + "': " + enrolledBeneficiaries.size() + " beneficiaries, "
                + evaluations.size() + " evaluations, average impact "
                + String.format("%.1f", getAverageImpactScore()) + "/10";
    }

    public int getProgramId() { return programId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public List<Beneficiary> getEnrolledBeneficiaries() { return Collections.unmodifiableList(enrolledBeneficiaries); }
    public List<ImpactEvaluation> getEvaluations() { return Collections.unmodifiableList(evaluations); }
}
