"""
Draws the Phase 1 UML class diagram of the NPO Management System.

    python draw_class_diagram.py <output.pdf> [preview.png]
"""
import sys
import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt
from matplotlib.patches import FancyBboxPatch, Rectangle, Polygon
from matplotlib.textpath import TextPath
from matplotlib.font_manager import FontProperties

W, H = 2800, 1860
FS_NAME, FS_MEM, FS_SMALL = 9, 7.2, 6.8
LH = 9.6                     # member line height
PAD = 6

FP = FontProperties(family="DejaVu Sans", size=FS_MEM)
FPB = FontProperties(family="DejaVu Sans", size=FS_NAME, weight="bold")


def text_w(s, fp):
    return TextPath((0, 0), s, prop=fp).get_extents().width


COLORS = {
    "model": ("#e8f0fb", "#2f5d9e"),
    "ctrl": ("#fff4c2", "#a07c00"),
    "view": ("#fff4c2", "#a07c00"),
    "single": ("#fde0e0", "#b03030"),
    "factory": ("#fde6c8", "#c06a00"),
    "observer": ("#efe2f7", "#7a3fa0"),
    "strategy": ("#dcf3ef", "#1d7f72"),
    "decorator": ("#e4f3da", "#3f8a2a"),
    "lookup": ("#ececec", "#555555"),
    "new": ("#e8f0fb", "#2f5d9e"),
}

classes = {}


class Box:
    def __init__(self, name, attrs, methods, kind, stereo=None, abstract=False, tag=None, minw=0):
        self.name, self.attrs, self.methods = name, attrs, methods
        self.kind, self.stereo, self.abstract, self.tag = kind, stereo, abstract, tag
        head = [name] + ([stereo] if stereo else []) + ([tag] if tag else [])
        widths = [text_w(name, FPB)] + [text_w(m, FP) for m in attrs + methods + head[1:]]
        self.w = max(minw, max(widths) + 2 * PAD + 4)
        self.nh = len(head)
        self.h_head = 6 + 12 + (self.nh - 1) * 10
        self.h_attr = max(1, len(attrs)) * LH + 5 if attrs else 7
        self.h_meth = max(1, len(methods)) * LH + 5 if methods else 7
        self.h = self.h_head + self.h_attr + self.h_meth
        self.x = self.y = 0
        classes[name] = self

    # geometry helpers (y grows downward)
    @property
    def cx(self): return self.x + self.w / 2

    @property
    def cy(self): return self.y + self.h / 2

    def top(self, f=0.5): return (self.x + self.w * f, self.y)

    def bottom(self, f=0.5): return (self.x + self.w * f, self.y + self.h)

    def left(self, f=0.5): return (self.x, self.y + self.h * f)

    def right(self, f=0.5): return (self.x + self.w, self.y + self.h * f)


def C(name, attrs=(), methods=(), kind="model", **kw):
    return Box(name, list(attrs), list(methods), kind, **kw)


def place(name, x, y):
    b = classes[name]
    b.x, b.y = x, y
    return b


def row(names, x0, y, gap=18):
    x = x0
    for n in names:
        place(n, x, y)
        x += classes[n].w + gap
    return x - gap


def center_row(names, cx, y, gap=18):
    total = sum(classes[n].w for n in names) + gap * (len(names) - 1)
    row(names, cx - total / 2, y, gap)


# =====================================================================
#                              CLASSES
# =====================================================================
# ---------- View ----------
C("AdminView", ["- lookupController: LookupController", "- eventController: EventController",
                "- communicationController: CommunicationController"],
  ["+ showDashboard()", "+ manageLookups(table: String)"], "view")
C("DonorView", ["- donationController: DonationController"],
  ["+ showDonationForm()", "+ showHistory(d: Donor)", "+ showReceipt(id: int)"], "view")
C("VolunteerView", ["- volunteerController: VolunteerController"],
  ["+ showTasks(v: Volunteer)", "+ showHours(v: Volunteer)"], "view")
C("BeneficiaryView", ["- beneficiaryController: BeneficiaryController"],
  ["+ showNeeds(b: Beneficiary)", "+ showDistributions(b: Beneficiary)"], "view")

# ---------- Controller ----------
C("BaseController", ["# db: DatabaseManager"], [], "ctrl", stereo="{abstract}", abstract=True, minw=170)
C("DonationController", ["- donationFactory: DonationFactory", "- donations: Map<Integer, Donation>",
                         "- observers: IDonationObserver[]"],
  ["+ createDonation(type, id, amount, donor,", "      donationType, campaign, anonymous): Donation",
   "+ processDonation(id: int)", "+ generateReceipt(id: int): String", "+ find(id: int): Donation"], "ctrl")
C("VolunteerController", ["- volunteers: List<Volunteer>"],
  ["+ registerVolunteer(v: Volunteer)", "+ assignToTask(v, t: Task): VolunteerAssignment",
   "+ findSuitableVolunteers(t, e): List<Volunteer>", "+ trackHours(v: Volunteer): double",
   "+ issueCertificate(v): VolunteerCertificate"], "ctrl")
C("EventController", ["- events: Map<Integer, Event>", "- reminderObserver: EventReminderObserver"],
  ["+ createEvent(e: Event)", "+ registerAttendee(eventId, p: Person)", "+ cancelRegistration(eventId, r)",
   "+ updateEvent(eventId, location, date)", "+ cancelEvent(eventId: int)"], "ctrl")
C("BeneficiaryController", ["- beneficiaries: List<Beneficiary>", "- programs: List<Program>"],
  ["+ registerBeneficiary(b: Beneficiary)", "+ recordDistribution(b, d)", "+ checkEligibility(b): boolean",
   "+ createProgram(p: Program)", "+ recordImpact(p, b, score, notes)"], "ctrl")
C("CommunicationController", ["- notificationService: NotificationService"],
  ["+ sendMessage(channel, msg, to: Person)", "+ sendToPreferredChannel(msg, to)"], "ctrl")
