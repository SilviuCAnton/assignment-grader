package ui;

import domain.entities.Assignment;
import domain.entities.Grade;
import domain.entities.Student;
import exceptions.ValidationException;
import services.service.AssignmentService;
import services.service.GradeService;
import services.service.StudentService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Class for the console user interface.
 */
public class Console {

    private StudentService studentService;
    private AssignmentService assignmentService;
    private GradeService gradeService;

    public Console(StudentService studentService, AssignmentService assignmentService, GradeService gradeService) {
        this.studentService = studentService;
        this.assignmentService = assignmentService;
        this.gradeService = gradeService;
    }

    public void run() {
        String option, option2;
        Scanner scanner = new Scanner(System.in);
        label:
        while (true) {
            displayMainMenu();
            try {
                option = scanner.nextLine();
                switch (option) {
                    case "1":
                        displayStudentMenu();
                        option2 = scanner.nextLine();
                        try {
                            switch (option2) {
                                case "1":
                                    addStudentUI(scanner);
                                    break;
                                case "2":
                                    updateStudentUI(scanner);
                                    break;
                                case "3":
                                    findStudentUI(scanner);
                                    break;
                                case "4":
                                    displayAllStudentsUI();
                                    break;
                                case "5":
                                    deleteStudentUI(scanner);
                                    break;
                                case "6":
                                    filterStudentsByGroupUI(scanner);
                                    break;
                                default:
                                    System.out.println("There is no such option.");
                                    break;
                            }
                        } catch (ValidationException ex) {
                            System.out.println(ex.getMessage());
                        }
                        break;
                    case "2":
                        displayAssignmentMenu();
                        option2 = scanner.nextLine();
                        try {
                            switch (option2) {
                                case "1":
                                    addAssignmentUI(scanner);
                                    break;
                                case "2":
                                    updateAssignmentUI(scanner);
                                    break;
                                case "3":
                                    findAssignmentUI(scanner);
                                    break;
                                case "4":
                                    displayAllAssignmentsUI();
                                    break;
                                case "5":
                                    deleteAssignmentUI(scanner);
                                    break;
                                default:
                                    System.out.println("There is no such option.");
                                    break;
                            }
                        } catch (ValidationException | IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                        break;
                    case "3":
                        displayGradeMenu();
                        option2 = scanner.nextLine();
                        try {
                            switch (option2) {
                                case "1":
                                    addGradeUI(scanner);
                                    break;
                                case "2":
                                    updateGradeUI(scanner);
                                    break;
                                case "3":
                                    findGradeUI(scanner);
                                    break;
                                case "4":
                                    displayAllGradesUI();
                                    break;
                                case "5":
                                    deleteGradeUI(scanner);
                                    break;
                                case "6":
                                    filterStudentsBySubmissionUI(scanner);
                                    break;
                                case "7":
                                    filterStudentsBySubmissionAndProfessorUI(scanner);
                                    break;
                                case "8":
                                    filterGradesByAssignmentAndWeekUI(scanner);
                                    break;
                                default:
                                    System.out.println("There is no such option.");
                                    break;
                            }
                        } catch (ValidationException | IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                        break;
                    case "0":
                        System.out.println("Closing application...");
                        scanner.close();
                        break label;
                    default:
                        System.out.println("The option does not exist...");
                        break;
                }
            } catch (NumberFormatException ex) {
                System.out.println("Option is not valid!");
            }
        }
    }

    //Student operations UI methods ------------------------------------------------------------------------------------------------------------
    private void addStudentUI(Scanner scanner) throws ValidationException, IllegalArgumentException {
        String id, firstName, lastName, email, coordinator;
        int group;
        System.out.println("Id = ");
        id = scanner.nextLine();
        System.out.println("First name = ");
        firstName = scanner.nextLine();
        System.out.println("Last name = ");
        lastName = scanner.nextLine();
        System.out.println("Group = ");
        group = Integer.parseInt(scanner.nextLine());
        System.out.println("Email = ");
        email = scanner.nextLine();
        System.out.println("Coordinator = ");
        coordinator = scanner.nextLine();
        Student st = studentService.saveStudent(id, firstName, lastName, group, email, coordinator);
        if (st != null) {
            System.out.println("Student already exists: " + st.toString());
        } else {
            System.out.println("The student has been stored.");
        }
    }

    private void updateStudentUI(Scanner scanner) throws ValidationException, IllegalArgumentException {
        String id, firstName, lastName, email, coordinator;
        int group;
        System.out.println("Id = ");
        id = scanner.nextLine();
        System.out.println("First name = ");
        firstName = scanner.nextLine();
        System.out.println("Last name = ");
        lastName = scanner.nextLine();
        System.out.println("Group = ");
        group = Integer.parseInt(scanner.nextLine());
        System.out.println("Email = ");
        email = scanner.nextLine();
        System.out.println("Coordinator = ");
        coordinator = scanner.nextLine();
        Student st = studentService.updateStudent(id, firstName, lastName, group, email, coordinator);
        if (st != null) {
            System.out.println("The student " + st.getFirstName() + ' ' + st.getLastName() + " has been updated.");
        } else {
            System.out.println("The student with the given id does not exist.");
        }
    }

    private void findStudentUI(Scanner scanner) throws IllegalArgumentException {
        String id;
        System.out.println("Id = ");
        id = scanner.nextLine();
        Student st = studentService.findStudent(id);
        if (st != null) {
            System.out.println(st.toString());
        } else {
            System.out.println("The student with the given id does not exist.");
        }
    }

    private void displayAllStudentsUI() {
        studentService.findAllStudents().forEach(System.out::println);
    }

    private void deleteStudentUI(Scanner scanner) throws IllegalArgumentException {
        String id;
        System.out.println("Id = ");
        id = scanner.nextLine();
        Student st = studentService.deleteStudent(id);
        if (st != null) {
            System.out.println(st.getFirstName() + " " + st.getLastName() + " has been removed.");
        } else {
            System.out.println("The student with the given id does not exist.");
        }
    }

    private void filterStudentsByGroupUI(Scanner scanner) {
        int group;
        System.out.println("Group = ");
        group = Integer.parseInt(scanner.nextLine());
        studentService.filterStudentsByGroup(group).forEach(System.out::println);
    }

    //Assignment operations UI methods --------------------------------------------------------------------------------------------
    private void addAssignmentUI(Scanner scanner) throws ValidationException, IllegalArgumentException {
        int id, deadlineWeek;
        String description;
        System.out.println("Id = ");
        id = Integer.parseInt(scanner.nextLine());
        System.out.println("Description = ");
        description = scanner.nextLine();
        System.out.println("Deadline week = ");
        deadlineWeek = Integer.parseInt(scanner.nextLine());
        Assignment assignment = assignmentService.addAssignment(id, description, deadlineWeek);
        if (assignment != null) {
            System.out.println("Assignment already exists: " + assignment.toString());
        } else {
            System.out.println("The assignment has been stored.");
        }
    }

    private void updateAssignmentUI(Scanner scanner) throws ValidationException {
        int id, deadlineWeek, startWeek;
        String description;
        System.out.println("Id = ");
        id = Integer.parseInt(scanner.nextLine());
        System.out.println("Description = ");
        description = scanner.nextLine();
        System.out.println("Start week = ");
        startWeek = Integer.parseInt(scanner.nextLine());
        System.out.println("Deadline week = ");
        deadlineWeek = Integer.parseInt(scanner.nextLine());
        Assignment assignment = assignmentService.updateAssignment(id, description, startWeek, deadlineWeek);
        if (assignment != null) {
            System.out.println("The assignment " + assignment.getDescription() + " has been updated.");
        } else {
            System.out.println("The assignment with the given id does not exist.");
        }
    }

    private void findAssignmentUI(Scanner scanner) {
        int id;
        System.out.println("Id = ");
        id = Integer.parseInt(scanner.nextLine());
        Assignment assignment = assignmentService.findAssignment(id);
        if (assignment != null) {
            System.out.println(assignment.toString());
        } else {
            System.out.println("The assignment with the given id does not exist.");
        }
    }

    private void displayAllAssignmentsUI() {
        assignmentService.findAllAssignments().forEach(System.out::println);
    }

    private void deleteAssignmentUI(Scanner scanner) {
        int id;
        System.out.println("Id = ");
        id = Integer.parseInt(scanner.nextLine());
        Assignment assignment = assignmentService.deleteAssignment(id);
        if (assignment != null) {
            System.out.println(assignment.getDescription() + " has been removed.");
        } else {
            System.out.println("The assignment with the given id does not exist.");
        }
    }

    //Grade operations UI methods -----------------------------------------------------------------------------------------------------
    private void addGradeUI(Scanner scanner) {
        String studentId, professor, feedback;
        int assignmentId, nOfWeeksLate = 0, penalty;
        float value;
        boolean motivation = false;
        System.out.println("Student id = ");
        studentId = scanner.nextLine();
        System.out.println("Assignment id = ");
        assignmentId = Integer.parseInt(scanner.nextLine());
        System.out.println("Professor name = ");
        professor = scanner.nextLine();

        penalty = gradeService.getGradePenalty(assignmentId);
        if (penalty > 0) {
            System.out.println("The deadline has passed! A penalty was applied. Max grade: " + (10 - penalty));
            System.out.println("Are you late in giving the grade? Weeks late: ");
            nOfWeeksLate = Integer.parseInt(scanner.nextLine());
        }

        if (penalty - nOfWeeksLate > 2) {
            System.out.println("The student is more than 2 weeks late. Does he have a motivation? (y/n)");
            if (scanner.nextLine().equals("y")) {
                motivation = true;
            }
        }

        System.out.println("Grade = ");
        value = Float.parseFloat(scanner.nextLine());

        System.out.println("Feedback = ");
        feedback = scanner.nextLine();

        Grade grade = gradeService.addGrade(studentId, assignmentId, value, professor, nOfWeeksLate, penalty, motivation, feedback);
        if (grade != null) {
            System.out.println("Grade already exists: " + grade.toString());
        } else {
            System.out.println("The grade has been stored.");
        }
    }

    private void updateGradeUI(Scanner scanner) {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String studentId, professor;
        int assignmentId;
        float value;
        System.out.println("Student id = ");
        studentId = scanner.nextLine();
        System.out.println("Assignment id = ");
        assignmentId = Integer.parseInt(scanner.nextLine());
        System.out.println("Value = ");
        value = Float.parseFloat(scanner.nextLine());
        System.out.println("Professor name = ");
        professor = scanner.nextLine();
        Grade grade = gradeService.updateGrade(studentId, assignmentId, value, professor);
        if (grade != null) {
            System.out.println("The grade " + grade.getId() + " has been updated.");
        } else {
            System.out.println("The grade with the given id does not exist.");
        }
    }

    private void findGradeUI(Scanner scanner) {
        String studentId;
        int assignmentId;
        System.out.println("Student id: ");
        studentId = scanner.nextLine();
        System.out.println("Assignment id: ");
        assignmentId = Integer.parseInt(scanner.nextLine());
        Grade grade = gradeService.findGrade(studentId, assignmentId);
        if (grade != null) {
            System.out.println(grade.toString());
        } else {
            System.out.println("The grade with the given id does not exist.");
        }

    }

    private void displayAllGradesUI() {
        gradeService.getAllGrades().forEach(System.out::println);
    }

    private void deleteGradeUI(Scanner scanner) {
        String studentId;
        int assignmentId;
        System.out.println("Student id = ");
        studentId = scanner.nextLine();
        System.out.println("Assignment id = ");
        assignmentId = Integer.parseInt(scanner.nextLine());
        Grade grade = gradeService.removeGrade(studentId, assignmentId);
        if (grade != null) {
            System.out.println(grade.getId() + " has been removed.");
        } else {
            System.out.println("The grade with the given id does not exist.");
        }
    }

    private void filterStudentsBySubmissionUI(Scanner scanner) {
        int assignmentId;
        System.out.println("Assignment id = ");
        assignmentId = Integer.parseInt(scanner.nextLine());
        gradeService.filterStudentsBySubmission(assignmentId).forEach(System.out::println);
    }

    private void filterStudentsBySubmissionAndProfessorUI(Scanner scanner) {
        int assignmentId;
        String professor;
        System.out.println("Assignment id = ");
        assignmentId = Integer.parseInt(scanner.nextLine());
        System.out.println("Professor = ");
        professor = scanner.nextLine();
        gradeService.filterStudentsBySubmissionAndProfessor(assignmentId, professor).forEach(System.out::println);
    }

    private void filterGradesByAssignmentAndWeekUI(Scanner scanner) {
        int assignmentId;
        int week;
        System.out.println("Assignment id = ");
        assignmentId = Integer.parseInt(scanner.nextLine());
        System.out.println("week = ");
        week = Integer.parseInt(scanner.nextLine());
        gradeService.filterGradesByAssignmentAndWeek(assignmentId, week).forEach(System.out::println);
    }

    //Menu display methods -----------------------------------------------------------------------------------------------------------
    private void displayStudentMenu() {
        System.out.println("Choose one of the following:");
        System.out.println("1. Add a student");
        System.out.println("2. Update a student");
        System.out.println("3. Find a student");
        System.out.println("4. Display all students");
        System.out.println("5. Delete a student");
        System.out.println("6. Filter students by group");
        System.out.println("Option: ");
    }

    private void displayAssignmentMenu() {
        System.out.println("Choose one of the following:");
        System.out.println("1. Add an assignment");
        System.out.println("2. Update an assignment");
        System.out.println("3. Find an assignment");
        System.out.println("4. Display all assignments");
        System.out.println("5. Delete an assignment");
        System.out.println("Option: ");
    }

    private void displayGradeMenu() {
        System.out.println("Choose one of the following:");
        System.out.println("1. Add a grade");
        System.out.println("2. Update a grade");
        System.out.println("3. Find a grade");
        System.out.println("4. Display all grades");
        System.out.println("5. Delete a grade");
        System.out.println("6. Show all students with submitted assignment");
        System.out.println("7. Show all students with submitted assignment to a professor");
        System.out.println("8. Show all grades given to an assignment in a given week");
        System.out.println("Option: ");
    }

    private void displayMainMenu() {
        System.out.println("Choose one of the following:");
        System.out.println("1. Student operations");
        System.out.println("2. Assignment operations");
        System.out.println("3. Grade operations");
        System.out.println("0. Exit");
        System.out.println("Option: ");
    }
}
