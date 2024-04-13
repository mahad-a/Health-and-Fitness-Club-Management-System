import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AdministrationGUI {
    private Member member;
    private Trainer trainer;
    private Administration admin;

    public AdministrationGUI() {
        member = new Member();
        trainer = new Trainer();
        admin = new Administration();
    }

    public void administrationDisplay(JFrame oldFrame, String username) {
        oldFrame.dispose();
        JFrame frame = new JFrame("Administration Display");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel titleLabel = new JLabel("Administration Display!");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        JButton roomBookingButton = new JButton("Room Booking Management");
        roomBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                roomBookingDisplay(frame, username);
            }
        });
        panel.add(roomBookingButton);

        JButton equipmentMaintenanceButton = new JButton("Equipment Maintenance Monitoring");
        equipmentMaintenanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                equipmentMaintenanceDisplay(frame, username);
            }
        });
        panel.add(equipmentMaintenanceButton);

        JButton classSchedulingButton = new JButton("Class Scheduling Updating");
        classSchedulingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                classSchedulingDisplay(frame, username);
            }
        });
        panel.add(classSchedulingButton);

        JButton billingAndPaymentButton = new JButton("Billing and Payment Processing");
        billingAndPaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                billingAndPaymentDisplay(frame, username);
            }
        });
        panel.add(billingAndPaymentButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                GUI.showNextScreen(frame, username, member.getUserID(username));
            }
        });
        panel.add(backButton);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
    }

    public void billingAndPaymentDisplay(JFrame oldFrame, String username) {
        oldFrame.dispose();
        JFrame frame = new JFrame("Billing and Payment Processing");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int id = member.getUserID(username);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        JButton createBillButton = new JButton("Create a Bill");
        createBillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                String memberIDStr = JOptionPane.showInputDialog("What is the member ID you want to bill? ");
                ArrayList<String> members = member.getMembersNames();
                String message = "Enter trainer ID number from list below:\n";
                for (String option: members){
                    message += option + "\n";
                }
                String memberIDStr = JOptionPane.showInputDialog(message);
                Date date = GUI.getDateInput();
                String amountStr = JOptionPane.showInputDialog("Amount to charge?");
                if (memberIDStr != null && !memberIDStr.isEmpty() && amountStr != null && !amountStr.isEmpty() && !date.toString().isEmpty()) {
                    int memberID = Integer.parseInt(memberIDStr);
                    float amount = Float.parseFloat(amountStr);
                    String paymentType = admin.getBillPaymentType(memberID);
                    admin.createBill(memberID, date, amount, paymentType);
                }
            }
        });
        panel.add(createBillButton);

        JButton updateBillButton = new JButton("Update a Bill");
        updateBillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String memberIDStr = JOptionPane.showInputDialog("What is the member ID you want to update their bill?");
                if (memberIDStr != null && !memberIDStr.isEmpty()){
                    int memberID = Integer.parseInt(memberIDStr);
                    String[] optionList = {"Amount", "Due Date"};
                    String selectedOption = (String) JOptionPane.showInputDialog(null, "Select option to update", "Updating Bill", JOptionPane.QUESTION_MESSAGE, null, optionList, optionList[0]);
                    if (Objects.equals(selectedOption, "Amount")){
                        String amountStr = JOptionPane.showInputDialog("Amount to charge?");
                        if (amountStr != null && !amountStr.isEmpty()) {
                            Date date = admin.getBillDate(memberID);
                            admin.updateBill(memberID, date, Float.parseFloat(amountStr));
                        }
                    } else {
                        Date date = GUI.getDateInput();
                        float amount = admin.getBillAmount(memberID);
                        admin.updateBill(memberID, date, amount);
                    }
                }
            }
        });
        panel.add(updateBillButton);

        JButton cancelBillButton = new JButton("Cancel a Bill");
        cancelBillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String memberIDStr = JOptionPane.showInputDialog("What is the member ID you want to cancel their bill? ");
                if (memberIDStr != null && !memberIDStr.isEmpty()) {
                    int memberID = Integer.parseInt(memberIDStr);
                    admin.cancelBill(memberID);
                }
            }
        });
        panel.add(cancelBillButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close current frame
                administrationDisplay(frame, username);
            }
        });
        panel.add(backButton);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    public void classSchedulingDisplay(JFrame oldFrame, String username) {
        oldFrame.dispose();
        JFrame frame = new JFrame("Class Scheduling");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int id = member.getUserID(username);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        JButton viewClassesButton = new JButton("View Current Class Schedules");
        viewClassesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = "";
                HashMap<Integer, ArrayList<String>> classes = admin.getAllClasses();
                for (HashMap.Entry<Integer, ArrayList<String>> entry : classes.entrySet()) {
                    int classNumber = entry.getKey();
                    ArrayList<String> bookingInfo = entry.getValue();
                    message += "Class Number: " + classNumber + "\n"
                            + "Class Name: " + bookingInfo.get(0) + "\n"
                            + "Booking Date: " + bookingInfo.get(1) + "\n"
                            + "Time Block: " + bookingInfo.get(2) + "\n"
                            + "Class List: " + bookingInfo.get(3) + "\n\n";
                }
                JTextArea textArea = new JTextArea();
                textArea.setEditable(false);
                textArea.setLineWrap(true);
                textArea.append(message);
                textArea.setWrapStyleWord(true);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 300));

                JOptionPane.showMessageDialog(null, scrollPane, "Classes", JOptionPane.PLAIN_MESSAGE);
            }
        });
        panel.add(viewClassesButton);

        JButton updateClassButton = new JButton("Update a Class");
        updateClassButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                String classID = JOptionPane.showInputDialog("Enter the class ID you want to update");
                ArrayList<String> classes = member.getClassSchedules();
                String message = "Enter class ID number from list below:\n";
                for (String option: classes){
                    message += option + "\n";
                }
                String classID = JOptionPane.showInputDialog(message);
                String className = JOptionPane.showInputDialog("Enter the class name");
                String bookingDate = JOptionPane.showInputDialog("Enter the booking date (MONDAY, TUESDAY, ETC.)");
                String timeBlock = JOptionPane.showInputDialog("Enter the time of day: (Morning, Afternoon, Evening, Night)");
                if(classID != null && !classID.isEmpty() && className != null && !className.isEmpty() && bookingDate != null && !bookingDate.isEmpty() && timeBlock != null && !timeBlock.isEmpty()) {
                    admin.updateClass(Integer.parseInt(classID), className, Main.Days.valueOf(bookingDate.toUpperCase()), Main.Times.valueOf(timeBlock.toUpperCase()), admin.getClassList(Integer.parseInt(classID)));
                }
            }
        });
        panel.add(updateClassButton);

        JButton addClassButton = new JButton("Add a Class");
        addClassButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                String trainerID = JOptionPane.showInputDialog("Enter the trainer ID to host the class");
                ArrayList<String> trainers = trainer.getTrainers();
                String message = "Enter class ID number from list below:\n";
                for (String option: trainers){
                    message += option + "\n";
                }
                String trainerID = JOptionPane.showInputDialog(message);
                String className = JOptionPane.showInputDialog("Enter the new class name");
                String bookingDate = JOptionPane.showInputDialog("Enter the booking date (MONDAY, TUESDAY, ETC.)");
                String timeBlock = JOptionPane.showInputDialog("Enter the time of day: (Morning, Afternoon, Evening, Night)");
                if(trainerID != null && !trainerID.isEmpty() && className != null && !className.isEmpty() && bookingDate != null && !bookingDate.isEmpty() && timeBlock != null && !timeBlock.isEmpty()) {
                    admin.addClass(className, Integer.parseInt(trainerID), Main.Days.valueOf(bookingDate.toUpperCase()), Main.Times.valueOf(timeBlock.toUpperCase()), " ");
                }
            }
        });
        panel.add(addClassButton);

        JButton removeClassButton = new JButton("Remove a Class");
        removeClassButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> classes = member.getClassSchedules();
                String message = "Enter class ID number from list below:\n";
                for (String option: classes){
                    message += option + "\n";
                }
                String classID = JOptionPane.showInputDialog(message);
                if (classID != null && !classID.isEmpty()){
                    admin.removeClass(Integer.parseInt(classID));
                }
            }
        });
        panel.add(removeClassButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close current frame
                administrationDisplay(frame, username);
            }
        });
        panel.add(backButton);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    public void equipmentMaintenanceDisplay(JFrame oldFrame, String username) {
        oldFrame.dispose();
        JFrame frame = new JFrame("Equipment Maintenance Monitoring");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int id = member.getUserID(username);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));

        JButton viewEquipmentButton = new JButton("View Current Equipment");
        viewEquipmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = (admin.equipmentMonitoring()).toString();
                JTextArea textArea = new JTextArea();
                textArea.setEditable(false);
                textArea.setLineWrap(true);
                textArea.append(message);
                textArea.setWrapStyleWord(true);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 300));

                JOptionPane.showMessageDialog(null, scrollPane, "Room Bookings", JOptionPane.PLAIN_MESSAGE);
            }
        });
        panel.add(viewEquipmentButton);

        JButton updateQuantityButton = new JButton("Update Quantity");
        updateQuantityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String quantityStr = JOptionPane.showInputDialog("Enter the new quantity");