C("LookupController", [],
  ["+ add(item: LookupEntity)", "+ getById(table, id): LookupEntity", "+ list(table): List<LookupEntity>",
   "+ edit(item: LookupEntity): boolean", "+ delete(table, id): boolean"], "ctrl", tag="CR-07")

# ---------- Singleton ----------
C("DatabaseManager", ["- instance: DatabaseManager {static}", "- connected: boolean",
                      "- lookupTables: Map<String, Map<Integer, LookupEntity>>"],
  ["- DatabaseManager()", "+ getInstance(): DatabaseManager {static}", "+ connect()", "+ disconnect()",
   "+ executeQuery(sql: String)", "+ insertLookup(item: LookupEntity)", "+ findLookup(table, id): LookupEntity",
   "+ findAllLookups(table): List<LookupEntity>", "+ updateLookup(item): boolean", "+ deleteLookup(table, id): boolean"],
  "single", stereo="«singleton»")

# ---------- Factory ----------
C("DonationFactory", [], ["+ createDonation(type: String, id: int,", "      amount: double): Donation"], "factory")
C("NotificationStrategyFactory", [], ["+ createStrategy(channel: String):", "      INotificationStrategy {static}"],
  "factory", stereo="«factory»", tag="CR-04")

# ---------- People ----------
C("Person", ["- personId: int", "- fullName: String", "- email: String", "- phoneNumber: String", "- address: String",
             "- preferredChannel: NotificationChannel"],
  ["+ updateProfile(email, phone, address)", "+ getContactInformation(): String"],
  stereo="{abstract}", abstract=True)
C("Donor", ["- donorType: String", "- totalDonations: double", "- donations: List<Donation>"],
  ["+ makeDonation(d: Donation)", "+ viewDonationHistory(): List<Donation>"])
C("Volunteer", ["- availability: String", "- totalHours: double", "- skills: List<Skill>",
                "- preferredEventTypes: List<EventType>", "- assignments: List<VolunteerAssignment>",
                "- certificates: List<VolunteerCertificate>"],
  ["+ updateAvailability(a: String)", "+ addSkill(s: Skill)", "+ addPreferredEventType(t: EventType)",
   "+ isQualifiedFor(t: Task): boolean", "+ prefers(t: EventType): boolean", "+ viewAssignedTasks(): List<Task>",
   "+ trackVolunteerHours(): double", "+ generateCertificate(id): VolunteerCertificate"])
C("Beneficiary", ["- registrationDate: LocalDate", "- eligibilityStatus: String", "- category: BeneficiaryCategory",
                  "- needs: List<BeneficiaryNeed>", "- distributions: List<ResourceDistribution>"],
  ["+ updateNeeds(n: BeneficiaryNeed)", "+ receive(d: ResourceDistribution)",
   "+ viewDistributionHistory(): List<...>", "+ checkEligibility(): boolean"])

# ---------- Donations ----------
C("Donation", ["- donationId: int", "- amount: double", "- donationDate: LocalDate", "- donationStatus: String",
               "- donationType: DonationType", "- donor: Donor", "- campaign: Campaign", "- anonymous: boolean",
               "- observers: List<IDonationObserver>"],
  ["+ processDonation() {abstract}", "+ getMethod(): String {abstract}", "# markCompleted()",
   "+ getDonationDetails(): String", "+ getDonorDisplayName(): String", "+ attach(o: IDonationObserver)",
   "+ detach(o: IDonationObserver)", "+ notifyObservers()"], stereo="{abstract}", abstract=True)
C("CashDonation", ["- cashierName: String"], ["+ processDonation()"])
C("OnlineDonation", ["- transactionId: String", "- paymentGateway: String"], ["+ processDonation()"])
C("InKindDonation", ["- itemDescription: String", "- estimatedValue: double"], ["+ processDonation()"])
C("RecurringDonation", ["- frequency: String", "- nextChargeDate: LocalDate", "- activeSubscription: boolean"],
  ["+ processDonation()", "+ cancelSubscription()"], tag="CR-01")
C("Campaign", ["- campaignId: int", "- title: String", "- goalAmount: double", "- raisedAmount: double",
               "- startDate: LocalDate", "- endDate: LocalDate"],
  ["+ addContribution(amount: double)", "+ getProgressPercentage(): double", "+ isGoalReached(): boolean"],
  tag="CR-02")

# ---------- Events ----------
C("Event", ["- eventId: int", "- eventName: String", "- description: String", "- location: String",
            "- eventDate: LocalDate", "- capacity: int", "- ticketPrice: double", "- status: String",
            "- eventType: EventType", "- registrations: List<EventRegistration>", "- tasks: List<Task>",
            "- observers: List<IEventObserver>"],
  ["+ createEvent()", "+ updateEvent(location, date)", "+ cancelEvent()",
   "+ registerAttendee(p: Person): EventRegistration", "+ cancelRegistration(r): EventRegistration",
   "+ trackAttendance(): int", "+ addTask(t: Task)", "+ attach(o: IEventObserver)", "+ detach(o: IEventObserver)",
   "+ notifyObservers()"])
C("EventRegistration", ["- registrationId: int", "- registrationDate: LocalDate", "- registrationStatus: String",
                        "- attendanceStatus: String", "- attendee: Person", "- ticket: Ticket"],
  ["+ register(t: Ticket)", "+ waitlist()", "+ cancelRegistration()", "+ confirmAttendance(): boolean"], tag="CR-05")
C("Ticket", ["- ticketId: int", "- ticketCode: String", "- price: double", "- issueDate: LocalDate", "- status: String"],
  ["+ issue()", "+ validate(): boolean", "+ cancel()"])
C("Task", ["- taskId: int", "- taskName: String", "- description: String", "- deadline: LocalDate", "- status: String",
           "- requiredSkills: List<Skill>"],
  ["+ addRequiredSkill(s: Skill)", "+ assignVolunteer(v, id): VolunteerAssignment", "+ updateTaskStatus(s: String)"],
  tag="CR-06")
