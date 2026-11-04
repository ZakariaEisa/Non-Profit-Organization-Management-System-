package model.beneficiary;

import java.time.LocalDate;
import model.people.Beneficiary;

/**
 * Records how much a program helped one beneficiary (score 1-10 + notes).
 */
public class ImpactEvaluation {
    private int evaluationId;
    private LocalDate evaluationDate;
    private Program program;
    private Beneficiary beneficiary;
    private int impactScore;
    private String outcomeNotes;

    public ImpactEvaluation(int evaluationId, Program program, Beneficiary beneficiary, int impactScore, String outcomeNotes) {
        if (impactScore < 1 || impactScore > 10) {
            throw new IllegalArgumentException("impact score must be between 1 and 10");
        }
        this.evaluationId = evaluationId;
        this.program = program;
        this.beneficiary = beneficiary;
        this.impactScore = impactScore;
        this.outcomeNotes = outcomeNotes;
        this.evaluationDate = LocalDate.now();
    }

    public String getSummary() {
        return beneficiary.getFullName() + " in '" + program.getName() + "': " + impactScore + "/10 - " + outcomeNotes;
    }

    public int getEvaluationId() { return evaluationId; }
    public LocalDate getEvaluationDate() { return evaluationDate; }
    public Program getProgram() { return program; }
    public Beneficiary getBeneficiary() { return beneficiary; }
    public int getImpactScore() { return impactScore; }
    public String getOutcomeNotes() { return outcomeNotes; }
}
