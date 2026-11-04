# NPO Management System - Phase 1

**CSE336 - Software Design Patterns | Major Assessment Task | Team 13**
Ain Shams University - Faculty of Engineering
Presented to **Dr. Raghda Essam**

A management system for a non-profit organization: donations, volunteers, events,
beneficiaries and communication, designed with design patterns and the MVC architecture.

## Team 13

| # | Name | Student ID | Design pattern | Other responsibilities |
|---|------|------------|----------------|------------------------|
| 1 | Mohamed Magdy Mahmoud | 22P0127 | Singleton | Database schema, CRUD lookup tables |
| 2 | Mahmoud Nashaat | 22P0126 | Factory | Donations, recurring donations, campaigns |
| 3 | Mohamed Ghoneem | 22P0124 | MVC | Views, controllers, Main demo, events, tickets, waiting list |
| 4 | Mohamed Mahdy | 22P0089 | Strategy | People classes, volunteer certificates |
| 5 | Zakaria Alaa Eisa | 22P0128 | Observer | Repository setup, client requirements, class diagram, report |
| 6 | Kareem Younis | 22P0136 | Decorator | Beneficiaries, programs and impact evaluation |

## Deliverables

| Deliverable | Location |
|-------------|----------|
| Class diagram | [`docs/NPO_Class_Diagram_UML.pdf`](docs/NPO_Class_Diagram_UML.pdf) |
| Phase 1 report | [`docs/Team13_Phase1_Report.pdf`](docs/Team13_Phase1_Report.pdf) |
| Client requirements (CR-01 ... CR-07) | [`CLIENT_REQUIREMENTS.md`](CLIENT_REQUIREMENTS.md) |
| Database schema (CRUD lookup tables) | [`src/database/schema.sql`](src/database/schema.sql) |
| Source code | [`src/`](src) |

## Phase 1 checklist

| Item | Where |
|------|-------|
| Class diagram | `docs/NPO_Class_Diagram_UML.pdf` |
| Strategy | `model/notification`: `INotificationStrategy`, `EmailStrategy`, `SMSStrategy`, `SocialMediaStrategy`, context `NotificationService` |
| Observer | subjects `Donation`, `Event`; observers in `model/observer` (Admin, Finance, DonorReceipt, CampaignProgress, EventReminder) |
| Decorator | `model/receipt`: `IReceipt`, `BasicReceipt`, `ReceiptDecorator`, `QRCodeReceiptDecorator`, `TaxInfoReceiptDecorator` |
| Factory | `factory/DonationFactory`, `factory/NotificationStrategyFactory` |
| Singleton | `database/DatabaseManager` |
| MVC | `model/` - `controller/` - `view/` |
| Client requirements | `CLIENT_REQUIREMENTS.md` (CR-01 ... CR-07) |
| CRUD lookup tables | `model/lookup` (6 tables extending `LookupEntity`), `controller/LookupController` (add / getById / list / edit / delete), `database/schema.sql` |

## Structure
```
src/
├── Main.java                   demo of every Phase 1 pattern
├── model/                      MODEL (MVC)
│   ├── people/                 Person (abstract), Donor, Volunteer, Beneficiary
│   ├── donation/               Donation (abstract, Observer subject), Cash/Online/InKind/Recurring, Campaign
│   ├── receipt/                DECORATOR
│   ├── observer/               OBSERVER interfaces + concrete observers
│   ├── event/                  Event (Observer subject), EventRegistration, Ticket, Task, VolunteerAssignment
│   ├── certificate/            VolunteerCertificate
│   ├── beneficiary/            BeneficiaryNeed, ResourceDistribution, Program, ImpactEvaluation
│   ├── lookup/                 CRUD lookup tables
│   └── notification/           STRATEGY
├── factory/                    FACTORY
├── database/                   SINGLETON + schema.sql
├── controller/                 CONTROLLER (MVC): BaseController + 6 controllers
└── view/                       VIEW (MVC): Admin / Donor / Volunteer / Beneficiary views
```

## Compile and run (Java 11+)
```
mkdir out
javac -d out $(find src -name "*.java")
java -cp out Main
```