C("VolunteerAssignment", ["- assignmentId: int", "- assignedDate: LocalDate", "- completionStatus: String",
                          "- hoursWorked: double"],
  ["+ assign()", "+ completeAssignment(hours: double)", "+ updateHours(hours: double)"])
C("VolunteerCertificate", ["- certificateId: int", "- issueDate: LocalDate", "- totalHours: double"],
  ["+ generateCertificate(): String"])

# ---------- Beneficiaries ----------
C("BeneficiaryNeed", ["- needId: int", "- description: String", "- priority: String", "- quantity: int"],
  ["+ updateNeed(priority, qty)", "+ getNeedDetails(): String"])
C("ResourceDistribution", ["- distributionId: int", "- distributionDate: LocalDate", "- resourceType: ResourceType",
                           "- quantity: int", "- program: Program"],
  ["+ distributeResource(b: Beneficiary)", "+ getDistributionDetails(): String"])
C("Program", ["- programId: int", "- name: String", "- description: String", "- startDate: LocalDate",
              "- endDate: LocalDate"],
  ["+ enroll(b: Beneficiary)", "+ evaluate(id, b, score, notes): ImpactEvaluation",
   "+ getAverageImpactScore(): double", "+ getImpactReport(): String"])
C("ImpactEvaluation", ["- evaluationId: int", "- evaluationDate: LocalDate", "- impactScore: int",
                       "- outcomeNotes: String", "- beneficiary: Beneficiary"], ["+ getSummary(): String"])

# ---------- Lookups ----------
C("LookupEntity", ["- id: int", "- name: String", "- description: String", "- active: boolean"],
  ["+ getTableName(): String {abstract}", "+ getters / setters"], "lookup", stereo="{abstract}", abstract=True)
for n in ["DonationType", "NotificationChannel", "BeneficiaryCategory", "ResourceType", "Skill", "EventType"]:
    C(n, ["+ TABLE: String {static}"], ["+ getTableName(): String"], "lookup",
      tag="CR-04" if n == "NotificationChannel" else None)

# ---------- Observer ----------
C("IDonationObserver", [], ["+ update(d: Donation)"], "observer", stereo="«interface»", minw=150)
C("IEventObserver", [], ["+ update(e: Event)"], "observer", stereo="«interface»", minw=150)
C("AdminDonationObserver", [], ["+ update(d: Donation)"], "observer")
C("FinanceDepartmentObserver", ["- totalReceived: double"], ["+ update(d: Donation)"], "observer")
C("DonorReceiptObserver", ["- notificationService: NotificationService"], ["+ update(d: Donation)"], "observer")
C("CampaignProgressObserver", [], ["+ update(d: Donation)"], "observer", tag="CR-02")
C("EventReminderObserver", ["- notificationService: NotificationService"], ["+ update(e: Event)"], "observer")

# ---------- Strategy ----------
C("NotificationService", ["- strategy: INotificationStrategy"],
  ["+ setStrategy(s: INotificationStrategy)", "+ send(msg: String, to: Person)",
   "+ sendUsingPreferredChannel(msg, to)"], "strategy", stereo="«context»")
C("INotificationStrategy", [], ["+ sendNotification(msg: String, to: Person)"], "strategy", stereo="«interface»")
C("EmailStrategy", [], ["+ sendNotification(msg, to)"], "strategy")
C("SMSStrategy", [], ["+ sendNotification(msg, to)"], "strategy")
C("SocialMediaStrategy", [], ["+ sendNotification(msg, to)"], "strategy")

# ---------- Decorator ----------
C("IReceipt", [], ["+ generateReceipt(): String", "+ getDonation(): Donation"], "decorator", stereo="«interface»")
C("BasicReceipt", ["- donation: Donation"], ["+ generateReceipt(): String", "+ getDonation(): Donation"], "decorator")
C("ReceiptDecorator", ["# wrapped: IReceipt"], ["+ generateReceipt(): String", "+ getDonation(): Donation"],
  "decorator", stereo="{abstract}", abstract=True)
C("QRCodeReceiptDecorator", [], ["+ generateReceipt(): String"], "decorator")
C("TaxInfoReceiptDecorator", [], ["+ generateReceipt(): String"], "decorator")

# =====================================================================
#                              LAYOUT
# =====================================================================
K = classes

# Row A: Factory | View | Controller | Singleton | Legend
place("DonationFactory", 40, 95)
row(["AdminView", "DonorView", "VolunteerView", "BeneficiaryView"], 250, 95, 16)
ctrl_names = ["DonationController", "VolunteerController", "EventController", "BeneficiaryController",
              "CommunicationController", "LookupController"]
cend = row(ctrl_names, 1070, 200, 14)
place("BaseController", (1070 + cend) / 2 - K["BaseController"].w / 2, 95)
place("DatabaseManager", cend + 70, 95)

# Row B: model
MY = 440
place("Donation", 40, MY)
place("Campaign", 215, MY)
row(["CashDonation", "OnlineDonation", "InKindDonation", "RecurringDonation"], 40, 745, 16)
place("Person", 640, MY)
place("Donor", 420, 600)
place("Volunteer", 605, 600)
place("Beneficiary", 810, 600)
place("BeneficiaryNeed", 810, 790)
place("ResourceDistribution", 945, 790)
place("Program", 1111, 790)
place("ImpactEvaluation", 1111, K["Program"].y + K["Program"].h + 30)
place("Event", 1360, MY)
place("EventRegistration", 1600, MY)
place("Ticket", 1770, MY)
place("Task", 1360, 720)
place("VolunteerAssignment", 1600, 720)
place("VolunteerCertificate", 1780, 720)

# lookup band
LEY = 1140
LCY = 1275
place("LookupEntity", 1500, LEY)
place("DonationType", 90, LCY)
place("NotificationChannel", 240, LCY)
place("BeneficiaryCategory", 880, LCY)
place("ResourceType", 1010, LCY)
place("Skill", 1360, LCY)
place("EventType", 1700, LCY)

# Row C: Observer | Strategy | Decorator
RY = 1440
place("IDonationObserver", 250, RY)
row(["AdminDonationObserver", "FinanceDepartmentObserver", "DonorReceiptObserver", "CampaignProgressObserver"],
    40, RY + 80, 16)
