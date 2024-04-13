import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.Calendar;

public class GUI {
    private static boolean isTrainer = false;
    private static boolean isAdmin = false;
    private static Member member;
    private static Trainer trainer;
    private static Administration administration;
    private static MemberGUI memberGUI;
    private static TrainerGUI trainerGUI;
    private static AdministrationGUI administrationGUI;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        member = new Member();
        trainer = new Trainer();
        administration = new Administration();
        memberGUI = new MemberGUI();
        trainerGUI = new TrainerGUI();
        administrationGUI = new AdministrationGUI();
        startGUI(frame);
    }

    private static boolean logIn(String username, String password) {
        return member.logIn(username, password);
    }

    public static void showNextScreen(JFrame oldFrame, String username, int userID) {
        oldFrame.dispose();
        JFrame frame = new JFrame("Main Menu");
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setSize(500, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel titleLabel = new JLabel("Hello " + username + "! Welcome to Health and Fitness Club Management System!");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JButton memberButton = new JButton("Member Display");
        memberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                memberGUI.memberDisplay(frame, username);
            }
        });
        isTrainer = trainer.isTrainer(userID);
        isAdmin = administration.isAdmin(userID);
        JButton trainerButton = new JButton("Trainers Display");
        if (isTrainer) {
            trainerButton.setEnabled(true);
            trainerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    trainerGUI.trainerDisplay(frame, username);
                }
            });
        } else {
            trainerButton.setEnabled(false);
        }

        JButton adminButton = new JButton("Administration Display");
        if (isAdmin) {
            adminButton.setEnabled(true);
            adminButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    administrationGUI.administrationDisplay(frame, username);
                }
            });
        } else {
            adminButton.setEnabled(false);
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(memberButton);
        buttonPanel.add(trainerButton);
        buttonPanel.add(adminButton);

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> System.exit(0));

        JButton signOutButton = new JButton("Sign Out");
        signOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGUI(frame);
            }
        });

        frame.add(titleLabel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(signOutButton, BorderLayout.SOUTH);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    public static Date getDateInput() {
        SpinnerDateModel model = new SpinnerDateModel(Calendar.getInstance().getTime(), null, null, Calendar.DAY_OF_MONTH);
        JSpinner enrollmentDateSpinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(enrollmentDateSpinner, "yyyy-MM-dd");
        enrollmentDateSpinner.setEditor(editor);

        Object[] message = {"Select Date:", enrollmentDateSpinner};
        JOptionPane.showMessageDialog(null, message, "Date Selection", JOptionPane.PLAIN_MESSAGE);

        java.util.Date utilDate = (java.util.Date) enrollmentDateSpinner.getValue();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        return sqlDate;
    }


    private static void startGUI(JFrame frame) {
        frame.getContentPane().removeAll();
        frame.setTitle("Health and Fitness Club Management System");
        frame.setSize(500, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel titleLabel = new JLabel("Welcome to Health and Fitness Club Management System!");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton signInButton = new JButton("Sign In");
        JButton signUpButton = new JButton("Sign Up");
        JButton quitButton = new JButton("Quit");

        signInButton.addActionListener(e -> {
            showSignInPanel(frame);
        });

        signUpButton.addActionListener(e -> {
            showSignUpPanel(frame);
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
        buttonPanel.add(signInButton);
        buttonPanel.add(signUpButton);

        frame.add(titleLabel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(quitButton, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
        frame.setResizable(false);
    }


    private static void showSignInPanel(JFrame frame) {
        frame.getContentPane().removeAll();
        frame.setSize(500, 200);

        JLabel titleLabel = new JLabel("Enter Username and Password.");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        JPanel loginPanel = new JPanel(new GridLayout(5, 2));
        loginPanel.add(new JLabel());
        loginPanel.add(new JLabel());
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel());
        loginPanel.add(new JLabel());
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(new JLabel());
        loginPanel.add(new JLabel());

        JButton signInButton = new JButton("Sign In");
        JButton backButton = new JButton("Back");
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                boolean loggedIn = logIn(username, password);

                if (loggedIn) {
                    int id = member.getUserID(username);
                    showNextScreen(frame, username, id);
                } else {
                    JOptionPane.showMessageDialog(frame, "Incorrect username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGUI(frame);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(signInButton);
        buttonPanel.add(backButton);

        frame.add(titleLabel, BorderLayout.NORTH);
        frame.add(loginPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.revalidate();
        frame.repaint();
    }

    private static void showSignUpPanel(JFrame frame) {
        frame.getContentPane().removeAll();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 2));

        JLabel firstNameLabel = new JLabel("First Name:");
        JTextField firstNameField = new JTextField();
        JLabel lastNameLabel = new JLabel("Last Name:");
        JTextField lastNameField = new JTextField();
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JTextField passwordField = new JTextField();
        JLabel fitnessGoalLabel = new JLabel("Fitness Goal:");
        JTextField fitnessGoalField = new JTextField();
        JLabel weightLabel = new JLabel("Weight:");
        JTextField weightField = new JTextField();
        JLabel enrollmentDateLabel = new JLabel("Enrollment Date:");

        SpinnerDateModel model = new SpinnerDateModel(Calendar.getInstance().getTime(), null, null, Calendar.DAY_OF_MONTH);
        JSpinner enrollmentDateSpinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(enrollmentDateSpinner, "yyyy-MM-dd");
        enrollmentDateSpinner.setEditor(editor);

        JLabel paymentLabel = new JLabel("Payment Type");
        String[] paymentOptions = {"Cash", "Debit", "Credit"};
        JComboBox<String> paymentCombo = new JComboBox<>(paymentOptions);

        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String username = usernameField.getText();
                String password = passwordField.getText();
                String fitnessGoal = fitnessGoalField.getText();
                float weight = Float.parseFloat(weightField.getText());
                java.util.Date utilDate = (java.util.Date) enrollmentDateSpinner.getValue();
                java.sql.Date enrollmentDate = new java.sql.Date(utilDate.getTime());

                String paymentType = (String) paymentCombo.getSelectedItem();

                member.userRegistration(firstName, lastName, username, password, fitnessGoal, weight, enrollmentDate);
                administration.setupPayee(paymentType, username);
                System.out.println("NEXT SCREEN WE REACHED");
                showNextScreen(frame, username, member.getUserID(username));
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGUI(frame);
            }
        });

        panel.add(firstNameLabel);
        panel.add(firstNameField);
        panel.add(lastNameLabel);
        panel.add(lastNameField);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(fitnessGoalLabel);
        panel.add(fitnessGoalField);
        panel.add(weightLabel);
        panel.add(weightField);
        panel.add(enrollmentDateLabel);
        panel.add(enrollmentDateSpinner);
        panel.add(paymentLabel);
        panel.add(paymentCombo);
        panel.add(backButton);
        panel.add(registerButton);


        frame.add(panel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }
}
