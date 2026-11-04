# NPO Management System – Client Requirements

These requirements extend the basic requirements in the project brief.
They were collected from the people who will use the system (donors,
volunteers, event coordinators, finance staff and administrators).

## 1. Client (user-collected) requirements

| ID | Requested by | Requirement | Where it is in the design |
|----|--------------|-------------|---------------------------|
| CR-01 | Donor | I want to set up a **recurring monthly donation** so I don't have to donate manually every month. | `RecurringDonation` (new donation type), created by `DonationFactory` |
| CR-02 | Fundraising manager | We need **fundraising campaigns** with a target amount, and the progress must update automatically whenever a donation arrives. | `Campaign`, `CampaignProgressObserver` (Observer) |
| CR-03 | Donor | I want to **donate anonymously**; my name must not appear on public lists or on the receipt. | `Donation.anonymous`, `Donation.getDonorDisplayName()` |
| CR-04 | Donors & volunteers | Every person should choose a **preferred notification channel** (Email / SMS / Social media) and only be contacted that way. | `NotificationChannel` lookup, `Person.preferredChannel`, `NotificationStrategyFactory` + `NotificationService` (Strategy) |
| CR-05 | Event coordinator | When an event is full, new registrations go to a **waiting list**, and the first person on the list is promoted automatically when someone cancels. | `Event.registerAttendee()`, `Event.cancelRegistration()`, `EventRegistration.registrationStatus` |
| CR-06 | Volunteer coordinator | Tasks must list the **skills they require**, and a volunteer can only be assigned if they have those skills. Volunteers can also state which **event types they prefer**. | `Task.requiredSkills`, `Volunteer.preferredEventTypes`, `Volunteer.isQualifiedFor()` |
| CR-07 | Administrator | One **admin page** to add, view, edit and delete all lookup values (donation types, event types, skills, beneficiary categories, resource types, channels) without changing code. | `LookupEntity` + 6 lookup tables, `LookupController`, `AdminView.manageLookups()`, `database/schema.sql` |

## 2. Basic requirements (from the brief) – traceability

| Brief requirement | Classes |
|-------------------|---------|
| Track donor info, amounts, types | `Donor`, `Donation`, `DonationType` |
| Receipts and tax documents | `IReceipt`, `BasicReceipt`, `QRCodeReceiptDecorator`, `TaxInfoReceiptDecorator` (Decorator) |
| Flexible donation methods (online, cash/check, in-kind) | `DonationFactory`, `OnlineDonation`, `CashDonation`, `InKindDonation` (Factory) |
| Volunteer info, skills, availability | `Volunteer`, `Skill` |
| Assign volunteers by preferences and qualifications | `Task`, `VolunteerAssignment`, `Volunteer.isQualifiedFor()` |
| Track volunteer hours, generate certificates | `VolunteerAssignment.hoursWorked`, `Volunteer.trackVolunteerHours()`, `VolunteerCertificate` |
| Create / manage / track events | `Event`, `EventType`, `EventController` |
| Registration, ticketing, attendance | `EventRegistration`, `Ticket`, `Event.trackAttendance()` |
| Event reminders | `IEventObserver`, `EventReminderObserver` (Observer) |
| Beneficiaries and their needs | `Beneficiary`, `BeneficiaryNeed`, `BeneficiaryCategory` |
| Track distribution of resources | `ResourceDistribution`, `ResourceType` |
| Monitor and evaluate program impact | `Program`, `ImpactEvaluation` |
| Email, SMS, social media communication | `INotificationStrategy`, `EmailStrategy`, `SMSStrategy`, `SocialMediaStrategy` (Strategy) |
| Single shared database access | `DatabaseManager` (Singleton) |
| Separation of UI, logic and data | `model` / `controller` / `view` packages (MVC) |