place("IEventObserver", 830, RY)
place("EventReminderObserver", 790, RY + 80)
place("NotificationService", 1120, RY)
place("NotificationStrategyFactory", 1330, RY)
place("INotificationStrategy", 1140, RY + 140)
row(["EmailStrategy", "SMSStrategy", "SocialMediaStrategy"], 1040, RY + 260, 16)
place("IReceipt", 1720, RY)
place("BasicReceipt", 1580, RY + 140)
place("ReceiptDecorator", 1800, RY + 140)
row(["QRCodeReceiptDecorator", "TaxInfoReceiptDecorator"], 1700, RY + 270, 16)

# =====================================================================

#                              DRAWING
# =====================================================================
fig = plt.figure(figsize=(W / 72, H / 72))
ax = fig.add_axes([0, 0, 1, 1])
ax.set_xlim(0, W)
ax.set_ylim(H, 0)
ax.axis("off")
fig.patch.set_facecolor("white")

LINE = "#333333"


def frame(title, names, kind, pad=18, extra=(0, 0, 0, 0)):
    bs = [classes[n] for n in names]
    x0 = min(b.x for b in bs) - pad - extra[0]
    y0 = min(b.y for b in bs) - pad - 16 - extra[1]
    x1 = max(b.x + b.w for b in bs) + pad + extra[2]
    y1 = max(b.y + b.h for b in bs) + pad + extra[3]
    col = COLORS[kind][1]
    ax.add_patch(FancyBboxPatch((x0, y0), x1 - x0, y1 - y0, boxstyle="round,pad=0,rounding_size=10",
                                fill=False, ec=col, lw=1.3, ls=(0, (5, 3)), zorder=0))
    ax.text(x0 + 10, y0 + 13, title, fontsize=11, weight="bold", color=col, va="baseline", zorder=1)


def draw_box(b):
    fill, edge = COLORS[b.kind]
    ax.add_patch(Rectangle((b.x + 2.5, b.y + 2.5), b.w, b.h, fc="#00000018", ec="none", zorder=2))
    ax.add_patch(Rectangle((b.x, b.y), b.w, b.h, fc="white", ec="#222222", lw=1.0, zorder=3))
    ax.add_patch(Rectangle((b.x, b.y), b.w, b.h_head, fc=fill, ec="#222222", lw=1.0, zorder=3))
    y = b.y + 6
    if b.stereo:
        ax.text(b.cx, y + 7, b.stereo, fontsize=FS_SMALL, ha="center", va="center", zorder=4, style="italic")
        y += 10
    ax.text(b.cx, y + 7, b.name, fontsize=FS_NAME, ha="center", va="center", weight="bold",
            style="italic" if b.abstract else "normal", zorder=4)
    y += 12
    if b.tag:
        ax.text(b.cx, y + 4, "[" + b.tag + "]", fontsize=FS_SMALL, ha="center", va="center", color="#b00060",
                weight="bold", zorder=4)
    ya = b.y + b.h_head
    ax.plot([b.x, b.x + b.w], [ya + b.h_attr] * 2, color="#222222", lw=0.8, zorder=4)
    for i, a in enumerate(b.attrs):
        ax.text(b.x + PAD, ya + 3 + LH * (i + 0.5), a, fontsize=FS_MEM, va="center", zorder=4)
    ym = ya + b.h_attr
    for i, m in enumerate(b.methods):
        st = "italic" if "{abstract}" in m else "normal"
        ax.text(b.x + PAD, ym + 3 + LH * (i + 0.5), m, fontsize=FS_MEM, va="center", style=st, zorder=4)


def clip(b, p_from):
    """Point where the line from box centre towards p_from leaves the box."""
    cx, cy = b.cx, b.cy
    dx, dy = p_from[0] - cx, p_from[1] - cy
    if dx == 0 and dy == 0:
        return cx, cy
    tx = (b.w / 2) / abs(dx) if dx else 1e9
    ty = (b.h / 2) / abs(dy) if dy else 1e9
    t = min(tx, ty)
    return cx + dx * t, cy + dy * t


def anchor(b, a, other):
    if a is None:
        return clip(b, (other.cx, other.cy) if isinstance(other, Box) else other)
    side, f = a if isinstance(a, tuple) else (a, 0.5)
    return getattr(b, side)(f)


def head_triangle(p, q, filled=False):
    """Hollow triangle at q pointing from p to q."""
    import math
    ang = math.atan2(q[1] - p[1], q[0] - p[0])
    L, Wd = 13, 7
    bx, by = q[0] - L * math.cos(ang), q[1] - L * math.sin(ang)
    pts = [q, (bx + Wd * math.sin(ang), by - Wd * math.cos(ang)), (bx - Wd * math.sin(ang), by + Wd * math.cos(ang))]
    ax.add_patch(Polygon(pts, closed=True, fc=LINE if filled else "white", ec=LINE, lw=1.0, zorder=5))
    return bx, by


def head_open(p, q):
    import math
    ang = math.atan2(q[1] - p[1], q[0] - p[0])
    L, Wd = 10, 5
    bx, by = q[0] - L * math.cos(ang), q[1] - L * math.sin(ang)
    ax.plot([bx + Wd * math.sin(ang), q[0], bx - Wd * math.sin(ang)],
            [by - Wd * math.cos(ang), q[1], by + Wd * math.cos(ang)], color=LINE, lw=1.0, zorder=5)


def diamond(p, q, filled):
    """Diamond at p (the whole side), line goes towards q."""
    import math
    ang = math.atan2(q[1] - p[1], q[0] - p[0])
    L, Wd = 16, 6
    mx, my = p[0] + L / 2 * math.cos(ang), p[1] + L / 2 * math.sin(ang)
    ex, ey = p[0] + L * math.cos(ang), p[1] + L * math.sin(ang)
    pts = [p, (mx + Wd * math.sin(ang), my - Wd * math.cos(ang)), (ex, ey),
           (mx - Wd * math.sin(ang), my + Wd * math.cos(ang))]
    ax.add_patch(Polygon(pts, closed=True, fc=LINE if filled else "white", ec=LINE, lw=1.0, zorder=5))
    return ex, ey


