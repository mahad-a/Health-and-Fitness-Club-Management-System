import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Member {
    public Member() {
    }

    public int getUserID(String username) {
        String selectSQL = "SELECT memberID FROM Members WHERE username = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(selectSQL)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("memberID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public String getUsername(int userID){
        String selectSQL = "SELECT username FROM Members WHERE memberid = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(selectSQL)) {
            pstmt.setInt(1, userID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
    public boolean logIn(String username, String password){
        String selectSQL = "SELECT * FROM Members WHERE username = ? AND passkey = ? LIMIT 1";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(selectSQL)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getMemberInfo(String member){
        String selectSQL = "SELECT memberID, first_name, last_name, username, fitness_goal, weight, enrollment_date FROM Members WHERE username = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(selectSQL)) {
            pstmt.setString(1, member);
            ResultSet rs = pstmt.executeQuery();
            String info = " ";

            if (rs.next()) {
                int memberID = rs.getInt("memberID");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String fitnessGoal = rs.getString("fitness_goal");
                float weight = rs.getFloat("weight");
                Date enrollmentDate = rs.getDate("enrollment_date");
                info = "Member ID: " + memberID + ", First Name: " + firstName + ", Last Name: " + lastName + ", Fitness Goal: " + fitnessGoal + ", Weight: " + weight + ", Enrollment Date: " + enrollmentDate;
            } else {
                System.out.println("No member found with the username: " + member);
            }
            return info;
        } catch (SQLException e) {
            System.out.println("Database fully written.");
        }
        return " ";
    }

    public int getMemberID(){
        try {
            Statement stmt = Main.conn.createStatement();
            String selectSQL = "SELECT memberID FROM Members";
            ResultSet rs = stmt.executeQuery(selectSQL);
            if (rs.next()) {
                do {
                    int memberID = rs.getInt("memberID");
                    System.out.println("Member ID: " + memberID);
                    return memberID;
                } while (rs.next());
            }
        } catch (SQLException e) {
            System.out.println("Database fully written.");
        }
        return 0;
    }

    public ArrayList<String> getMembersNames() {
        ArrayList<String> members = new ArrayList<>();
        String selectSQL = "SELECT * FROM Members";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(selectSQL)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int trainerID = rs.getInt("memberid");
                String username = rs.getString("username");

                members.add("Member ID: " + trainerID + ", Username: " + username);
            }
            return members;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    public void userRegistration(String firstname, String lastname, String username, String password, String fitnessGoal, float weight, Date enrollmentDate) {
        String insertSQL = "INSERT INTO Members (first_name, last_name, username, passkey, fitness_goal, weight, enrollment_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, firstname);
            pstmt.setString(2, lastname);
            pstmt.setString(3, username);
            pstmt.setString(4, password);
            pstmt.setString(5, fitnessGoal);
            pstmt.setFloat(6, weight);
            pstmt.setDate(7, enrollmentDate);
            pstmt.executeUpdate();
            System.out.println("New Member Registered! Welcome " + username + "!");
        } catch (SQLException e) {
            System.out.println("Member already exists.");
        }
    }

    public void updateUsername(int member_id, String username) {
        String updateInfoSQL = "UPDATE Members SET username = ? WHERE memberid = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(updateInfoSQL)) {
            pstmt.setString(1, username);
            pstmt.setInt(2, member_id);
            int usernameUpdated = pstmt.executeUpdate();
            if (usernameUpdated > 0){
                System.out.println("Member: " + member_id + " has been successfully updated.");

            } else {
                System.out.println("No member with that member id can be found in the database.");
            }
        } catch (SQLException e){
            System.out.println("error");
        }
    }

    public void updatePassword(int member_id, String password) {
        String updateInfoSQL = "UPDATE Members SET passkey = ? WHERE memberid = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(updateInfoSQL)) {
            pstmt.setString(1, password);
            pstmt.setInt(2, member_id);
            int passwordUpdated = pstmt.executeUpdate();
            if (passwordUpdated > 0){
                System.out.println("Member: " + member_id + " has been successfully updated.");
            } else {
                System.out.println("No member with that member id can be found in the database.");
            }
        } catch (SQLException e){
            System.out.println("error");
        }
    }

    public void updateWeight(int member_id, float weight) {
        String updateInfoSQL = "UPDATE Members SET weight = ? WHERE memberid = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(updateInfoSQL)) {
            pstmt.setFloat(1, weight);
            pstmt.setInt(2, member_id);
            int weightUpdated = pstmt.executeUpdate();
            if (weightUpdated > 0){
                System.out.println("Member: " + member_id + " has been successfully updated.");
            } else {
                System.out.println("No member with that member id can be found in the database.");
            }
        } catch (SQLException e){
            System.out.println("error");
        }
    }

    public void updateFitnessGoals(int member_id, String fitnessGoal) {
        String updateInfoSQL = "UPDATE Members SET fitness_goal = ? WHERE memberid = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(updateInfoSQL)) {
            pstmt.setString(1, fitnessGoal);
            pstmt.setInt(2, member_id);
            int emailUpdated = pstmt.executeUpdate();
            if (emailUpdated > 0){
                System.out.println("member: " + member_id + " has successfully updated fitness goal.");
            } else {
                System.out.println("No member with that member id can be found in the database.");
            }
        } catch (SQLException e){
            System.out.println("error");
        }
    }

    public void updatePersonalInfo(int userID) {
        Scanner scanner = new Scanner(System.in);
        boolean validChoice = false;
        while (!validChoice) {
            System.out.println("Personal Information:\n1. Update Username\n2. Update Password");
            System.out.println("What information do you want to update? ");
            try {
                int updateInfo = Integer.parseInt(scanner.nextLine());
                if (updateInfo < 1 || updateInfo > 2) {
                    System.out.println("Invalid choice. Please enter 1 or 2.");
                    continue;
                }

                System.out.println("What is the new " + (updateInfo == 1 ? "username" : "password") + "? ");
                String newData = scanner.nextLine();

                switch (updateInfo) {
                    case 1:
                        updateUsername(userID, newData);
                        break;
                    case 2:
                        updatePassword(userID, newData);
                        break;
                }
                validChoice = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    public ArrayList<String> viewExerciseRoutines(int memberID){
        ArrayList<String> routines = new ArrayList<>();
        String selectSQL = "SELECT routine_name, duration, quantity, exercise_date, routineid FROM ExerciseRoutines WHERE memberID = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(selectSQL)) {
            pstmt.setInt(1, memberID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String routineName = rs.getString("routine_name");
                int duration = rs.getInt("duration");
                int quantity = rs.getInt("quantity");
                Date exerciseDate = rs.getDate("exercise_date");
                int routineID = rs.getInt("routineid");
                routines.add("RoutineID: " + routineID + "Routine Name: " + routineName + ", Duration: " + duration + " minutes, Quantity: " + quantity + ", Exercise Date: " + exerciseDate);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching exercise routines: " + e.getMessage());
        }
        return routines;
    }

    public void addExerciseRoutine(int memberID, String routineName, int duration, int quantity, Date exerciseDate) {
        String SQL = "INSERT INTO ExerciseRoutines (memberID, routine_name, duration, quantity, exercise_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(SQL)) {
            pstmt.setInt(1, memberID);
            pstmt.setString(2, routineName);
            pstmt.setInt(3, duration);
            pstmt.setInt(4, quantity);
            pstmt.setDate(5, exerciseDate);
            pstmt.executeUpdate();
            System.out.println("New exercise routine added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding exercise routine: " + e.getMessage());
        }
    }

    public void removeExerciseRoutine(int routineID, int userID) {
        String SQL = "DELETE FROM ExerciseRoutines WHERE routineID = ? AND memberid = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(SQL)) {
            pstmt.setInt(1, routineID);
            pstmt.setInt(2, userID);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Exercise routine removed successfully.");
            } else {
                System.out.println("No exercise routine found with ID " + routineID);
            }
        } catch (SQLException e) {
            System.out.println("Error removing exercise routine: " + e.getMessage());
        }
    }

    public ArrayList<String> viewFitnessAchievements(int memberID) {
        ArrayList<String> achievements = new ArrayList<>();
        String selectSQL = "SELECT * FROM FitnessAchievements WHERE memberID = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(selectSQL)) {
            pstmt.setInt(1, memberID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int achievementID = rs.getInt("achievementID");
                String achievementInfo = rs.getString("achievement_info");
                Date achievementDate = rs.getDate("achievement_date");
                achievements.add("Achievement ID: " + achievementID + ", Achievement Info: " + achievementInfo + ", Achievement Date: " + achievementDate);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return achievements;
    }

    public void addFitnessAchievement(int memberID, String achievementInfo, Date achievementDate) {
        String insertSQL = "INSERT INTO FitnessAchievements (memberID, achievement_info, achievement_date) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(insertSQL)) {
            pstmt.setInt(1, memberID);
            pstmt.setString(2, achievementInfo);
            pstmt.setDate(3, achievementDate);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeFitnessAchievement(int achievementID) {
        String deleteSQL = "DELETE FROM FitnessAchievements WHERE achievementID = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, achievementID);
            int achievementDeleted = pstmt.executeUpdate();
            if (achievementDeleted > 0) {
                System.out.println("Achievement removed successfully.");
            } else {
                System.out.println("No achievement found with ID " + achievementID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> viewHealthStatistics(int memberID) {
        ArrayList<String> health = new ArrayList<>();
        String selectSQL = "SELECT * FROM HealthStatistics WHERE memberID = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(selectSQL)) {
            pstmt.setInt(1, memberID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int healthID = rs.getInt("healthID");
                String stat = rs.getString("stat");
                health.add("Health ID: " + healthID + ", Statistic: " + stat);
            }
            return health;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return health;
    }

    public void addHealthStatistic(int memberID, String statistic) {
        String insertSQL = "INSERT INTO HealthStatistics (memberID, stat) VALUES (?, ?)";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(insertSQL)) {
            pstmt.setInt(1, memberID);
            pstmt.setString(2, statistic);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeHealthStatistic(int memberID, int healthID) {
        String deleteSQL = "DELETE FROM HealthStatistics WHERE memberID = ? AND healthID = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, memberID);
            pstmt.setInt(2, healthID);
            int healthDel = pstmt.executeUpdate();
            if (healthDel > 0) {
                System.out.println("Health statistic removed successfully.");
            } else {
                System.out.println("No health statistic found for the given member ID and health ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public String getPaymentType(int memberID) {
        String paymentType = null;
        String selectSQL = "SELECT payment_type FROM Payment WHERE member_id = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(selectSQL)) {
            pstmt.setInt(1, memberID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                paymentType = rs.getString("payment_type");
            } else {
                System.out.println("No payment found for member with ID: " + memberID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paymentType;
    }

    public String bookPrivateSession(int trainerID) {
        try {
            Trainer trainer = new Trainer();
            Administration admin = new Administration();
            String[] roomInfo = admin.getTrainerRoom(trainerID);
            if (Objects.equals(roomInfo[1].toUpperCase(), trainer.getTrainerSchedule(trainerID))) {
                Main.Days bookingDate = Main.Days.valueOf(roomInfo[1].toUpperCase());
                admin.bookRoom(Integer.parseInt(roomInfo[0]), bookingDate, Main.Times.valueOf(roomInfo[2].toUpperCase()), trainerID, "Private Session");
                return ("Booking confirmed!");
            } else {
                return ("Booking cancelled. Trainer is not available.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        return ("Booking cancelled.");
    }

    public String[] getRoomInfo(int roomNumber){
        String[] roomInfo = new String[2];
        String selectSQL = "SELECT * FROM RoomBookings WHERE roomnumber is ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(selectSQL)){
            pstmt.setInt(1, roomNumber);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                roomInfo[0] = rs.getString("booking_date");
                roomInfo[1] = rs.getString("time_block");
                return roomInfo;
            }
        } catch (SQLException e){
            System.out.println("Failed");
        }
        return roomInfo;
    }

    public ArrayList<Integer> displayAvailableRooms() {
        try {
            ArrayList<Integer> roomNumbers = new ArrayList<Integer>();
            String selectSQL = "SELECT roomNumber, booking_date, time_block FROM RoomBookings WHERE booked_by IS NULL";
            try (PreparedStatement pstmt = Main.conn.prepareStatement(selectSQL)) {
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    roomNumbers.add(rs.getInt("roomNumber"));
                }
                return roomNumbers;
            } catch (SQLException e) {
                System.out.println("Failed to retrieve available rooms.");
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        return null;
    }

    public ArrayList<String> getClassSchedules() {
        ArrayList<String> schedules = new ArrayList<>();
        String selectSQL = "SELECT classID, className, booking_date, time_block, class_list FROM Classes";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(selectSQL)){
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int classID = rs.getInt("classID");
                String className = rs.getString("className");
                String bookingDate = rs.getString("booking_date");
                String timeBlock = rs.getString("time_block");
                String classList = rs.getString("class_list");
                schedules.add("Class ID: " + classID + ", Class Name: " + className + ", Booking Date: " + bookingDate + ", Time Block: " + timeBlock + ", Class List: " + classList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedules;
    }

    public String getClassList(String className) {
        String classList = "";
        String selectSQL = "SELECT class_list FROM Classes WHERE className = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(selectSQL)) {
            pstmt.setString(1, className.toLowerCase());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                classList = rs.getString("class_list");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classList;
    }

    public void updateClassList(String className, String classList) {
        String updateSQL = "UPDATE Classes SET class_list = ? WHERE className = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, classList);
            pstmt.setString(2, className);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void profileManagement() {
        Scanner scanner = new Scanner(System.in);
        boolean validChoice = false;

        while (!validChoice) {
            System.out.println("Profile Management Section:\n1. Update Personal Information\n2. Update Fitness Goals\n3. Update Weight");
            System.out.println("Select an option: ");

            try {
                int profileChange = Integer.parseInt(scanner.nextLine());

                if (profileChange < 1 || profileChange > 3) {
                    System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                    continue;
                }

                System.out.println("User ID? ");
                int userID = Integer.parseInt(scanner.nextLine());

                switch (profileChange) {
                    case 1:
                        updatePersonalInfo(userID);
                        break;
                    case 2:
                        System.out.println("What is the new fitness goal? ");
                        String goal = scanner.nextLine();
                        updateFitnessGoals(userID, goal);
                        break;
                    case 3:
                        System.out.println("What is the new weight? ");
                        float weight = Float.parseFloat(scanner.nextLine());
                        updateWeight(userID, weight);
                        break;
                }

                validChoice = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    public void dashboardDisplay(){
        Scanner scanner = new Scanner(System.in);
        boolean validChoice = false;
        while(!validChoice) {
            System.out.println("Dashboard Management Section:\n1. View Exercise Routines\n2. View Fitness Achievements\n3. View Health Statistics");
            System.out.println("Select an option: ");

            try {
                int dashboardSelect = Integer.parseInt(scanner.nextLine());

                if (dashboardSelect < 1 || dashboardSelect > 3) {
                    System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                    continue;
                }

                System.out.println("User ID? ");
                int userID = Integer.parseInt(scanner.nextLine());

                switch (dashboardSelect) {
                    case 1:
                        System.out.println("1. View Current Exercise Routines\n2. Add New Exercise Routine\n3. Remove Exercise Routine");
                        String routineChoice = scanner.nextLine();
                        System.out.println("What is your member ID? ");
                        int memberID = scanner.nextInt();
                        if (Objects.equals(routineChoice, "1")){
                            ArrayList<String> x = viewExerciseRoutines(memberID);
                        } else if (Objects.equals(routineChoice, "2")){
                            System.out.println("Enter Routine Name: ");
                            String routineName = scanner.nextLine();
                            System.out.println("Enter Duration (in minutes): ");
                            int duration = scanner.nextInt();
                            System.out.println("Enter Quantity: ");
                            int quantity = scanner.nextInt();
                            scanner.nextLine(); // Consume newline
                            System.out.println("Enter Exercise Date (YYYY-MM-DD): ");
                            String dateStr = scanner.nextLine();
                            Date exerciseDate = Date.valueOf(dateStr);
                            addExerciseRoutine(memberID, routineName, duration, quantity, exerciseDate);
                        } //else if (Objects.equals(routineChoice, "3")){
//                            removeExerciseRoutine(memberID);
//                        }
                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                }

                validChoice = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    public void scheduleDisplay(){
        Scanner scanner = new Scanner(System.in);
        boolean validChoice = false;
        while(!validChoice) {
            System.out.println("Schedule Management Section:\n1. Book a Private Session\n2. Enroll in Fitness Class");
            System.out.println("Select an option: ");

            try {
                int scheduleSelect = Integer.parseInt(scanner.nextLine());

                if (scheduleSelect < 1 || scheduleSelect > 2) {
                    System.out.println("Invalid choice. Please enter a number between 1 or 2.");
                    continue;
                }
                switch (scheduleSelect) {
                    case 1:
                        System.out.println("What is the ID of the trainer you wish to book with? ");
                        int trainerID = Integer.parseInt(scanner.nextLine());
//                        bookPrivateSession(trainerID);
                        break;
                    case 2:
                        break;
                }

                validChoice = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    public void memberDisplay() {
        Scanner scanner = new Scanner(System.in);
        boolean validChoice = false;
        while (!validChoice) {
            System.out.println("Now in Member Display, Menu:\n1. User Registration\n2. Profile Management\n3. Dashboard Display\n4. Schedule Management\n5. Back");
//            System.out.println("Select an option: ");
            System.out.println("---------------------------------------------------------------");
            String displayChoice = scanner.nextLine();

            try {
                int choice = Integer.parseInt(displayChoice);
                validChoice = true;

                switch (choice) {
                    case 1:
                        Scanner s = new Scanner(System.in);
                        System.out.println("First Name? ");
                        String firstname = s.nextLine();
                        System.out.println("Last Name? ");
                        String lastname = s.nextLine();
                        System.out.println("Username? ");
                        String username = s.nextLine();
                        System.out.println("Password? ");
                        String password = s.nextLine();
                        System.out.println("Fitness Goal? ");
                        String fitnessGoal = s.nextLine();

                        System.out.println("Enrollment Date? (YYYY-MM-DD) format. ");
                        String date = s.nextLine();
                        LocalDate localDate = LocalDate.parse(date);
                        Date enrollementDate = java.sql.Date.valueOf(localDate);
                        System.out.println("Weight? ");
                        float weight = s.nextFloat();
                        userRegistration(firstname,lastname, username, password, fitnessGoal, weight, enrollementDate);
                        break;
                    case 2:
                        profileManagement();
                        break;
                    case 3:
                        dashboardDisplay();
                        break;
                    case 4:
                        System.out.println("Schedule Management is not implemented yet.");
//                        scheduleManagement();
                        break;
                    case 5:
                        break; // go back
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}