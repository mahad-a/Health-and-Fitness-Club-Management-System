import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TrainerGUI {
    private JFrame frame;
    private Member member;
    private Trainer trainer;
    private Administration administration;

    public TrainerGUI() {
        member = new Member();
        trainer = new Trainer();
        frame = new JFrame();
        administration = new Administration();
    }
    public void viewMemberProfileGUI() {
        String username = JOptionPane.showInputDialog(null, "Enter Member Username:");
        if (username != null && !username.isEmpty()) {
            Trainer trainer = new Trainer();

            JOptionPane.showMessageDialog(null, trainer.viewMemberProfile(username));
        }
    }

    public void scheduleManagement(JFrame oldFrame, String username){
        oldFrame.dispose();
        JFrame profileFrame = new JFrame("Schedule Management for Trainers");
        profileFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        profileFrame.setSize(400, 300);
        profileFrame.setLocationRelativeTo(null);
        int userID = member.getUserID(username);

        JPanel panel = new JPanel(new GridLayout(3, 1));

        JButton viewScheduleButton = new JButton("View Your Schedule");
        JButton updateScheduleButton = new JButton("Update Schedule");
        JButton closeButton = new JButton("Close");

        viewScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "The following are your scheduled days: " + trainer.getTrainerSchedule(userID));
            }
        });
        updateScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog();
                dialog.setTitle("Select Available Days");
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
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
                        int id = member.getUserID(username);
                        trainer.updateTrainerSchedule(id, selectedDays.toString());
                        JOptionPane.showMessageDialog(frame, "Updating your Schedule");
                    }
                });
                panel.add(submitButton);
                dialog.add(panel);
                dialog.setVisible(true);
            }
        });
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                profileFrame.dispose();
                trainerDisplay(frame, username);
            }
        });

        panel.add(viewScheduleButton);
        panel.add(updateScheduleButton);
        panel.add(closeButton);

        profileFrame.add(panel);
        profileFrame.setVisible(true);
    }

    public void trainerDisplay(JFrame oldFrame, String username) {
        oldFrame.dispose();
        JFrame frame = new JFrame("Trainer Display Menu");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Trainer Display!");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1));

        JButton scheduleManagementButton = new JButton("Schedule Management");
        scheduleManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scheduleManagement(frame, username);
            }
        });

        JButton memberProfileButton = new JButton("Member Profile Viewing");
        memberProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewMemberProfileGUI();
            }
        });

        JButton applyAdminButton = new JButton("Apply to Administration");
        if (administration.isAdmin(member.getUserID(username))){
            applyAdminButton.setEnabled(false);
        }
        applyAdminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int trainerID = member.getUserID(username);
                trainer.trainerToAdmin(trainerID, username);
                JOptionPane.showMessageDialog(null, "Welcome to Administration Staff!");
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close current frame
                GUI.showNextScreen(frame, username, member.getUserID(username));
            }
        });

        buttonPanel.add(scheduleManagementButton);
        buttonPanel.add(memberProfileButton);
        buttonPanel.add(applyAdminButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        frame.getContentPane().add(mainPanel);
        frame.setVisible(true);
    }
}