def label_near(p, q, txt, off=10, along=16, color="#222222"):
    import math
    ang = math.atan2(q[1] - p[1], q[0] - p[0])
    x = p[0] + along * math.cos(ang) - off * math.sin(ang)
    y = p[1] + along * math.sin(ang) + off * math.cos(ang)
    ax.text(x, y, txt, fontsize=FS_SMALL, ha="center", va="center", color=color, zorder=6,
            bbox=dict(fc="white", ec="none", pad=0.4, alpha=0.85))


def rel(a, b, kind="assoc", ma=None, mb=None, label=None, aa=None, ab=None, via=(), lw=1.0, color=LINE,
        label_pos=0.5):
    """
    a -> b relationship.
      assoc       : line, open arrow at b (navigable to b)
      line        : plain line
      agg / comp  : diamond at a (a is the whole), line to b
      dep         : dashed line, open arrow at b
      inherit     : solid, hollow triangle at b (b is parent)
      realize     : dashed, hollow triangle at b
    """
    A, B = classes[a], classes[b]
    first_target = via[0] if via else B
    last_source = via[-1] if via else A
    p = anchor(A, aa, first_target)
    q = anchor(B, ab, last_source)
    pts = [p] + list(via) + [q]
    ls = (0, (5, 3)) if kind in ("dep", "realize") else "-"
    start = pts[0]
    if kind in ("agg", "comp"):
        start = diamond(pts[0], pts[1], kind == "comp")
    end = pts[-1]
    if kind in ("inherit", "realize"):
        end = head_triangle(pts[-2], pts[-1])
    xs = [start[0]] + [v[0] for v in pts[1:-1]] + [end[0]]
    ys = [start[1]] + [v[1] for v in pts[1:-1]] + [end[1]]
    ax.plot(xs, ys, color=color, lw=lw, ls=ls, zorder=1.5, solid_capstyle="butt")
    if kind in ("assoc", "dep"):
        head_open(pts[-2], pts[-1])
    if ma:
        label_near(pts[0], pts[1], ma, along=22 if kind in ("agg", "comp") else 14)
    if mb:
        label_near(pts[-1], pts[-2], mb)
    if label:
        i = max(0, min(len(pts) - 2, int((len(pts) - 1) * label_pos)))
        p0, p1 = pts[i], pts[i + 1]
        ax.text((p0[0] + p1[0]) / 2, (p0[1] + p1[1]) / 2, label, fontsize=FS_SMALL, ha="center", va="center",
                style="italic", color="#444444", zorder=6, bbox=dict(fc="white", ec="none", pad=0.5, alpha=0.9))


def tree(parent, children, dashed=False, gap=None):
    """Inheritance / realization drawn as one shared bus."""
    P = classes[parent]
    kids = [classes[c] for c in children]
    top_of_kids = min(k.y for k in kids)
    pb = P.y + P.h
    bus = pb + (top_of_kids - pb) / 2 if gap is None else pb + gap
    ls = (0, (5, 3)) if dashed else "-"
    tip = (P.cx, pb)
    base = head_triangle((P.cx, bus), tip)
    ax.plot([P.cx, P.cx], [base[1], bus], color=LINE, lw=1.0, ls=ls, zorder=1.5)
    xs = [k.cx for k in kids] + [P.cx]
    ax.plot([min(xs), max(xs)], [bus, bus], color=LINE, lw=1.0, ls=ls, zorder=1.5)
    for k in kids:
        ax.plot([k.cx, k.cx], [bus, k.y], color=LINE, lw=1.0, ls=ls, zorder=1.5)


# ---------- title ----------
ax.text(W / 2, 28, "Non-Profit Organization Management System – Phase 1 Class Diagram",
        fontsize=20, weight="bold", ha="center", va="center")


def X(n, f=0.5): return K[n].x + K[n].w * f
def Y(n, f=0.5): return K[n].y + K[n].h * f
def BOT(n): return K[n].y + K[n].h
def RIGHT(n): return K[n].x + K[n].w


# ---------- package frames ----------
frame("Factory Pattern", ["DonationFactory"], "factory")
frame("MVC – View", ["AdminView", "DonorView", "VolunteerView", "BeneficiaryView"], "view")
frame("MVC – Controller", ["BaseController"] + ctrl_names, "ctrl")
frame("Singleton Pattern", ["DatabaseManager"], "single", extra=(0, 0, 42, 0))
frame("Model – Core Domain (MVC – Model)",
      ["Campaign", "Donation", "CashDonation", "RecurringDonation", "Person", "Donor", "Volunteer", "Beneficiary",
       "Event", "Ticket", "VolunteerCertificate", "BeneficiaryNeed", "ImpactEvaluation", "VolunteerAssignment"],
      "model", pad=22, extra=(0, 0, 30, 50))
frame("CRUD Lookup Tables (Model)", ["LookupEntity", "DonationType", "NotificationChannel", "BeneficiaryCategory",
                                     "ResourceType", "Skill", "EventType"], "lookup", extra=(0, 0, 40, 0))
frame("Observer Pattern", ["IDonationObserver", "IEventObserver", "AdminDonationObserver", "FinanceDepartmentObserver",
                           "DonorReceiptObserver", "CampaignProgressObserver", "EventReminderObserver"], "observer")
frame("Strategy Pattern", ["NotificationService", "NotificationStrategyFactory",
                                                       "INotificationStrategy", "EmailStrategy", "SMSStrategy",
                                                       "SocialMediaStrategy"], "strategy")
frame("Decorator Pattern", ["IReceipt", "BasicReceipt", "ReceiptDecorator", "QRCodeReceiptDecorator",
                            "TaxInfoReceiptDecorator"], "decorator", extra=(0, 0, 30, 0))

