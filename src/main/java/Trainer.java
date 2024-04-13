import java.sql.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Trainer {

    public void newTrainer(int memberID, String username, String specialization, String schedule, Date hire_date){
        String insertSQL = "INSERT INTO Trainers (trainerID, username, specialization, schedule, hire_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(insertSQL)) {
            pstmt.setInt(1, memberID); // Set the memberID
            pstmt.setString(2, username);
            pstmt.setString(3, specialization);
            pstmt.setString(4, schedule.toString());
            pstmt.setDate(5, hire_date);
            pstmt.executeUpdate();
            System.out.println(username + " has been added as a Trainer and schedule made.");
        } catch (SQLException e) {
            System.out.println("Failed insert.");
            e.printStackTrace();
        }
    }

    public void createTrainerSchedule(String username, String specialization, Main.Days schedule, Date hire_date) {
        Member member = new Member();

        Scanner s = new Scanner(System.in);
        System.out.println("First Name? ");
        String firstname = s.nextLine();
        System.out.println("Last Name? ");
        String lastname = s.nextLine();
        System.out.println("Password? ");
        String password = s.nextLine();
        System.out.println("Fitness Goal? ");
        String fitnessGoal = s.nextLine();
        System.out.println("Enrollment Date? (YYYY-MM-DD) format. ");
        String date = s.nextLine();
        LocalDate localDate = LocalDate.parse(date);
        Date enrollmentDate = java.sql.Date.valueOf(localDate);
        System.out.println("Weight? ");
        float weight = s.nextFloat();
        member.userRegistration(firstname, lastname, username, password, fitnessGoal, weight, enrollmentDate);

        int memberID = member.getMemberID();

        String insertSQL = "INSERT INTO Trainers (trainerID, username, specialization, schedule, hire_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(insertSQL)) {
            pstmt.setInt(1, memberID);
            pstmt.setString(2, username);
            pstmt.setString(3, specialization);
            pstmt.setString(4, schedule.toString());
            pstmt.setDate(5, hire_date);
            pstmt.executeUpdate();
            System.out.println(username + " has been added as a Trainer and schedule made.");
        } catch (SQLException e) {
            System.out.println("Failed insert.");
            e.printStackTrace();
        }
    }


    public void updateTrainerSchedule(int trainerID, String schedule){
        String insertSQL = "UPDATE Trainers SET schedule = ? WHERE trainerid = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, schedule);
            pstmt.setInt(2, trainerID);
            int scheduleUpdated = pstmt.executeUpdate();
            if (scheduleUpdated > 0){
                System.out.println("Trainer: " + trainerID + " schedule has been updated.");
            }
        } catch (SQLException e) {
            System.out.println("Failed update.");
            e.printStackTrace();
        }
    }


    public ArrayList<String> getTrainers() {
        ArrayList<String> trainers = new ArrayList<>();
        String selectSQL = "SELECT * FROM Trainers";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(selectSQL)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int trainerID = rs.getInt("trainerID");
                String username = rs.getString("username");
                String specialization = rs.getString("specialization");
                String schedule = rs.getString("schedule");

                trainers.add("Trainer ID: " + trainerID + ", Username: " + username + ", Specialization: " + specialization + ", Schedule: " + schedule);
            }
            return trainers;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trainers;
    }

    public String viewMemberProfile(String username){
        Member searchMember = new Member();
        return searchMember.getMemberInfo(username);
    }

    public String getTrainerSchedule(int trainerID){
        String SQL = "SELECT schedule FROM Trainers WHERE trainerID = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(SQL)) {
            pstmt.setInt(1, trainerID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("schedule");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching exercise routines: " + e.getMessage());
        }
        return null;
    }

    public boolean isTrainer(int trainerID){
        String SQL = "SELECT * FROM Trainers WHERE trainerID = ? LIMIT 1";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(SQL)) {
            pstmt.setInt(1, trainerID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateTrainerUsername(int trainerID, String username){
        String SQL = "UPDATE Trainers SET username = ? WHERE trainerID = ? ";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(SQL)){
            pstmt.setString(1, username);
            pstmt.setInt(2, trainerID);
            int scheduleUpdated = pstmt.executeUpdate();
            if (scheduleUpdated > 0){
                System.out.println("Trainer: " + trainerID + " username has been updated.");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void trainerToAdmin(int trainerID, String username){
        String insertSQL = "INSERT INTO Admins (adminid, username) VALUES (?, ?)";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(insertSQL)){
            pstmt.setInt(1, trainerID);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void trainerDisplay() {
        Scanner scanner = new Scanner(System.in);
        boolean validChoice = false;
        while (!validChoice) {
            System.out.println("Now in Trainer Display, Menu:\n1. Schedule Management\n2. Member Profile Viewing\n3. Back");
            System.out.println("---------------------------------------------------------------");
            String displayChoice = scanner.nextLine();

            try {
                int choice = Integer.parseInt(displayChoice);
                validChoice = true;
                switch (choice) {
                    case 1:
                        System.out.println("1. Update Schedule\n2. New Trainer Schedule ");
                        String updateOrCreate = scanner.nextLine();
                        System.out.println("What is your schedule? (Example: Monday): ");
                        String scheduleString = scanner.nextLine();
                        Main.Days schedule = Main.Days.valueOf(scheduleString.toUpperCase());
                        if (Objects.equals(updateOrCreate, "1")){
                            System.out.println("What is your trainer ID? ");
                            int trainerID = scanner.nextInt();
                            updateTrainerSchedule(trainerID, schedule.toString());
                        } else if (Objects.equals(updateOrCreate, "2")){
                            System.out.println("What will be your username? ");
                            String username = scanner.nextLine();
                            System.out.println("What will be your specialization? ");
                            String specialization = scanner.nextLine();
                            System.out.println("Hire Date? (YYYY-MM-DD) format. ");
                            String date = scanner.nextLine();
                            LocalDate localDate = LocalDate.parse(date);
                            Date hire_date = java.sql.Date.valueOf(localDate);
                            createTrainerSchedule(username, specialization, schedule, hire_date);
                        }
                        break;
                    case 2:
                        System.out.println("What is the username of the member you wish to look for? ");
                        String username = scanner.nextLine();
//                        viewMemberProfile(username);
                        break;
                    case 3:
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

}
