-- =============================================================
-- NPO Management System - database schema (Phase 1 design)
-- Lookup tables first: the admin manages them with full CRUD
-- (LookupController / AdminView.manageLookups()).
-- =============================================================

-- ---------------- CRUD lookup tables ----------------
CREATE TABLE donation_type (
    id          INT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    active      BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE beneficiary_category (
    id          INT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    active      BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE event_type (
    id          INT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    active      BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE skill (
    id          INT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    active      BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE resource_type (
    id          INT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    active      BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE notification_channel (
    id          INT PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,   -- Email / SMS / Social
    description VARCHAR(255),
    active      BOOLEAN NOT NULL DEFAULT TRUE
);

-- Example CRUD statements used by LookupController
-- CREATE : INSERT INTO skill (id, name, description) VALUES (1, 'First Aid', 'Certified first aid');
-- READ   : SELECT * FROM skill WHERE active = TRUE;
-- UPDATE : UPDATE skill SET name = 'Advanced First Aid' WHERE id = 1;
-- DELETE : DELETE FROM skill WHERE id = 1;

-- ---------------- People ----------------
CREATE TABLE person (
    person_id             INT PRIMARY KEY,
    full_name             VARCHAR(150) NOT NULL,
    email                 VARCHAR(150),
    phone_number          VARCHAR(30),
    address               VARCHAR(255),
    preferred_channel_id  INT REFERENCES notification_channel(id),
    person_kind           VARCHAR(20) NOT NULL   -- DONOR / VOLUNTEER / BENEFICIARY
);

CREATE TABLE donor (
    person_id       INT PRIMARY KEY REFERENCES person(person_id),
    donor_type      VARCHAR(50),
    total_donations DECIMAL(12,2) DEFAULT 0
);

CREATE TABLE volunteer (
    person_id    INT PRIMARY KEY REFERENCES person(person_id),
    availability VARCHAR(100),
    total_hours  DECIMAL(8,2) DEFAULT 0
);

CREATE TABLE volunteer_skill (
    volunteer_id INT REFERENCES volunteer(person_id),
    skill_id     INT REFERENCES skill(id),
    PRIMARY KEY (volunteer_id, skill_id)
);

CREATE TABLE volunteer_preferred_event_type (
    volunteer_id  INT REFERENCES volunteer(person_id),
    event_type_id INT REFERENCES event_type(id),
    PRIMARY KEY (volunteer_id, event_type_id)
);

CREATE TABLE beneficiary (
    person_id          INT PRIMARY KEY REFERENCES person(person_id),
    registration_date  DATE,
    eligibility_status VARCHAR(30),
    category_id        INT REFERENCES beneficiary_category(id)
);

-- ---------------- Donations ----------------
CREATE TABLE campaign (
    campaign_id   INT PRIMARY KEY,
    title         VARCHAR(150) NOT NULL,
    goal_amount   DECIMAL(12,2) NOT NULL,
    raised_amount DECIMAL(12,2) DEFAULT 0,
    start_date    DATE,
    end_date      DATE
);

CREATE TABLE donation (
    donation_id      INT PRIMARY KEY,
    donor_id         INT REFERENCES donor(person_id),
    donation_type_id INT REFERENCES donation_type(id),
    campaign_id      INT REFERENCES campaign(campaign_id),
    method           VARCHAR(20) NOT NULL,   -- CASH / ONLINE / IN_KIND / RECURRING
    amount           DECIMAL(12,2) NOT NULL,
    donation_date    DATE,
    status           VARCHAR(20),
    anonymous        BOOLEAN DEFAULT FALSE,
    extra_details    VARCHAR(255)            -- cashier, transaction id, item, frequency ...
);

-- ---------------- Events ----------------
CREATE TABLE event (
    event_id      INT PRIMARY KEY,
    event_type_id INT REFERENCES event_type(id),
    event_name    VARCHAR(150) NOT NULL,
    description   VARCHAR(500),
    location      VARCHAR(255),
    event_date    DATE,
    capacity      INT,
    ticket_price  DECIMAL(10,2) DEFAULT 0,
    status        VARCHAR(20)
);

CREATE TABLE event_registration (
    registration_id     INT PRIMARY KEY,
    event_id            INT REFERENCES event(event_id),
    person_id           INT REFERENCES person(person_id),
    registration_date   DATE,
    registration_status VARCHAR(20),   -- CONFIRMED / WAITLISTED / CANCELLED
    attendance_status   VARCHAR(20)    -- PENDING / ATTENDED / ABSENT
);

CREATE TABLE ticket (
    ticket_id       INT PRIMARY KEY,
    registration_id INT UNIQUE REFERENCES event_registration(registration_id),
    ticket_code     VARCHAR(50) UNIQUE,
    price           DECIMAL(10,2),
    issue_date      DATE,
    status          VARCHAR(20)        -- VALID / USED / CANCELLED
);

CREATE TABLE task (
    task_id     INT PRIMARY KEY,
    event_id    INT REFERENCES event(event_id),
    task_name   VARCHAR(150),
    description VARCHAR(500),
    deadline    DATE,
    status      VARCHAR(20)
);

CREATE TABLE task_required_skill (
    task_id  INT REFERENCES task(task_id),
    skill_id INT REFERENCES skill(id),
    PRIMARY KEY (task_id, skill_id)
);

CREATE TABLE volunteer_assignment (
    assignment_id     INT PRIMARY KEY,
    volunteer_id      INT REFERENCES volunteer(person_id),
    task_id           INT REFERENCES task(task_id),
    assigned_date     DATE,
    completion_status VARCHAR(20),
    hours_worked      DECIMAL(6,2) DEFAULT 0
);

CREATE TABLE volunteer_certificate (
    certificate_id INT PRIMARY KEY,
    volunteer_id   INT REFERENCES volunteer(person_id),
    issue_date     DATE,
    total_hours    DECIMAL(8,2)
);

-- ---------------- Beneficiaries & programs ----------------
CREATE TABLE beneficiary_need (
    need_id        INT PRIMARY KEY,
    beneficiary_id INT REFERENCES beneficiary(person_id),
    description    VARCHAR(255),
    priority       VARCHAR(20),
    quantity       INT
);

CREATE TABLE program (
    program_id  INT PRIMARY KEY,
    name        VARCHAR(150) NOT NULL,
    description VARCHAR(500),
    start_date  DATE,
    end_date    DATE
);

CREATE TABLE program_enrollment (
    program_id     INT REFERENCES program(program_id),
    beneficiary_id INT REFERENCES beneficiary(person_id),
    PRIMARY KEY (program_id, beneficiary_id)
);

CREATE TABLE resource_distribution (
    distribution_id   INT PRIMARY KEY,
    beneficiary_id    INT REFERENCES beneficiary(person_id),
    resource_type_id  INT REFERENCES resource_type(id),
    program_id        INT REFERENCES program(program_id),
    distribution_date DATE,
    quantity          INT
);

CREATE TABLE impact_evaluation (
    evaluation_id   INT PRIMARY KEY,
    program_id      INT REFERENCES program(program_id),
    beneficiary_id  INT REFERENCES beneficiary(person_id),
    evaluation_date DATE,
    impact_score    INT CHECK (impact_score BETWEEN 1 AND 10),
    outcome_notes   VARCHAR(500)
);
