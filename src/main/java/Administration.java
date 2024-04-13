import java.sql.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
public class Administration {

    public Administration() {
    }

    public boolean isAdmin(int adminID){
        String SQL = "SELECT * FROM Admins WHERE adminID = ? LIMIT 1";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(SQL)) {
            pstmt.setInt(1, adminID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public ArrayList<String> equipmentMonitoring() {
        ArrayList<String> equipments = new ArrayList<>();
        String selectSQL = "SELECT * FROM Equipment";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(selectSQL)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("equipmentid");
                String equipmentName = rs.getString("equipment_name");
                int quantity = rs.getInt("quantity");
                Date lastMaintenanceDate = rs.getDate("last_maintenance_date");
                equipments.add("Equipment ID: " + id + "\nEquipment Name: " + equipmentName + "\nQuantity: " + quantity + "\nLast Maintenance Date: " + lastMaintenanceDate + "\n");
            }
            return equipments;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return equipments;
    }


    public void updateQuantity(int equipmentID, int newQuantity) {
        try {
            String updateSQL = "UPDATE Equipment SET quantity = ? WHERE equipmentID = ?";
            try (PreparedStatement pstmt = Main.conn.prepareStatement(updateSQL)) {
                pstmt.setInt(1, newQuantity);
                pstmt.setInt(2, equipmentID);
                int updatedQuantity = pstmt.executeUpdate();

                if (updatedQuantity > 0) {
                    System.out.println("Quantity updated for equipment: " + equipmentID);
                } else {
                    System.out.println("No equipment found with the name: " + equipmentID);
                }
            }
        } catch (SQLException e) {
            // Handle SQL exceptions
            e.printStackTrace();
        }
    }

    public void updateMaintenanceDate(int equipmentID, Date maintenanceDate) {

        String updateSQL = "UPDATE Equipment SET last_maintenance_date = ? WHERE equipmentID = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(updateSQL)) {
            pstmt.setDate(1, maintenanceDate);
            pstmt.setInt(2, equipmentID);
            int updatedMaintenance = pstmt.executeUpdate();

            if (updatedMaintenance > 0) {
                System.out.println("Maintenance date updated for equipment: " + equipmentID);
            } else {
                System.out.println("No equipment found with the name: " + equipmentID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addEquipment(String equipmentName, int quantity, Date maintenanceDate) {
        String insertSQL = "INSERT INTO Equipment (equipment_name, quantity, last_maintenance_date) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, equipmentName);
            pstmt.setInt(2, quantity);
            pstmt.setDate(3, maintenanceDate);
            pstmt.executeUpdate();
            System.out.println("New equipment added: " + equipmentName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeEquipment(int equipmentID) {
        String deleteSQL = "DELETE FROM Equipment WHERE equipmentID = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, equipmentID);
            int equipmentRemoved = pstmt.executeUpdate();
            if (equipmentRemoved > 0) {
                System.out.println("Equipment removed with ID: " + equipmentID);
            } else {
                System.out.println("No equipment found with ID: " + equipmentID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void equipmentMaintenanceDisplay(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the equipment name: ");
        int equipmentID = scanner.nextInt();
        System.out.println("1. View Current Equipment\n2. Update Maintenance Date\n3. Update Quantity\n4. Add New Equipment\n5. Remove Equipment");
        int equipChoice = scanner.nextInt();
        switch (equipChoice){
            case 1:
                equipmentMonitoring();
                break;
            case 2:
                System.out.println("Enter the new maintenance date (YYYY-MM-DD): ");
                String date = scanner.nextLine();
                LocalDate localDate = LocalDate.parse(date);
                Date maintenanceDate = java.sql.Date.valueOf(localDate);
                updateMaintenanceDate(equipmentID, maintenanceDate);
                break;
            case 3:
                System.out.println("Enter new quantity: ");
                int newQuantity = scanner.nextInt();
                updateQuantity(equipmentID, newQuantity);
                break;
            case 4:
                System.out.println("Enter name of the new equipment: ");
                String equipmentName = scanner.nextLine();
                System.out.println("Enter the quantity of equipment: ");
                int quantity = scanner.nextInt();
                System.out.println("Enter the last maintenance date (YYYY-MM-DD): ");
                String lastDate = scanner.nextLine();
                LocalDate lastLocalDate = LocalDate.parse(lastDate);
                Date newMaintenanceDate = java.sql.Date.valueOf(lastLocalDate);
                updateMaintenanceDate(equipmentID, newMaintenanceDate);
                addEquipment(equipmentName, quantity, newMaintenanceDate);
                break;
            case 5:
                removeEquipment(equipmentID);
                break;
        }
        equipmentMonitoring();
    }

    public String[] getTrainerRoom(int trainerID) {
        String[] roomInfo = new String[3];
        String selectSQL = "SELECT * FROM RoomBookings WHERE booked_by = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(selectSQL)) {
            pstmt.setInt(1, trainerID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int roomNumber = rs.getInt("roomNumber");
                String bookingDate = rs.getString("booking_date");
                String timeBlock = rs.getString("time_block");
                roomInfo[0] = String.valueOf(roomNumber);
                roomInfo[1] = bookingDate;
                roomInfo[2] = timeBlock;
                return roomInfo;
//                return "Room Number: " + roomNumber + ", Booking Date: " + bookingDate + ", Time Block: " + timeBlock + ", Booked By: " + trainerID;
            } else {
                return roomInfo;
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve room booking.");
            e.printStackTrace();
            return null;
        }
    }

//

    public String bookRoom(int roomNumber, Main.Days bookingDate, Main.Times timeBlock, int trainerID, String session){
        String insertSQL = "INSERT INTO RoomBookings (roomNumber, booking_date, time_block, booked_by, session) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(insertSQL)) {
            pstmt.setInt(1, roomNumber);
            pstmt.setString(2, bookingDate.toString());
            pstmt.setString(3, timeBlock.toString());
            pstmt.setInt(4, trainerID);
            pstmt.setString(5, session);
            pstmt.executeUpdate();
            System.out.println("Booking confirmed!");
            return ("Room Number: " + roomNumber + "Time Block: " + bookingDate + "Trainer ID: " + timeBlock + "Session: " + session);
        } catch (SQLException e) {
            System.out.println("Failed to book the session.");
            e.printStackTrace();
        }
        return " ";
    }

    public void updateRoomBooking(int roomNumber, Main.Days newBookingDate, Main.Times newTimeBlock, int bookedBy) {
        String updateSQL = "UPDATE RoomBookings SET booking_date = ?, time_block = ?, booked_by = ? WHERE roomNumber = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, newBookingDate.toString());
            pstmt.setString(2, newTimeBlock.toString());
            pstmt.setInt(3, bookedBy);
            pstmt.setInt(4, roomNumber);
            int updatedRooms = pstmt.executeUpdate();
            if (updatedRooms > 0) {
                System.out.println("Room booking updated successfully.");
            } else {
                System.out.println("No room booking found with room number: " + roomNumber);
            }
        } catch (SQLException e) {
            System.out.println("Error updating room booking: " + e.getMessage());
        }
    }

    public HashMap<Integer, ArrayList<String>> getAllRoomBookings() {
        HashMap<Integer, ArrayList<String>> roomBookings = new HashMap<>();
        String selectSQL = "SELECT * FROM RoomBookings";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(selectSQL)){
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int roomNumber = rs.getInt("roomNumber");
                if (roomBookings.containsKey(roomNumber)){
                    roomBookings.get(roomNumber).add(rs.getString("booking_date"));
                    roomBookings.get(roomNumber).add(rs.getString("time_block"));
                    roomBookings.get(roomNumber).add(rs.getString("booked_by"));
                } else {
                    ArrayList<String> bookingInfo = new ArrayList<>();
                    bookingInfo.add(rs.getString("booking_date"));
                    bookingInfo.add(rs.getString("time_block"));
                    bookingInfo.add(rs.getString("booked_by"));
                    roomBookings.put(roomNumber, bookingInfo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomBookings;
    }

    public HashMap<Integer, ArrayList<String>> getAllClasses() {
        HashMap<Integer, ArrayList<String>> classBookings = new HashMap<>();
        String selectSQL = "SELECT * FROM Classes";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(selectSQL)){
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int classID = rs.getInt("classID");
                ArrayList<String> classInfo = new ArrayList<>();
                classInfo.add(rs.getString("className"));
                classInfo.add(rs.getString("booking_date"));
                classInfo.add(rs.getString("time_block"));
                classInfo.add(rs.getString("class_list"));
                classBookings.put(classID, classInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classBookings;
    }

    public void updateClass(int classID, String className, Main.Days booking_date, Main.Times time_block, String class_list) {
        String updateSQL = "UPDATE Classes SET className = ?, booking_date = ?, time_block = ?, class_list = ? WHERE classID = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, className);
            pstmt.setString(2, booking_date.toString().toUpperCase());
            pstmt.setString(3, time_block.toString().toUpperCase());
            pstmt.setString(4, class_list);
            pstmt.setInt(5, classID);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Class updated successfully.");
            } else {
                System.out.println("No class found with ID: " + classID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addClass(String className, int trainerID, Main.Days booking_date, Main.Times time_block, String class_list) {
        String insertSQL = "INSERT INTO Classes (className, trainerID, booking_date, time_block, class_list) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, className);
            pstmt.setInt(2, trainerID);
            pstmt.setString(3, booking_date.toString().toUpperCase());
            pstmt.setString(4, time_block.toString().toUpperCase());
            pstmt.setString(5, class_list);
            pstmt.executeUpdate();
            System.out.println("New class added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeClass(int classID) {
        String deleteSQL = "DELETE FROM Classes WHERE classID = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, classID);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Class removed successfully.");
            } else {
                System.out.println("No class found with ID: " + classID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getClassList(int classID) {
        String classList = "";
        String selectSQL = "SELECT class_list FROM Classes WHERE classID = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(selectSQL)) {
            pstmt.setInt(1, classID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                classList = rs.getString("class_list");
            } else {
                System.out.println("No class found with ID: " + classID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classList;
    }

    public void setupPayee(String paymentType, String username) {
        String insertSQL = "INSERT INTO Payment (amount, payment_date, payment_type, member_id) VALUES (?, ?, ?, ?)";
        Member member = new Member();
        try (PreparedStatement pstmt = Main.conn.prepareStatement(insertSQL)) {
            pstmt.setFloat(1, 0);
            pstmt.setDate(2, null);
            pstmt.setString(3, paymentType);
            pstmt.setInt(4, member.getUserID(username));

            pstmt.executeUpdate();
            System.out.println("Payee setup successfully.");

        } catch (SQLException e) {
            System.out.println("Error setting up payee: " + e.getMessage());
        }
    }

    public void createBill(int memberID, Date paymentDate, float amount, String paymentType) {
        String updateSQL = "UPDATE Payment SET amount = ?, payment_date = ?, payment_type = ? WHERE member_id = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(updateSQL)) {
            pstmt.setFloat(1, amount);
            pstmt.setDate(2, paymentDate);
            pstmt.setString(3, paymentType);
            pstmt.setInt(4, memberID);
            int createdBill = pstmt.executeUpdate();
            if (createdBill > 0) {
                System.out.println("Bill created for member with ID: " + memberID);
            } else {
                System.out.println("No bill found for member: " + memberID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cancelBill(int memberID) {
        String updateSQL = "UPDATE Payment SET amount = ?, payment_date = ? WHERE member_id = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(updateSQL)) {
            pstmt.setFloat(1, 0);
            pstmt.setDate(2, null);
            pstmt.setInt(3, memberID);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Bill canceled for member: " + memberID);
            } else {
                System.out.println("No bill found for member: " + memberID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBill(int memberID, Date date, float newAmount) {
        String updateSQL = "UPDATE Payment SET amount = ?, payment_date = ? WHERE member_id = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(updateSQL)) {
            pstmt.setFloat(1, newAmount);
            pstmt.setDate(2, date);
            pstmt.setInt(3, memberID);
            int billUpdated = pstmt.executeUpdate();
            if (billUpdated > 0) {
                System.out.println("Bill updated for member: " + memberID);
            } else {
                System.out.println("No bill found for member: " + memberID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Date getBillDate(int memberID) {
        String selectSQL = "SELECT payment_date FROM Payment WHERE member_id = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(selectSQL)) {
            pstmt.setInt(1, memberID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDate("payment_date");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public float getBillAmount(int memberID) {
        String selectSQL = "SELECT amount FROM Payment WHERE member_id = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(selectSQL)) {
            pstmt.setInt(1, memberID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getFloat("amount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getBillPaymentType(int memberID) {
        String selectSQL = "SELECT payment_type FROM Payment WHERE member_id = ?";
        try (PreparedStatement pstmt = Main.conn.prepareStatement(selectSQL)) {
            pstmt.setInt(1, memberID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("payment_type");
            } else {
                return "Cash";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void administrationDisplay(){
        Scanner scanner = new Scanner(System.in);
        boolean validChoice = false;
        while (!validChoice) {
            System.out.println("Now in Administration Display, Menu:\n1. Room Booking Management\n2. Equipment Maintenance Monitoring\n3. Class Scheduling Updating\n4. Billing and Payment Processing");
            System.out.println("Select an option: ");
            String displayChoice = scanner.nextLine();

            try {
                int choice = Integer.parseInt(displayChoice);
                validChoice = true;

                switch (choice) {
                    case 1:

                        break;
                    case 2:
                        equipmentMaintenanceDisplay();
                        break;
                    case 3:
                        break;
                    case 4:
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
