import java.time.LocalDate;
import controller.BeneficiaryController;
import controller.CommunicationController;
import controller.DonationController;
import controller.EventController;
import controller.LookupController;
import controller.VolunteerController;
import database.DatabaseManager;
import model.beneficiary.BeneficiaryNeed;
import model.beneficiary.Program;
import model.beneficiary.ResourceDistribution;
import model.donation.Campaign;
import model.donation.Donation;
import model.donation.InKindDonation;
import model.event.Event;
import model.event.EventRegistration;
import model.event.Task;
import model.event.VolunteerAssignment;
import model.lookup.BeneficiaryCategory;
import model.lookup.DonationType;
import model.lookup.EventType;
import model.lookup.NotificationChannel;
import model.lookup.ResourceType;
import model.lookup.Skill;
import model.notification.NotificationService;
import model.people.Beneficiary;
import model.people.Donor;
import model.people.Volunteer;
import view.AdminView;
import view.BeneficiaryView;
import view.DonorView;
import view.VolunteerView;

/**
 * Entry point of the NPO Management System.
 * Phase 1 demo: walks through every Phase 1 pattern and the client requirements.
 */
public class Main {
    public static void main(String[] args) {
        // ---------- SINGLETON ----------
        section("Singleton - DatabaseManager");
        DatabaseManager db = DatabaseManager.getInstance();
        db.connect();
        System.out.println("  same instance everywhere: " + (db == DatabaseManager.getInstance()));

        // ---------- MVC wiring ----------
        NotificationService notificationService = new NotificationService();
        LookupController lookupController = new LookupController();
        DonationController donationController = new DonationController(notificationService);
        EventController eventController = new EventController(notificationService);
        VolunteerController volunteerController = new VolunteerController();
        BeneficiaryController beneficiaryController = new BeneficiaryController();
        CommunicationController communicationController = new CommunicationController(notificationService);

        AdminView adminView = new AdminView(lookupController, eventController, communicationController);
        DonorView donorView = new DonorView(donationController);
        VolunteerView volunteerView = new VolunteerView(volunteerController);
        BeneficiaryView beneficiaryView = new BeneficiaryView(beneficiaryController);

        // ---------- CRUD LOOKUP TABLES ----------
        section("CRUD lookup tables (CR-07)");
        adminView.showDashboard();
        DonationType general = new DonationType(1, "General", "Unrestricted donation");
        DonationType education = new DonationType(2, "Education Fund", "School support");
        EventType fundraiser = new EventType(1, "Fundraiser", "Raise money");
        EventType workshop = new EventType(2, "Workshop", "Training session");
        Skill firstAid = new Skill(1, "First Aid", "Certified first aider");
        Skill driving = new Skill(2, "Driving", "Has a driving license");
        BeneficiaryCategory orphans = new BeneficiaryCategory(1, "Orphans", "Children without parents");
        ResourceType foodBasket = new ResourceType(1, "Food Basket", "Monthly food basket");
        NotificationChannel email = new NotificationChannel(1, "Email", null);
        NotificationChannel sms = new NotificationChannel(2, "SMS", null);
        NotificationChannel social = new NotificationChannel(3, "Social", null);
        lookupController.add(general);                                    // Create
        lookupController.add(education);
        lookupController.add(fundraiser);
        lookupController.add(workshop);
        lookupController.add(firstAid);
        lookupController.add(driving);
        lookupController.add(orphans);
        lookupController.add(foodBasket);
        lookupController.add(email);
        lookupController.add(sms);
        lookupController.add(social);
        lookupController.add(new Skill(3, "Photography", "Event photos"));
        adminView.manageLookups(Skill.TABLE);                             // Read
        Skill photo = (Skill) lookupController.getById(Skill.TABLE, 3);
        photo.setName("Photography & Video");
        lookupController.edit(photo);                                     // Update
        lookupController.delete(Skill.TABLE, 3);                          // Delete
        System.out.println("  after edit + delete:");
        adminView.manageLookups(Skill.TABLE);

        // ---------- people ----------
        Donor sara = new Donor(1, "Sara Ali", "sara@mail.com", "01000000001", "Cairo", "Individual");
        sara.setPreferredChannel(email);
        Donor omar = new Donor(2, "Omar Hassan", "omar@mail.com", "01000000002", "Giza", "Individual");
        omar.setPreferredChannel(sms);
        Volunteer mona = new Volunteer(3, "Mona Adel", "mona@mail.com", "01000000003", "Cairo", "Weekends");
        mona.addSkill(firstAid);
        mona.addPreferredEventType(fundraiser);
        mona.setPreferredChannel(sms);
        Volunteer karim = new Volunteer(4, "Karim Nabil", "karim@mail.com", "01000000004", "Alex", "Evenings");
        karim.addSkill(driving);
        volunteerController.registerVolunteer(mona);
        volunteerController.registerVolunteer(karim);

        // ---------- FACTORY + OBSERVER + DECORATOR ----------
        section("Factory + Observer + Decorator (CR-01, CR-02, CR-03)");
        Campaign schoolCampaign = new Campaign(1, "Back to School", 10000, LocalDate.now(), LocalDate.now().plusMonths(2));
        donorView.showDonationForm();
        donationController.createDonation("online", 101, 1500, sara, education, schoolCampaign, false);
        donationController.createDonation("recurring", 102, 300, omar, education, schoolCampaign, false);
        Donation inKind = donationController.createDonation("in-kind", 103, 2000, null, general, null, true);
        ((InKindDonation) inKind).setItemDescription("20 school bags");
        System.out.println("- processing donation 101");
        donationController.processDonation(101);
        System.out.println("- processing donation 102 (recurring, SMS donor)");
        donationController.processDonation(102);
        System.out.println("- processing donation 103 (anonymous, in-kind)");
        donationController.processDonation(103);
        System.out.println("- full receipt for donation 101:");
        donorView.showReceipt(101);
        donorView.showHistory(sara);

        // ---------- STRATEGY ----------
        section("Strategy - notification channels (CR-04)");
        communicationController.sendMessage("Email", "Thank you for supporting us!", sara);
        communicationController.sendMessage("SMS", "Thank you for supporting us!", sara);
        communicationController.sendMessage("Social", "Thank you for supporting us!", sara);
        communicationController.sendToPreferredChannel("Your monthly report is ready.", omar);

        // ---------- Events, ticketing, waitlist ----------
        section("Events, tickets and waiting list (CR-05)");
        Event gala = new Event(1, "Charity Gala", "Annual fundraiser", "Cairo Hall",
                LocalDate.now().plusDays(20), 2, 250, fundraiser);
        eventController.createEvent(gala);
        EventRegistration r1 = eventController.registerAttendee(1, sara);
        EventRegistration r2 = eventController.registerAttendee(1, omar);
        EventRegistration r3 = eventController.registerAttendee(1, mona);
        printRegistration(r1);
        printRegistration(r2);
        printRegistration(r3);
        System.out.println("- Omar cancels, the waiting list moves up:");
        eventController.cancelRegistration(1, r2);
        printRegistration(r2);
        printRegistration(r3);
        r1.confirmAttendance();
        System.out.println("  attended so far: " + gala.trackAttendance());
        System.out.println("- event moved (Observer reminds attendees on their own channel):");
        eventController.updateEvent(1, "Nile Ritz Ballroom", LocalDate.now().plusDays(21));

        // ---------- Volunteers ----------
        section("Volunteer matching, hours and certificate (CR-06)");
        Task firstAidDesk = new Task(1, "First aid desk", "Stay at the medical desk", gala.getEventDate());
        firstAidDesk.addRequiredSkill(firstAid);
        gala.addTask(firstAidDesk);
        System.out.println("  suitable volunteers: " + volunteerController.findSuitableVolunteers(firstAidDesk, gala).size()
                + " (" + volunteerController.findSuitableVolunteers(firstAidDesk, gala).get(0).getFullName() + ")");
        System.out.println("  assign Karim (no first aid): " + (volunteerController.assignToTask(karim, firstAidDesk) == null ? "rejected" : "ok"));
        VolunteerAssignment monaAssignment = volunteerController.assignToTask(mona, firstAidDesk);
        monaAssignment.completeAssignment(6);
        volunteerView.showTasks(mona);
        volunteerView.showHours(mona);
        System.out.println(volunteerController.issueCertificate(mona).generateCertificate());

        // ---------- Beneficiaries & impact ----------
        section("Beneficiaries, distributions and program impact");
        Beneficiary youssef = new Beneficiary(5, "Youssef Samir", "-", "01000000005", "Assiut", orphans);
        youssef.updateNeeds(new BeneficiaryNeed(1, "Monthly food", "HIGH", 1));
        beneficiaryController.registerBeneficiary(youssef);
        Program foodProgram = new Program(1, "Food Security 2026", "Monthly baskets", LocalDate.now(), LocalDate.now().plusYears(1));
        beneficiaryController.createProgram(foodProgram);
        foodProgram.enroll(youssef);
        beneficiaryController.recordDistribution(youssef, new ResourceDistribution(1, foodBasket, 1, foodProgram));
        beneficiaryView.showNeeds(youssef);
        beneficiaryView.showDistributions(youssef);
        beneficiaryController.recordImpact(foodProgram, youssef, 8, "No missed meals this month");
        System.out.println("  " + foodProgram.getImpactReport());

        db.disconnect();
    }

    private static void section(String title) {
        System.out.println();
        System.out.println("==================== " + title + " ====================");
    }

    private static void printRegistration(EventRegistration r) {
        System.out.println("  " + r.getAttendee().getFullName() + ": " + r.getRegistrationStatus()
                + (r.getTicket() == null ? "" : " ticket " + r.getTicket().getTicketCode() + " (" + r.getTicket().getStatus() + ")"));
    }
}