//                String equipmentIDStr = JOptionPane.showInputDialog("Enter equipment ID");
                ArrayList<String> equipments = admin.equipmentMonitoring();
                String message = "Enter equipment ID number from list below:\n";
                for (String option: equipments){
                    message += option + "\n";
                }
                String equipmentIDStr = JOptionPane.showInputDialog(message);
                if (quantityStr != null && !quantityStr.isEmpty() && equipmentIDStr != null && !equipmentIDStr.isEmpty()){
                    int equipmentID = Integer.parseInt(equipmentIDStr);
                    int quantity = Integer.parseInt(quantityStr);
                    admin.updateQuantity(equipmentID, quantity);
                }
            }
        });
        panel.add(updateQuantityButton);

        JButton updateMaintenanceDateButton = new JButton("Update Maintenance Date");
        updateMaintenanceDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> equipments = admin.equipmentMonitoring();
                String message = "Enter equipment ID number from list below:\n";
                for (String option: equipments){
                    message += option + "\n";
                }
                String equipmentIDStr = JOptionPane.showInputDialog(message);
                Date maintenceDate = GUI.getDateInput();
                admin.updateMaintenanceDate(Integer.parseInt(equipmentIDStr), maintenceDate);
            }
        });
        panel.add(updateMaintenanceDateButton);

        JButton addEquipmentButton = new JButton("Add Equipment");
        addEquipmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newEquipment = JOptionPane.showInputDialog("Enter the new equipment");
                String quantityStr = JOptionPane.showInputDialog("Enter the new quantity");
                Date newMaintenanceDate = GUI.getDateInput();
                if (newEquipment != null && !newEquipment.isEmpty() && quantityStr != null && !quantityStr.isEmpty() && !(newMaintenanceDate.toString()).isEmpty()) {
                    int quantity = Integer.parseInt(quantityStr);
                    admin.addEquipment(newEquipment, quantity, newMaintenanceDate);
                }
            }
        });
        panel.add(addEquipmentButton);

        JButton removeEquipmentButton = new JButton("Remove Equipment");
        removeEquipmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = member.getUserID(username);
                ArrayList<String> equipments = admin.equipmentMonitoring();
                String message = "Enter equipments ID number from list below:\n";
                for (String option: equipments){
                    message += option + "\n";
                }
                String equipmentIDStr = JOptionPane.showInputDialog(message);
                if (equipmentIDStr != null && !equipmentIDStr.isEmpty()) {
                    int equipmentID = Integer.parseInt(equipmentIDStr);
                    admin.removeEquipment(equipmentID);
                }
            }
        });
        panel.add(removeEquipmentButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close current frame
                administrationDisplay(frame, username);
            }
        });
        panel.add(backButton);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }


    public void roomBookingDisplay(JFrame oldFrame, String username){
        oldFrame.dispose();
        JFrame frame = new JFrame("Manage Room Bookings");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int id = member.getUserID(username);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        JButton viewAllRooms = new JButton("View Current Booked Rooms");
        viewAllRooms.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = "";
                HashMap<Integer, ArrayList<String>> roomBookings = admin.getAllRoomBookings();
                for (HashMap.Entry<Integer, ArrayList<String>> entry : roomBookings.entrySet()) {
                    int roomNumber = entry.getKey();
                    ArrayList<String> bookingInfo = entry.getValue();

                    message += "Room Number: " + roomNumber + "\n";
                    for (int i = 0; i< bookingInfo.size(); i+=3) {
                        message += "Booking Date: " + bookingInfo.get(i) + "\n"
                                + "Time Block: " + bookingInfo.get(i+1) + "\n"
                                + "Booked By: " + bookingInfo.get(i+2) + "\n\n";
                    }
                    message += "------------------------------------------------\n";
                }
                JTextArea textArea = new JTextArea();
                textArea.setEditable(false);
                textArea.setLineWrap(true);
                textArea.append(message);
                textArea.setWrapStyleWord(true);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 300));

                JOptionPane.showMessageDialog(null, scrollPane, "Room Bookings", JOptionPane.PLAIN_MESSAGE);

            }
        });
        panel.add(viewAllRooms);

        JButton bookRoom = new JButton("Book a Room");
        bookRoom.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String roomNumber = JOptionPane.showInputDialog("Enter the room number");
                String bookingDate = JOptionPane.showInputDialog("Enter the desired booking date (Monday, Tuesday, etc.)");
                String timeBlock = JOptionPane.showInputDialog("Enter the time of day (Morning, Afternoon, Evening, Night)");
                String session = JOptionPane.showInputDialog("Type of Session: Private or Public");
                if (roomNumber != null && !roomNumber.isEmpty() && bookingDate != null && !bookingDate.isEmpty() && timeBlock != null && !timeBlock.isEmpty() && session != null && !session.isEmpty() ) {
                    admin.bookRoom(Integer.parseInt(roomNumber), Main.Days.valueOf(bookingDate.toUpperCase()), Main.Times.valueOf(timeBlock.toUpperCase()), member.getUserID(username), session);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Answer, Try Again");
                }
            }
        });
        panel.add(bookRoom);

        JButton updateRoomBooking = new JButton("Update a Room Booking");
        updateRoomBooking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String roomNumber = JOptionPane.showInputDialog("Enter the room number");
                String bookingDate = JOptionPane.showInputDialog("Enter the desired booking date (Monday, Tuesday, etc.)");
                String timeBlock = JOptionPane.showInputDialog("Enter the time of day (Morning, Afternoon, Evening, Night)");
                ArrayList<String> trainers = trainer.getTrainers();
                String message = "Enter trainer ID number from list below:\n";
                for (String option: trainers){
                    message += option + "\n";
                }
                String id = JOptionPane.showInputDialog(message);
                if (roomNumber != null && !roomNumber.isEmpty() && bookingDate != null && !bookingDate.isEmpty() && timeBlock != null && !timeBlock.isEmpty()) {
                    admin.updateRoomBooking(Integer.parseInt(roomNumber), Main.Days.valueOf(bookingDate.toUpperCase()), Main.Times.valueOf(timeBlock.toUpperCase()), Integer.parseInt(id));
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Answer, Try Again");
                }
            }
        });
        panel.add(updateRoomBooking);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close current frame
                administrationDisplay(frame, username);
            }
        });
        panel.add(backButton);

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

}