# ---------- generalization / realization ----------
tree("BaseController", ctrl_names)
tree("Person", ["Donor", "Volunteer", "Beneficiary"])
tree("Donation", ["CashDonation", "OnlineDonation", "InKindDonation", "RecurringDonation"])
tree("LookupEntity", ["DonationType", "NotificationChannel", "BeneficiaryCategory", "ResourceType", "Skill",
                      "EventType"])
tree("IDonationObserver", ["AdminDonationObserver", "FinanceDepartmentObserver", "DonorReceiptObserver",
                           "CampaignProgressObserver"], dashed=True)
tree("IEventObserver", ["EventReminderObserver"], dashed=True)
tree("INotificationStrategy", ["EmailStrategy", "SMSStrategy", "SocialMediaStrategy"], dashed=True)
tree("IReceipt", ["BasicReceipt", "ReceiptDecorator"], dashed=True)
tree("ReceiptDecorator", ["QRCodeReceiptDecorator", "TaxInfoReceiptDecorator"])

G = "#555555"

# ---------- MVC: View -> Controller ----------
for view, ctl, ch in [("BeneficiaryView", "BeneficiaryController", 345), ("VolunteerView", "VolunteerController", 357),
                      ("DonorView", "DonationController", 369), ("AdminView", "LookupController", 381)]:
    rel(view, ctl, "assoc", aa=("bottom", 0.5), ab=("bottom", 0.5), via=[(X(view), ch), (X(ctl), ch)], lw=0.8,
        color=G, mb="1")
ax.text(X("AdminView"), 392, "views use controllers", fontsize=FS_SMALL, style="italic", color=G, ha="left",
        va="center")

# ---------- Controller / Singleton / Factory ----------
rel("BaseController", "DatabaseManager", "assoc", ma="0..*", mb="1", label="db = getInstance()",
    aa=("right", 0.5), ab=("left", (Y("BaseController") - K["DatabaseManager"].y) / K["DatabaseManager"].h))
dm = K["DatabaseManager"]
ax.plot([dm.x + dm.w, dm.x + dm.w + 26, dm.x + dm.w + 26, dm.x + dm.w],
        [dm.y + 28, dm.y + 28, dm.y + 56, dm.y + 56], color=LINE, lw=1.0, zorder=1.5)
head_open((dm.x + dm.w + 26, dm.y + 56), (dm.x + dm.w, dm.y + 56))
ax.text(dm.x + dm.w + 30, dm.y + 42, "instance\n1", fontsize=FS_SMALL, va="center")
rel("LookupController", "DatabaseManager", "dep", label="CRUD", aa=("right", 0.6), ab=("left", 0.85), lw=0.9)
rel("DonationController", "DonationFactory", "assoc", mb="1", label="donationFactory", aa=("top", 0.1),
    ab=("top", 0.5), via=[(X("DonationController", 0.1), 52), (X("DonationFactory"), 52)], lw=0.9, color=G,
    label_pos=0.5)
rel("DonationFactory", "Donation", "dep", label="«create»", aa=("bottom", 0.5), ab=("top", 0.5), lw=1.0,
    via=[(X("DonationFactory"), 420), (X("Donation"), 420)] if abs(X("DonationFactory") - X("Donation")) > 1 else ())
# DatabaseManager stores lookup rows
rel("DatabaseManager", "LookupEntity", "assoc", ma="1", mb="0..*", label="stores rows", aa=("bottom", 0.1),
    ab=("right", 0.5), via=[(X("DatabaseManager", 0.1), 395), (1985, 395), (1985, Y("LookupEntity")),
                            ], lw=0.9, color=G, label_pos=0.6)
# CommunicationController -> NotificationService
rel("CommunicationController", "NotificationService", "assoc", mb="1", label="notificationService",
    aa=("bottom", 0.5), ab=("top", 0.5),
    via=[(X("CommunicationController"), 400), (1965, 400), (1965, 1392), (X("NotificationService"), 1392)],
    lw=0.9, color=G, label_pos=0.55)

# ---------- donations ----------
y_dd = Y("Donation", 0.8)
rel("Donor", "Donation", "assoc", ma="1", mb="0..*", label="makes", aa=("left", (y_dd - K["Donor"].y) / K["Donor"].h),
    ab=("right", 0.8))
rel("Donation", "Campaign", "assoc", ma="0..*", mb="0..1", aa=("right", 0.25),
    ab=("left", (Y("Donation", 0.25) - K["Campaign"].y) / K["Campaign"].h))
gap_x = (RIGHT("CashDonation") + K["OnlineDonation"].x) / 2
rel("Donation", "DonationType", "assoc", ma="0..*", mb="1",
    aa=("bottom", (gap_x - K["Donation"].x) / K["Donation"].w),
    ab=("top", (gap_x - K["DonationType"].x) / K["DonationType"].w), lw=0.9)
rel("Person", "NotificationChannel", "assoc", ma="0..*", mb="0..1", label="prefers", aa=("top", 0.2),
    ab=("bottom", 0.5), via=[(X("Person", 0.2), 418), (28, 418), (28, LCY + 72), (X("NotificationChannel"), LCY + 72)], lw=0.9,
    label_pos=0.45)

# ---------- events ----------
rel("Event", "EventRegistration", "comp", ma="1", mb="0..*", aa=("right", 0.15),
    ab=("left", (Y("Event", 0.15) - K["EventRegistration"].y) / K["EventRegistration"].h))
rel("EventRegistration", "Ticket", "comp", ma="1", mb="0..1", aa=("right", 0.25),
    ab=("left", (Y("EventRegistration", 0.25) - K["Ticket"].y) / K["Ticket"].h))
rel("EventRegistration", "Person", "assoc", ma="0..*", mb="1", label="attendee", aa=("top", 0.5), ab=("top", 0.75),
    via=[(X("EventRegistration"), 425), (X("Person", 0.75), 425)], label_pos=0.5)
rel("Event", "Task", "comp", ma="1", mb="0..*", aa=("bottom", 0.3), ab=("top", 0.3))
rel("Task", "VolunteerAssignment", "assoc", ma="1", mb="0..*", aa=("right", 0.3),
    ab=("left", (Y("Task", 0.3) - K["VolunteerAssignment"].y) / K["VolunteerAssignment"].h))
