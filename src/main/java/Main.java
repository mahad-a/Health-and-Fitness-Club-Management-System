import java.sql.*;
import java.util.Scanner;

public class Main {
    public static Connection conn; // allows other functions to access conn
    public enum Days {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    public enum Times {
        MORNING, AFTERNOON, EVENING, NIGHT
    }

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/project_3005";
        String user = "postgres";
        String password = "comp3005";
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, password);
            if (conn != null) {
                System.out.println("Connected to PostgreSQL successfully!\n");
                System.out.println("Welcome to Health and Fitness Club Management System!");
                System.out.println("---------------------------------------------------------------");

                GUI.main(args); // abandoned command line code, delete when completed

                Scanner scanner = new Scanner(System.in);
                boolean running = true;
                while (running) {
//                    System.out.println("Select the desired display: ");
                    System.out.println("Menu: \n1. Member Display\n2. Trainer Display\n3. Administrative Staff Display\n4. Quit");
                    System.out.println("---------------------------------------------------------------");
                    String displayChoice = scanner.nextLine();

                    switch (displayChoice.toLowerCase()) {
                        case "1":
                            Member member = new Member();
                            member.memberDisplay();
                            break;
                        case "2":
                            Trainer trainer = new Trainer();
                            trainer.trainerDisplay();
                            break;
                        case "3":
                            Administration admin = new Administration();
                            admin.administrationDisplay();
                            break;
                        case "4":
                        case "quit":
                            System.out.println("Closing...");
                            running = false;
                            break;
                        default:
                            System.out.println("Invalid choice. Please enter a number between 1 and 4, or 'quit' to exit.");
                    }
                }
            } else {
                System.out.println("Failed to establish connection.");
            }
            conn.close();
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }
}
