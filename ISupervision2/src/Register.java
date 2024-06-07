/*
 * Project: ISupervision
 * Author: Sarah Berger
 * Last Change: 26.05.2024
 */

import javax.swing.*;

/**
 * This class handles the registration process for new users.
 */
public class Register extends MainStudent {

    /**
     * The panel for the registration interface
     */
    public JPanel registerPanel;

    /**
     * The button for switching to the login window
     */
    private JButton logInButton;

    /**
     * The button for submitting the registration
     */
    private JButton submitButton;

    /**
     * The field for entering the username
     */
    private JTextField enterUsername;

    /**
     * The field for entering the email
     */
    private JTextField enterEmail;

    /**
     * The field for entering the password
     */
    private JPasswordField enterPassword;

    /**
     * The field for entering the password again
     */
    private JPasswordField enterConfPassword;

    /**
     * The field for entering the last name
     */
    private JTextField enterLastName;

    /**
     * The field for entering the first name
     */
    private JTextField enterFirstName;

    /**
     * The username of the person that is using the GUI now, if the user is loggen in via the logIn then
     * the username is null
     */
    public static String usernameRegister = null;

    /**
     * Initializes the registration window and sets up event listeners for the login and submit buttons.
     * The login button switches to the login window.
     * Upon clicking the submit button, the entered user data is validated.
     * If the data is valid, a new user is created and registered in the system.
     * Depending on the user's role (admin, student, assistant), they are directed to the corresponding home window.
     */
    public Register() {
        ImageIcon icon = new ImageIcon("src/media/eye.png");
        setIconImage(icon.getImage());
        setTitle("Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(registerPanel);
        setSize(500, 600);
        setLocationRelativeTo(null);

        logInButton.addActionListener(_ -> {
            LogIn logInWindow = new LogIn();
            logInWindow.setVisible(true);
            Register.this.dispose();
        });

        submitButton.addActionListener(_ -> {
            String pass = String.valueOf(enterPassword.getPassword());
            String confPass = String.valueOf(enterConfPassword.getPassword());
            String firstName = enterFirstName.getText();
            String secondName = enterLastName.getText();
            String email = enterEmail.getText();
            String username = enterUsername.getText();
            LoginStatus loginStatus = LoginStatus.login(username, pass);

            boolean error = General.valid(firstName, secondName, username, email, pass, confPass, loginStatus);
            if (error) {
                User user = User.createUser(firstName, secondName, username, email, pass);
                usernameRegister = user.getUsername();

                if (email.matches(".*\\.ad$")) {
                    user.createRole("admin");
                    AdminHome adminHome = new AdminHome();
                    adminHome.setVisible(true);
                    this.dispose();
                } else if (email.matches(".*\\.st$")) {
                    user.createRole("student");
                    StudentHome studentHome = new StudentHome();
                    studentHome.setVisible(true);
                    this.dispose();
                } else if (email.matches(".*\\.as$")) {
                    user.createRole("assistent");
                    String usernameAdmin = MainAssist.getAdmin();
                    if (usernameAdmin == null) {
                        JOptionPane.showMessageDialog(null, "Please wait till a admin will select you as his assistant!");
                    } else {
                        AssistHome assistHome = new AssistHome();
                        assistHome.setVisible(true);
                        this.dispose();
                    }
                }
            }
        });
    }

    /**
     * Setter
     * @param usernameRegister the username of the person that is using the GUI now
     */
    public static void setUsernameRegister(String usernameRegister) {
        Register.usernameRegister = usernameRegister;
    }
}
