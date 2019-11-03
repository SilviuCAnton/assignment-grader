package ui;

import domain.Assignment;
import domain.Student;
import exceptions.ValidationException;
import services.config.ApplicationContext;
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

    public Console() {
        this.studentService = new StudentService(ApplicationContext.getProperties().getProperty("data.catalog.students"));
        this.assignmentService = new AssignmentService(ApplicationContext.getProperties().getProperty("data.catalog.assignments"));
        this.gradeService = new GradeService(this.studentService, this.assignmentService, ApplicationContext.getProperties().getProperty("data.catalog.grades"));
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
            System.out.print("Id = ");
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
        int id, deadlineWeek;
        String description;
        try {
            System.out.println("Id = ");
            id = Integer.parseInt(bf.readLine());
            System.out.println("Description = ");
            description = bf.readLine();
            System.out.println("Deadline week = ");
            deadlineWeek = Integer.parseInt(bf.readLine());
            Assignment assignment = assignmentService.updateAssignment(id, description, deadlineWeek);
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
            System.out.print("Id = ");
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
        System.out.println("1. Add a assignment");
        System.out.println("2. Update a assignment");
        System.out.println("3. Find a assignment");
        System.out.println("4. Display all assignments");
        System.out.println("5. Delete a assignment");
        System.out.println("Option: ");
    }

    private void displayMainMenu() {
        System.out.println("Choose one of the following:");
        System.out.println("1. Student operations");
        System.out.println("2. Assignment operations");
        System.out.println("0. Exit");
        System.out.println("Option: ");
    }
}
