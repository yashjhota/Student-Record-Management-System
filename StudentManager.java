import java.io.*;
import java.util.*;

class Student {
    int id;
    String name;
    String course;

    Student(int id, String name, String course) {
        this.id = id;
        this.name = name;
        this.course = course;
    }

    public String toCSV() {
        return id + "," + name + "," + course;
    }

    public static Student fromCSV(String line) {
        String[] data = line.split(",");
        return new Student(Integer.parseInt(data[0]), data[1], data[2]);
    }

    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Course: " + course;
    }
}

public class StudentManager {
    static final String FILE_NAME = "students.csv";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n1. Add Student\n2. View Students\n3. Search Student\n4. Update Student\n5. Delete Student\n6. Sort Students\n7. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline
            switch (choice) {
                case 1 -> addStudent(sc);
                case 2 -> viewStudents();
                case 3 -> searchStudent(sc);
                case 4 -> updateStudent(sc);
                case 5 -> deleteStudent(sc);
                case 6 -> sortStudents(sc);
                case 7 -> {
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    static void addStudent(Scanner sc) {
    try {
        System.out.print("Enter ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        List<Student> students = readStudents();

        // Check for duplicate ID
        for (Student s : students) {
            if (s.id == id) {
                System.out.println("❌ Student with ID " + id + " already exists. Cannot add duplicate.");
                return;
            }
        }

        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Course: ");
        String course = sc.nextLine();
        students.add(new Student(id, name, course));
        writeStudents(students);
        System.out.println("✅ Student added successfully!");
    } catch (Exception e) {
        System.out.println("Error adding student: " + e.getMessage());
    }
}

    static void viewStudents() {
        List<Student> students = readStudents();
        if (students.isEmpty()) {
            System.out.println("No students found!");
            return;
        }
        System.out.println("\nStudent Records:");
        for (Student s : students) {
            System.out.println(s);
        }
    }

    static void searchStudent(Scanner sc) {
        System.out.print("Enter ID or Name to search: ");
        String key = sc.nextLine().toLowerCase();
        List<Student> students = readStudents();
        boolean found = false;
        for (Student s : students) {
            if (String.valueOf(s.id).equals(key) || s.name.toLowerCase().contains(key)) {
                System.out.println(s);
                found = true;
            }
        }
        if (!found) System.out.println("Student not found!");
    }

    static void updateStudent(Scanner sc) {
        try {
            System.out.print("Enter ID to update: ");
            int id = sc.nextInt();
            sc.nextLine();
            List<Student> students = readStudents();
            boolean updated = false;
            for (Student s : students) {
                if (s.id == id) {
                    System.out.print("Enter new name: ");
                    s.name = sc.nextLine();
                    System.out.print("Enter new course: ");
                    s.course = sc.nextLine();
                    updated = true;
                    break;
                }
            }
            if (updated) {
                writeStudents(students);
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("Student with ID " + id + " not found.");
            }
        } catch (Exception e) {
            System.out.println("Error updating student: " + e.getMessage());
        }
    }

    static void deleteStudent(Scanner sc) {
        try {
            System.out.print("Enter ID to delete: ");
            int id = sc.nextInt();
            sc.nextLine();
            List<Student> students = readStudents();
            boolean removed = students.removeIf(s -> s.id == id);
            if (removed) {
                writeStudents(students);
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Student with ID " + id + " not found.");
            }
        } catch (Exception e) {
            System.out.println("Error deleting student: " + e.getMessage());
        }
    }

    static void sortStudents(Scanner sc) {
        System.out.print("Sort by 'ID' or 'Name': ");
        String option = sc.nextLine();
        List<Student> students = readStudents();
        if (option.equalsIgnoreCase("ID")) {
            students.sort(Comparator.comparingInt(s -> s.id));
        } else if (option.equalsIgnoreCase("Name")) {
            students.sort(Comparator.comparing(s -> s.name.toLowerCase()));
        } else {
            System.out.println("Invalid sort option!");
            return;
        }
        writeStudents(students);
        System.out.println("Students sorted successfully!");
    }

    static List<Student> readStudents() {
        List<Student> list = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return list;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(Student.fromCSV(line));
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return list;
    }

    static void writeStudents(List<Student> students) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Student s : students) {
                writer.println(s.toCSV());
            }
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }
}