rel("Event", "EventType", "assoc", ma="0..*", mb="1", aa=("right", 0.95), ab=("top", 0.8),
    via=[(1945, Y("Event", 0.95)), (1945, LCY - 12), (X("EventType", 0.8), LCY - 12)], lw=0.9)

# Volunteer bus (deepest first from the left so lines do not cross each other)
vb = BOT("Volunteer")
rel("Volunteer", "Skill", "assoc", ma="0..*", mb="0..*", label="has", aa=("bottom", 0.2), ab=("top", 0.2),
    via=[(X("Volunteer", 0.2), 1066), (X("Skill", 0.2), 1066)], lw=0.9, label_pos=0.5)
rel("Volunteer", "EventType", "assoc", ma="0..*", mb="0..*", label="prefers", aa=("bottom", 0.4), ab=("top", 0.3),
    via=[(X("Volunteer", 0.4), 1056), (X("EventType", 0.3), 1056)], lw=0.9, label_pos=0.62)
rel("Volunteer", "VolunteerCertificate", "comp", ma="1", mb="0..*", aa=("bottom", 0.6), ab=("bottom", 0.5),
    via=[(X("Volunteer", 0.6), 1046), (X("VolunteerCertificate"), 1046)], lw=1.0)
rel("Volunteer", "VolunteerAssignment", "assoc", ma="1", mb="0..*", aa=("bottom", 0.8), ab=("bottom", 0.5),
    via=[(X("Volunteer", 0.8), 1036), (X("VolunteerAssignment"), 1036)], lw=1.0, label="works", label_pos=0.5)
rel("Task", "Skill", "assoc", ma="0..*", mb="0..*", label="requires", aa=("bottom", (1415 - K["Task"].x) / K["Task"].w),
    ab=("top", (1415 - K["Skill"].x) / K["Skill"].w), lw=0.9)

# ---------- beneficiaries ----------
rel("Beneficiary", "BeneficiaryNeed", "comp", ma="1", mb="0..*", label="has", aa=("bottom", 0.2),
    ab=("top", (X("Beneficiary", 0.2) - K["BeneficiaryNeed"].x) / K["BeneficiaryNeed"].w))
rel("Beneficiary", "ResourceDistribution", "assoc", ma="1", mb="0..*", label="receives", aa=("bottom", 0.95),
    ab=("top", (X("Beneficiary", 0.95) - K["ResourceDistribution"].x) / K["ResourceDistribution"].w))
rel("Program", "Beneficiary", "assoc", ma="0..*", mb="0..*", label="enrolls", aa=("top", 0.2), ab=("right", 0.8),
    via=[(X("Program", 0.2), Y("Beneficiary", 0.8))], label_pos=0.6)
rel("Program", "ImpactEvaluation", "comp", ma="1", mb="0..*", aa=("bottom", 0.3), ab=("top", 0.3))
rel("ResourceDistribution", "Program", "assoc", ma="0..*", mb="0..1", aa=("right", 0.6),
    ab=("left", (Y("ResourceDistribution", 0.6) - K["Program"].y) / K["Program"].h))
bc_x = (RIGHT("BeneficiaryNeed") + K["ResourceDistribution"].x) / 2
rel("Beneficiary", "BeneficiaryCategory", "assoc", ma="0..*", mb="1",
    aa=("bottom", (bc_x - K["Beneficiary"].x) / K["Beneficiary"].w),
    ab=("top", (bc_x - K["BeneficiaryCategory"].x) / K["BeneficiaryCategory"].w), lw=0.9)
rt_x = X("ResourceDistribution", 0.8)
rel("ResourceDistribution", "ResourceType", "assoc", ma="0..*", mb="1", aa=("bottom", 0.8),
    ab=("top", (rt_x - K["ResourceType"].x) / K["ResourceType"].w), lw=0.9)

# ---------- observers ----------
rel("Donation", "IDonationObserver", "agg", ma="1", mb="0..*", label="observers", aa=("left", 0.3), ab=("top", 0.5),
    via=[(14, Y("Donation", 0.3)), (14, 1380), (X("IDonationObserver"), 1380)], label_pos=0.7)
rel("Event", "IEventObserver", "agg", ma="1", mb="0..*", label="observers", aa=("left", 0.95), ab=("top", 0.5),
    via=[(1335, Y("Event", 0.95)), (1335, 1380), (X("IEventObserver"), 1380)], label_pos=0.75)
rel("EventReminderObserver", "NotificationService", "assoc", mb="1", aa=("right", 0.5),
    ab=("left", 0.55), via=[(1060, Y("EventReminderObserver")), (1060, Y("NotificationService", 0.55))], lw=0.9)
rel("DonorReceiptObserver", "NotificationService", "assoc", mb="1", aa=("bottom", 0.8), ab=("bottom", 0.12),
    via=[(X("DonorReceiptObserver", 0.8), 1660), (1095, 1660), (1095, BOT("NotificationService") + 14),
         (X("NotificationService", 0.12), BOT("NotificationService") + 14)], lw=0.9)
rel("DonorReceiptObserver", "IReceipt", "dep", label="«create» receipt", aa=("bottom", 0.3),
    ab=("left", 0.5), via=[(X("DonorReceiptObserver", 0.3), 1825), (1535, 1825), (1535, Y("IReceipt"))], lw=0.9,
    label_pos=0.3)

# ---------- strategy ----------
rel("NotificationService", "INotificationStrategy", "agg", ma="1", mb="1", label="strategy",
    aa=("bottom", 0.45), ab=("top", (X("NotificationService", 0.45) - K["INotificationStrategy"].x) /
                                   K["INotificationStrategy"].w))
rel("NotificationService", "NotificationStrategyFactory", "dep", label="«use»", aa=("right", 0.4),
    ab=("left", (Y("NotificationService", 0.4) - K["NotificationStrategyFactory"].y) /
                K["NotificationStrategyFactory"].h))
