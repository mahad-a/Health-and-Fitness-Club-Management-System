import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class MemberGUI {
    private JFrame frame;
    private Member member;
    private Trainer trainer;
    private Administration admin;
    public MemberGUI() {
        member = new Member();
        trainer = new Trainer();
        admin = new Administration();
    }

    public void memberDisplay(JFrame oldFrame, String username) {
        oldFrame.dispose();
        frame = new JFrame();
        frame.setTitle("Member Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null); // Center the frame

        JLabel titleLabel = new JLabel("Member Display!");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
        JButton profileButton = new JButton("Profile Management");
        JButton dashboardButton = new JButton("Dashboard Display");
        JButton scheduleButton = new JButton("Schedule Management");
        JButton backButton = new JButton("Back");

        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                profileManagement(frame, username);
            }
        });
        dashboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dashboardDisplay(frame, username);
            }
        });
        scheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scheduleManagement(frame, username);
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                GUI.showNextScreen(frame, username, member.getUserID(username));
            }
        });

        buttonPanel.add(profileButton);
        buttonPanel.add(dashboardButton);
        buttonPanel.add(scheduleButton);
        buttonPanel.add(backButton);

        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.setResizable(false);
    }


    public void memberToTrainer(String username){
        if (username != null && !username.isEmpty()) {
            JDialog dialog = new JDialog();
            dialog.setTitle("Select Available Days");
            dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            dialog.setSize(300, 200);

            JPanel panel = new JPanel(new GridLayout(7, 1));

            JCheckBox[] checkboxes = new JCheckBox[Main.Days.values().length];
            for (int i = 0; i < Main.Days.values().length; i++) {
                Main.Days day = Main.Days.values()[i];
                checkboxes[i] = new JCheckBox(day.toString());
                panel.add(checkboxes[i]);
            }

            JButton submitButton = new JButton("Submit");
            submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ArrayList<Main.Days> selectedDays = new ArrayList<>();
                    for (int i = 0; i < checkboxes.length; i++) {
                        if (checkboxes[i].isSelected()) {
                            selectedDays.add(Main.Days.values()[i]);
                        }
                    }
                    dialog.dispose();

                    String specialization = JOptionPane.showInputDialog(frame, "Enter your specialization:");

                    if (specialization != null && !specialization.isEmpty()) {
                        int id = member.getUserID(username);
                        LocalDate today = LocalDate.now();
                        Date hireDate = java.sql.Date.valueOf(today);
                        trainer.newTrainer(id, username, specialization, selectedDays.toString(), hireDate);
                    }

                    JOptionPane.showMessageDialog(frame, "Adding as Trainer");
                }
            });

            panel.add(submitButton);
            dialog.add(panel);
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(frame, "No user logged in.");
        }
    }

    public void updatePersonalInfo(JFrame oldFrame, int userID){
        oldFrame.dispose();
        JFrame personalFrame = new JFrame("Personal Information Section");
        JPanel panel = new JPanel(new GridLayout(3, 1));
        personalFrame.setTitle("Personal Information");
        personalFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        personalFrame.setSize(600, 400);
        personalFrame.setLocationRelativeTo(null); // Center the frame

        JButton updateUsernameButton = new JButton("Update Username");
        JButton updatePasswordButton = new JButton("Update Password");
        JButton closeButton = new JButton("Close");

        String username = member.getUsername(userID);

        updatePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = JOptionPane.showInputDialog(frame, "Enter new password:");
                if (password != null && !password.isEmpty()) {
                    if (userID != -1) {
                        member.updatePassword(userID, password);
                    }
                }
                JOptionPane.showMessageDialog(frame, "Updating Password");
            }
        });
        String[] wrap = {username};
        updateUsernameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newUsername = JOptionPane.showInputDialog(frame, "Enter new username:");
                if (newUsername != null && !newUsername.isEmpty()) {
                    if (userID != -1) {
                        member.updateUsername(userID, newUsername);
                    }
                    if (trainer.isTrainer(userID)){
                        trainer.updateTrainerUsername(userID, newUsername);
                    }
                    wrap[0] = newUsername;
                    System.out.println("Updated username: " + wrap[0]);
                }
                JOptionPane.showMessageDialog(frame, "Updating Username");
            }
        });
        username = wrap[0];
        System.out.println("Current username after button: " + username);

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                personalFrame.dispose();
            }
        });

        panel.add(updateUsernameButton);
        panel.add(updatePasswordButton);
        panel.add(closeButton);

        personalFrame.add(panel);
        personalFrame.setVisible(true);
        personalFrame.setResizable(false);
    }

    public static void addExerciseRoutine(Member member, String username) {
        JFrame frame = new JFrame("Add Exercise Routine");
        frame.setSize(400, 200);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField routineNameField = new JTextField(20);
        JTextField durationField = new JTextField(20);
        JTextField quantityField = new JTextField(20);

        SpinnerDateModel model = new SpinnerDateModel(Calendar.getInstance().getTime(), null, null, Calendar.DAY_OF_MONTH);
        JSpinner exerciseDateSpinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(exerciseDateSpinner, "yyyy-MM-dd");
        exerciseDateSpinner.setEditor(editor);

        panel.add(new JLabel("Routine Name:"));
        panel.add(routineNameField);
        panel.add(new JLabel("Duration (minutes):"));
        panel.add(durationField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(new JLabel("Exercise Date (YYYY-MM-DD):"));
        panel.add(exerciseDateSpinner);

        JButton addButton = new JButton("Add Exercise Routine");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String routineName = routineNameField.getText();
                int duration = Integer.parseInt(durationField.getText());
                int quantity = Integer.parseInt(quantityField.getText());
                java.util.Date utilDate = (java.util.Date) exerciseDateSpinner.getValue();
                java.sql.Date exerciseDate = new java.sql.Date(utilDate.getTime());

                member.addExerciseRoutine(member.getUserID(username),routineName, duration, quantity, exerciseDate);
                frame.dispose();
            }
        });
        panel.add(addButton);

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


    public static void removeExerciseRoutine(Member member, String username) {
        int id = member.getUserID(username);
        ArrayList<String> routines = member.viewExerciseRoutines(id);
        String message = "Enter routine ID number from list below:\n";
        for (String option: routines){
            message += option + "\n";
        }
        String routineIDStr = JOptionPane.showInputDialog(message);
        if (routineIDStr != null && !routineIDStr.isEmpty()){
            int routineID = Integer.parseInt(routineIDStr);
            int userID = member.getUserID(username);
            member.removeExerciseRoutine(routineID, userID);
        }
    }

    public void classSchedule(JFrame oldFrame, String username){
        oldFrame.dispose();
        JFrame frame = new JFrame("Class Schedule Management");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton viewScheduleButton = new JButton("View All Class Schedules");
        JButton enrollButton = new JButton("Enroll in a Class");
        JButton dropOutButton = new JButton("Drop out of a Class");
        JButton backButton = new JButton("Back");

        viewScheduleButton.addActionListener(new ActionListener() {
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

        enrollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String className = JOptionPane.showInputDialog("Enter the class to enroll: " + member.getClassSchedules().toString());
                int confirm = JOptionPane.showConfirmDialog(null, "There is a fee of $50. Confirm purchase?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    String classList = member.getClassList(className);

                    if (classList == null || classList.isEmpty()) {
                        member.updateClassList(className, username);
                    } else {
                        String[] classSplit = classList.split(", ");
                        ArrayList<String> userList = new ArrayList<>(Arrays.asList(classSplit));
                        userList.add(username);
                        member.updateClassList(className, userList.toString());
                    }
                    admin.createBill(member.getUserID(username), Date.valueOf(LocalDate.now().plusDays(30)), 50, member.getPaymentType(member.getUserID(username)));
                }
            }
        });

        dropOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String className = JOptionPane.showInputDialog("Enter the class to drop out: " + member.getClassSchedules().toString());
                String classList = member.getClassList(className);

                if (classList == null || classList.isEmpty()) {
                    String updatedList = "";
                    member.updateClassList(className, updatedList);
                } else {
                    ArrayList<String> userList = new ArrayList<>();
                    classList = classList.replace("[", "").replace("]", "");
                    String[] classSplit = classList.split(", ");
                    for (String name: classSplit){
                        if (!Objects.equals(name, username)){
                            userList.add(name);
                        }
                    }
                    member.updateClassList(className, userList.toString());
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                memberDisplay(frame, username);
            }
        });

        JPanel panel = new JPanel(new GridLayout(4, 1));
        panel.add(viewScheduleButton);
        panel.add(enrollButton);
        panel.add(dropOutButton);
        panel.add(backButton);

        frame.add(panel);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public void profileManagement(JFrame oldFrame, String username) {
        oldFrame.dispose();
        JFrame profileFrame = new JFrame("Profile Management");
        profileFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        profileFrame.setSize(400, 300);
        profileFrame.setLocationRelativeTo(null);
        int userID = member.getUserID(username);

        JPanel panel = new JPanel(new GridLayout(5, 1));

        JButton updatePersonalInfoButton = new JButton("Update Personal Information");
        JButton updateFitnessGoalsButton = new JButton("Update Fitness Goals");
        JButton updateWeightButton = new JButton("Update Weight");
        JButton applyTrainerButton = new JButton("Apply for Trainer");
        JButton closeButton = new JButton("Close");

        boolean isTrainer = trainer.isTrainer(userID);
        if (isTrainer){
            applyTrainerButton.setEnabled(false);
        }

        updatePersonalInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePersonalInfo(frame, userID);
            }
        });
        updateFitnessGoalsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fitnessGoals = JOptionPane.showInputDialog(frame, "Enter new fitness goals:");
                if (fitnessGoals != null && !fitnessGoals.isEmpty()) {
                    int userID = member.getUserID(username);
                    if (userID != -1) {
                        member.updateFitnessGoals(userID, fitnessGoals);
                        JOptionPane.showMessageDialog(frame, "Updating Fitness Goals");
                    }
                }
            }
        });
        updateWeightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String weightStr = JOptionPane.showInputDialog(frame, "Enter new weight:");
                if (weightStr != null && !weightStr.isEmpty()) {
                    try {
                        float newWeight = Float.parseFloat(weightStr);
                        int userID = member.getUserID(username);
                        if (userID != -1) {
                            member.updateWeight(userID, newWeight);
                            JOptionPane.showMessageDialog(frame, "Updating Weight");
                        }
                    } catch (NumberFormatException r) {
                        JOptionPane.showMessageDialog(frame, "Invalid weight value. Please enter a valid number.");
                    }
                }
            }
        });

        applyTrainerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                memberToTrainer(username);
                if (trainer.isTrainer(userID)) applyTrainerButton.setEnabled(false);
            }
        });


        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                profileFrame.dispose();
                memberDisplay(frame, username);
            }
        });

        panel.add(updatePersonalInfoButton);
        panel.add(updateFitnessGoalsButton);
        panel.add(updateWeightButton);
        panel.add(applyTrainerButton);
        panel.add(closeButton);

        profileFrame.add(panel);
        profileFrame.setVisible(true);
    }

    public void dashboardDisplay(JFrame oldFrame, String username) {
        oldFrame.dispose();
        JFrame dashboardFrame = new JFrame("Dashboard Management");
        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboardFrame.setLocationRelativeTo(null);
        dashboardFrame.setSize(500, 350);

        int id = member.getUserID(username);

        // Create buttons
        JButton viewRoutinesButton = new JButton("View Exercise Routines");
        JButton addRoutineButton = new JButton("Add New Exercise Routine");
        JButton removeRoutineButton = new JButton("Remove Exercise Routine");
        JButton viewAchievementsButton = new JButton("View Fitness Achievements");
        JButton addAchievementsButton = new JButton("Add an Fitness Achievement");
        JButton removeAchievementButton = new JButton("Remove a Fitness Achievement");
        JButton viewStatisticsButton = new JButton("View Health Statistics");
        JButton addStatisticsButton = new JButton("Add Health Statistics");
        JButton removeStatisticsButton = new JButton("Remove Health Statistics");
        JButton closeButton = new JButton("Close");

        dashboardFrame.setLayout(new GridLayout(5, 2));
        dashboardFrame.add(viewRoutinesButton);
        dashboardFrame.add(addRoutineButton);
        dashboardFrame.add(removeRoutineButton);
        dashboardFrame.add(viewAchievementsButton);
        dashboardFrame.add(addAchievementsButton);
        dashboardFrame.add(removeAchievementButton);
        dashboardFrame.add(viewStatisticsButton);
        dashboardFrame.add(addStatisticsButton);
        dashboardFrame.add(removeStatisticsButton);
        dashboardFrame.add(closeButton);

        viewRoutinesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> routines = member.viewExerciseRoutines(id);
                JTextArea textArea = new JTextArea();
                textArea.setEditable(false);
                textArea.setLineWrap(true);
                textArea.append(routines.toString());
                textArea.setWrapStyleWord(true);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 300));

                JOptionPane.showMessageDialog(null, scrollPane, "Exercise Routines", JOptionPane.PLAIN_MESSAGE);
            }
        });

        addRoutineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addExerciseRoutine(member, username);
            }
        });

        removeRoutineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeExerciseRoutine(member, username);
            }
        });

        addAchievementsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String achievementInfo = JOptionPane.showInputDialog("What did you achieve?");
                Date date = GUI.getDateInput();
                if (achievementInfo != null && !achievementInfo.isEmpty() && !date.toString().isEmpty()){
                    member.addFitnessAchievement(member.getUserID(username), achievementInfo, date);
                }
            }
        });

        removeAchievementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = member.getUserID(username);
                ArrayList<String> fitness = member.viewFitnessAchievements(id);
                String message = "Enter fitness ID number from list below:\n";
                for (String option: fitness){
                    message += option + "\n";
                }
                String achievementID = JOptionPane.showInputDialog(message);
                if (achievementID != null && !achievementID.isEmpty()) {
                    member.removeFitnessAchievement(Integer.parseInt(achievementID));
                }
            }
        });

        viewAchievementsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> achievements = member.viewFitnessAchievements(member.getUserID(username));
                JTextArea textArea = new JTextArea();
                textArea.setEditable(false);
                for (String achievement : achievements) {
                    textArea.append(achievement + "\n");
                }
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 300));
                JOptionPane.showMessageDialog(null, scrollPane, "Your Fitness Achievements", JOptionPane.PLAIN_MESSAGE);
            }
        });

        viewStatisticsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> health = member.viewHealthStatistics(id);
                JTextArea textArea = new JTextArea();
                textArea.setEditable(false);
                for (String healthStat : health) {
                    textArea.append(healthStat + "\n");
                }
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 300));
                JOptionPane.showMessageDialog(null, scrollPane, "Your Health Statistics", JOptionPane.PLAIN_MESSAGE);
            }
        });

        addStatisticsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String stat = JOptionPane.showInputDialog("What is the new statistic?");
                if (stat != null && !stat.isEmpty()) {
                    member.addHealthStatistic(id, stat);
                    JOptionPane.showMessageDialog(null, "Added new statistic");
                }
            }
        });

        removeStatisticsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> healthOptions = member.viewHealthStatistics(id);
                String message = "Enter ID number from list below:\n";
                for (String option: healthOptions){
                    message += option + "\n";
                }
                String healthIDStr = JOptionPane.showInputDialog(message);
                if (healthIDStr != null && !healthIDStr.isEmpty()) {
                    member.removeHealthStatistic(id, Integer.parseInt(healthIDStr));
                }
            }
        });
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dashboardFrame.dispose();
                memberDisplay(frame, username);
            }
        });

        dashboardFrame.setVisible(true);
    }

    public void scheduleManagement(JFrame oldFrame, String username) {
        oldFrame.dispose();
        JFrame frame = new JFrame("Schedule Management");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridLayout(3, 1));

        JButton bookSessionButton = new JButton("Book a Private Session");
        JButton enrollClassButton = new JButton("Enroll in Fitness Class");
        JButton backButton = new JButton("Back");

        bookSessionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> trainers = trainer.getTrainers();
                String message = "Enter trainer ID number from list below:\n";
                for (String option: trainers){
                    message += option + "\n";
                }
                String id = JOptionPane.showInputDialog(message);
                if (id != null && !id.isEmpty()) {
                    int confirm = JOptionPane.showConfirmDialog(null, "There is a fee of $75. Confirm purchase?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION){
                        int trainerID = Integer.parseInt(id);
                        String message2 = member.bookPrivateSession(trainerID);
                        JOptionPane.showMessageDialog(null, message2);
                        admin.createBill(member.getUserID(username), Date.valueOf(LocalDate.now().plusDays(30)), 75, member.getPaymentType(member.getUserID(username)));
                    } else {
                        JOptionPane.showMessageDialog(null, "Booking cancelled.");
                    }
                }
            }
        });

        enrollClassButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                classSchedule(frame, username);
            }
        });


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                memberDisplay(frame, username);
            }
        });

        frame.add(bookSessionButton);
        frame.add(enrollClassButton);
        frame.add(backButton);

        frame.setVisible(true);
    }
}
