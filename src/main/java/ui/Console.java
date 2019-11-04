package ui;

import domain.entities.Assignment;
import domain.entities.Grade;
import domain.entities.Student;
import exceptions.ValidationException;
import services.service.AssignmentService;
import services.service.GradeService;
import services.service.StudentService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
        int option, option2;
        while (true) {
            displayMainMenu();
            BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
            try {
                option = Integer.parseInt(bf.readLine());

                if (option == 1) {
                    displayStudentMenu();
                    try {
                        option2 = Integer.parseInt(bf.readLine());
                    } catch (IOException ex) {
                        System.out.println("Option is not valid!");
                        continue;
                    }
                    try {
                        switch (option2) {
                            case 1:
                                addStudentUI();
                                break;
                            case 2:
                                updateStudentUI();
                                break;
                            case 3:
                                findStudentUI();
                                break;
                            case 4:
                                displayAllStudentsUI();
                                break;
                            case 5:
                                deleteStudentUI();
                                break;
                            default:
                                System.out.println("There is no such option.");
                                break;
                        }
                    } catch (ValidationException ex) {
                        System.out.println(ex.getMessage());
                    }
                } else if (option == 2) {
                    displayAssignmentMenu();
                    try {
                        option2 = Integer.parseInt(bf.readLine());
                    } catch (IOException ex) {
                        System.out.println("Option is not valid!");
                        continue;
                    }
                    try {
                        switch (option2) {
                            case 1:
                                addAssignmentUI();
                                break;
                            case 2:
                                updateAssignmentUI();
                                break;
                            case 3:
                                findAssignmentUI();
                                break;
                            case 4:
                                displayAllAssignmentsUI();
                                break;
                            case 5:
                                deleteAssignmentUI();
                                break;
                            default:
                                System.out.println("There is no such option.");
                                break;
                        }
                    } catch (ValidationException | IllegalArgumentException ex) {
                        System.out.println(ex.getMessage());
                    }
                } else if(option == 3) {
                    displayGradeMenu();
                    try {
                        option2 = Integer.parseInt(bf.readLine());
                    } catch (IOException ex) {
                        System.out.println("Option is not valid!");
                        continue;
                    }
                    try {
                        switch (option2) {
                            case 1:
                                addGradeUI();
                                break;
                            case 2:
                                updateGradeUI();
                                break;
                            case 3:
                                findGradeUI();
                                break;
                            case 4:
                                displayAllGradesUI();
                                break;
                            case 5:
                                deleteGradeUI();
                                break;
                            default:
                                System.out.println("There is no such option.");
                                break;
                        }
                    } catch (ValidationException | IllegalArgumentException ex) {
                        System.out.println(ex.getMessage());
                    }
                } else if (option == 0) {
                    System.out.println("Closing application...");
                    break;
                } else {
                    System.out.println("The option does not exist...");
                }
            } catch (IOException | NumberFormatException ex) {
                System.out.println("Option is not valid!");
            }
        }
    }

    //Student operations UI methods ------------------------------------------------------------------------------------------------------------
    private void addStudentUI() throws ValidationException, IllegalArgumentException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String id, firstName, lastName, email, coordinator;
        int group;
        try {
            System.out.println("Id = ");
            id = bf.readLine();
            System.out.println("First name = ");
            firstName = bf.readLine();
            System.out.println("Last name = ");
            lastName = bf.readLine();
            System.out.println("Group = ");
            group = Integer.parseInt(bf.readLine());
            System.out.println("Email = ");
            email = bf.readLine();
            System.out.println("Coordinator = ");
            coordinator = bf.readLine();
            Student st = studentService.saveStudent(id, firstName, lastName, group, email, coordinator);
            if (st != null) {
                System.out.println("Student already exists: " + st.toString());
            } else {
                System.out.println("The student has been stored.");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void updateStudentUI() throws ValidationException, IllegalArgumentException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String id, firstName, lastName, email, coordinator;
        int group;
        try {
            System.out.println("Id = ");
            id = bf.readLine();
            System.out.println("First name = ");
            firstName = bf.readLine();
            System.out.println("Last name = ");
            lastName = bf.readLine();
            System.out.println("Group = ");
            group = Integer.parseInt(bf.readLine());
            System.out.println("Email = ");
            email = bf.readLine();
            System.out.println("Coordinator = ");
            coordinator = bf.readLine();
            Student st = studentService.updateStudent(id, firstName, lastName, group, email, coordinator);
            if (st != null) {
                System.out.println("The student " + st.getFirstName() + ' ' + st.getLastName() + " has been updated.");
            } else {
                System.out.println("The student with the given id does not exist.");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void findStudentUI() throws IllegalArgumentException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String id;
        try {
            System.out.println("Id = ");
            id = bf.readLine();
            Student st = studentService.findStudent(id);
            if (st != null) {
                System.out.println(st.toString());
            } else {
                System.out.println("The student with the given id does not exist.");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void displayAllStudentsUI() {
        studentService.findAllStudents().forEach(System.out::println);
    }

    private void deleteStudentUI() throws IllegalArgumentException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String id;
        try {
            System.out.println("Id = ");
            id = bf.readLine();
            Student st = studentService.deleteStudent(id);
            if (st != null) {
                System.out.println(st.getFirstName() + " " + st.getLastName() + " has been removed.");
            } else {
                System.out.println("The student with the given id does not exist.");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //Assignment operations UI methods --------------------------------------------------------------------------------------------
    private void addAssignmentUI() throws ValidationException, IllegalArgumentException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        int id, deadlineWeek;
        String description;
        try {
            System.out.println("Id = ");
            id = Integer.parseInt(bf.readLine());
            System.out.println("Description = ");
            description = bf.readLine();
            System.out.println("Deadline week = ");
            deadlineWeek = Integer.parseInt(bf.readLine());
            Assignment assignment = assignmentService.addAssignment(id, description, deadlineWeek);
            if (assignment != null) {
                System.out.println("Assignment already exists: " + assignment.toString());
            } else {
                System.out.println("The assignment has been stored.");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void updateAssignmentUI() throws ValidationException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        int id, deadlineWeek, startWeek;
        String description;
        try {
            System.out.println("Id = ");
            id = Integer.parseInt(bf.readLine());
            System.out.println("Description = ");
            description = bf.readLine();
            System.out.println("Start week = ");
            startWeek = Integer.parseInt(bf.readLine());
            System.out.println("Deadline week = ");
            deadlineWeek = Integer.parseInt(bf.readLine());
            Assignment assignment = assignmentService.updateAssignment(id, description, startWeek, deadlineWeek);
            if (assignment != null) {
                System.out.println("The assignment " + assignment.getDescription() + " has been updated.");
            } else {
                System.out.println("The assignment with the given id does not exist.");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void findAssignmentUI() {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        int id;
        try {
            System.out.println("Id = ");
            id = Integer.parseInt(bf.readLine());
            Assignment assignment = assignmentService.findAssignment(id);
            if (assignment != null) {
                System.out.println(assignment.toString());
            } else {
                System.out.println("The assignment with the given id does not exist.");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void displayAllAssignmentsUI() {
        assignmentService.findAllAssignments().forEach(System.out::println);
    }

    private void deleteAssignmentUI() {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        int id;
        try {
            System.out.println("Id = ");
            id = Integer.parseInt(bf.readLine());
            Assignment assignment = assignmentService.deleteAssignment(id);
            if (assignment != null) {
                System.out.println(assignment.getDescription() + " has been removed.");
            } else {
                System.out.println("The assignment with the given id does not exist.");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //Grade operations UI methods -----------------------------------------------------------------------------------------------------
    private void addGradeUI() {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String studentId, professor, feedback;
        int assignmentId, nOfWeeksLate = 0, penalty;
        float value;
        boolean motivation = false;
        try{
            System.out.println("Student id = ");
            studentId = bf.readLine();
            System.out.println("Assignment id = ");
            assignmentId = Integer.parseInt(bf.readLine());
            System.out.println("Professor name = ");
            professor = bf.readLine();

            penalty = gradeService.getGradePenalty(assignmentId);
            if(penalty > 0) {
                System.out.println("The deadline has passed! A penalty was applied. Max grade: " + (10 - penalty));
                System.out.println("Are you late in giving the grade? Weeks late: ");
                nOfWeeksLate = Integer.parseInt(bf.readLine());
            }

            if(penalty - nOfWeeksLate > 2) {
                System.out.println("The student is more than 2 weeks late. Does he have a motivation? (y/n)");
                if(bf.readLine().equals("y")) {
                    motivation = true;
                }
            }

            System.out.println("Grade = ");
            value = Float.parseFloat(bf.readLine());

            System.out.println("Feedback = ");
            feedback = bf.readLine();

            Grade grade = gradeService.addGrade(studentId, assignmentId, value, professor, nOfWeeksLate, penalty, motivation, feedback);
            if (grade != null) {
                System.out.println("Grade already exists: " + grade.toString());
            } else {
                System.out.println("The grade has been stored.");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void updateGradeUI() {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String studentId, professor;
        int assignmentId;
        float value;
        try {
            System.out.println("Student id = ");
            studentId = bf.readLine();
            System.out.println("Assignment id = ");
            assignmentId = Integer.parseInt(bf.readLine());
            System.out.println("Value = ");
            value = Float.parseFloat(bf.readLine());
            System.out.println("Professor name = ");
            professor = bf.readLine();
            Grade grade = gradeService.updateGrade(studentId, assignmentId, value, professor);
            if (grade != null) {
                System.out.println("The grade " + grade.getId() + " has been updated.");
            } else {
                System.out.println("The grade with the given id does not exist.");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void findGradeUI() {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String studentId;
        int assignmentId;
        try{
            System.out.println("Student id: ");
            studentId = bf.readLine();
            System.out.println("Assignment id: ");
            assignmentId = Integer.parseInt(bf.readLine());
            Grade grade = gradeService.findGrade(studentId, assignmentId);
            if (grade != null) {
                System.out.println(grade.toString());
            } else {
                System.out.println("The grade with the given id does not exist.");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void displayAllGradesUI() {
        gradeService.getAllGrades().forEach(System.out::println);
    }

    private void deleteGradeUI() {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        String studentId;
        int assignmentId;
        try {
            System.out.println("Student id = ");
            studentId = bf.readLine();
            System.out.println("Assignment id = ");
            assignmentId = Integer.parseInt(bf.readLine());
            Grade grade = gradeService.removeGrade(studentId, assignmentId);
            if (grade != null) {
                System.out.println(grade.getId() + " has been removed.");
            } else {
                System.out.println("The grade with the given id does not exist.");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //Menu display methods -----------------------------------------------------------------------------------------------------------
    private void displayStudentMenu() {
        System.out.println("Choose one of the following:");
        System.out.println("1. Add a student");
        System.out.println("2. Update a student");
        System.out.println("3. Find a student");
        System.out.println("4. Display all students");
        System.out.println("5. Delete a student");
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