rel("NotificationStrategyFactory", "INotificationStrategy", "dep", label="«create»", aa=("bottom", 0.5),
    ab=("right", 0.5), via=[(X("NotificationStrategyFactory"), Y("INotificationStrategy"))], lw=0.9)

# ---------- decorator ----------
rel("ReceiptDecorator", "IReceipt", "agg", ma="0..*", mb="1", label="wraps", aa=("right", 0.3), ab=("right", 0.5),
    via=[(RIGHT("ReceiptDecorator") + 28, Y("ReceiptDecorator", 0.3)), (RIGHT("ReceiptDecorator") + 28, Y("IReceipt"))])

# ---------- legend ----------
lx, ly, lw_ = 2535, 58, 252
ax.add_patch(Rectangle((lx, ly), lw_, 300, fc="white", ec="#222222", lw=1.0, zorder=3))
ax.text(lx + 10, ly + 17, "Legend", fontsize=10, weight="bold", zorder=4)
items = [("inherit", "Generalization (extends)"), ("realize", "Realization (implements)"),
         ("assoc", "Association (navigable)"), ("agg", "Aggregation"), ("comp", "Composition"),
         ("dep", "Dependency «create»/«use»")]
for i, (k, t) in enumerate(items):
    y = ly + 40 + i * 21
    x0, x1 = lx + 12, lx + 82
    ls = (0, (5, 3)) if k in ("dep", "realize") else "-"
    s, e = (x0, y), (x1, y)
    if k in ("agg", "comp"):
        s = diamond((x0, y), (x1, y), k == "comp")
    if k in ("inherit", "realize"):
        e = head_triangle((x0, y), (x1, y))
    ax.plot([s[0], e[0]], [y, y], color=LINE, lw=1.0, ls=ls, zorder=4)
    if k in ("assoc", "dep"):
        head_open((x0, y), (x1, y))
    ax.text(lx + 92, y, t, fontsize=7.8, va="center", zorder=4)
notes = ["+ public   - private   # protected", "{abstract} / italic = abstract", "{static} = class member",
         "«interface» = Java interface", "Multiplicity: 1, 0..1, 0..*"]
for i, t in enumerate(notes):
    ax.text(lx + 12, ly + 176 + i * 17, t, fontsize=7.8, va="center", zorder=4)
ax.text(lx + 12, ly + 176 + len(notes) * 17, "[CR-0x] = added for a client requirement", fontsize=7.8, va="center",
        color="#b00060", weight="bold", zorder=4)

# ---------- client requirements note ----------
nx, ny, nw = 2035, 430, 750
reqs = [
    ("CR-01", "Recurring monthly donations", "RecurringDonation, DonationFactory"),
    ("CR-02", "Fundraising campaigns with live progress", "Campaign, CampaignProgressObserver"),
    ("CR-03", "Anonymous donations", "Donation.anonymous, getDonorDisplayName()"),
    ("CR-04", "Preferred notification channel per person", "NotificationChannel, NotificationStrategyFactory"),
    ("CR-05", "Event waiting list when capacity is full", "Event.registerAttendee(), EventRegistration"),
    ("CR-06", "Skill-based volunteer matching + preferences", "Task.requiredSkills, Volunteer.isQualifiedFor()"),
    ("CR-07", "Admin CRUD for all lookup tables", "LookupEntity, LookupController, AdminView"),
]
nh = 50 + len(reqs) * 33


def note(x, y, w, h):
    ax.add_patch(Polygon([(x, y), (x + w - 20, y), (x + w, y + 20), (x + w, y + h), (x, y + h)], closed=True,
                         fc="#fffbe6", ec="#222222", lw=1.0, zorder=3))
    ax.plot([x + w - 20, x + w - 20, x + w], [y, y + 20, y + 20], color="#222222", lw=1.0, zorder=4)


note(nx, ny, nw, nh)
ax.text(nx + 14, ny + 24, "Client Requirements (collected from users)", fontsize=11, weight="bold", zorder=4)
for i, (cid, txt, where) in enumerate(reqs):
    y = ny + 50 + i * 33
    ax.text(nx + 14, y, cid, fontsize=9, weight="bold", color="#b00060", va="center", zorder=4)
    ax.text(nx + 75, y - 6, txt, fontsize=9, va="center", zorder=4)
    ax.text(nx + 75, y + 8, "→ " + where, fontsize=8, va="center", color="#555555", style="italic", zorder=4)

# ---------- patterns summary note ----------
px, py = 2035, ny + nh + 30
pats = [
    ("Singleton", "DatabaseManager – one shared DB connection (getInstance())"),
    ("Factory", "DonationFactory, NotificationStrategyFactory – create objects from user input"),
    ("Observer", "Donation / Event notify Admin, Finance, DonorReceipt, Campaign, EventReminder"),
    ("Strategy", "NotificationService switches Email / SMS / Social at runtime"),
    ("Decorator", "Receipts extended with tax info and QR code without subclassing"),
    ("MVC", "view → controller → model packages; BaseController shares the DB"),
    ("CRUD lookups", "6 tables extend LookupEntity; LookupController add/get/list/edit/delete"),
]
ph = 50 + len(pats) * 24
note(px, py, nw, ph)
ax.text(px + 14, py + 24, "Phase 1 Design Patterns", fontsize=11, weight="bold", zorder=4)
for i, (p, t) in enumerate(pats):
    y = py + 50 + i * 24
    ax.text(px + 14, y, p, fontsize=9, weight="bold", va="center", zorder=4)
    ax.text(px + 115, y, t, fontsize=8.5, va="center", zorder=4)


for b in classes.values():
    draw_box(b)

out_pdf = sys.argv[1]
fig.savefig(out_pdf, metadata={"Title": "NPO Management System - Phase 1 Class Diagram", "Author": "Zakaria Eisa",
                                "Subject": "UML class diagram", "Creator": "matplotlib"})
if len(sys.argv) > 2:
    fig.savefig(sys.argv[2], dpi=60)
print("saved", out_pdf)
