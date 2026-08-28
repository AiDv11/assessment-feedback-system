# Assessment Feedback System (AFS)

A Java desktop application for managing academic assessment feedback, built with
Java Swing and NetBeans. Four user roles — Student, Lecturer, Academic Staff and
Administrator — each with their own dashboard and permissions.

**University group assignment · January – February 2026 · Team of 4**

---

## My contribution

I built the **Lecturer module** — 3 classes, ~1,270 lines of a ~13,000-line system:

| File | Lines | What it does |
|---|---|---|
| `LecturerDashboard.java` | 206 | Navigation shell and panel routing for the lecturer role |
| `KeyInMarksPanel.java` | 632 | Marks entry via an editable `JTable`, automated grade calculation, single-record and bulk save |
| `ProvideFeedbackPanel.java` | 432 | Feedback authoring, feedback history, student and assessment lookup |

Both panels implement the team's shared `Editor` interface, so the lecturer
screens plug into the same navigation and persistence contract as the rest of
the application.

The remaining modules (Student, Academic Staff, Administrator, and the data
layer) were written by my teammates.

---

## Tech

- **Language:** Java
- **UI:** Java Swing — `.form` files are NetBeans GUI Builder layouts
- **Storage:** flat text files in `data/`
- **IDE:** NetBeans

## Running it

Open the project in NetBeans and run the main class. The `data/` directory must
be present in the working directory — the application reads and writes its text
files there on startup.
