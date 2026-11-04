package controller;

import java.util.ArrayList;
import java.util.List;
import model.beneficiary.ImpactEvaluation;
import model.beneficiary.Program;
import model.beneficiary.ResourceDistribution;
import model.people.Beneficiary;

public class BeneficiaryController extends BaseController {
    private final List<Beneficiary> beneficiaries = new ArrayList<>();
    private final List<Program> programs = new ArrayList<>();
    private int nextEvaluationId = 1;

    public void registerBeneficiary(Beneficiary beneficiary) {
        beneficiaries.add(beneficiary);
    }

    public void recordDistribution(Beneficiary beneficiary, ResourceDistribution distribution) {
        if (!beneficiary.checkEligibility()) {
            throw new IllegalStateException(beneficiary.getFullName() + " is not eligible");
        }
        distribution.distributeResource(beneficiary);
    }

    public boolean checkEligibility(Beneficiary beneficiary) {
        return beneficiary.checkEligibility();
    }

    public void createProgram(Program program) {
        programs.add(program);
    }

    public ImpactEvaluation recordImpact(Program program, Beneficiary beneficiary, int score, String notes) {
        return program.evaluate(nextEvaluationId++, beneficiary, score, notes);
    }
}
