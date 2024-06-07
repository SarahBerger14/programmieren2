/*
 * Project: ISupervision
 * Author: Sarah Berger
 * Last Change: 26.05.2024
 */

import javax.swing.*;

/**
 * This class handles the login functionality for existing users.
 */
public class LogIn extends MainStudent {
    /**
     * The panel for the login window
     */
    public JPanel logInPanel;

    /**
     * The button for navigating to the registration window
     */
    private JButton registerButton;

    /**
     * The button for submitting the login credentials
     */
    private JButton submitButton;

    /**
     * The field for entering the password
     */
    private JPasswordField enterPassword;

    /**
     * The field for entering the username
     */
    private JTextField enterUsername;

    /**
     * The username of the person that is using the GUI now, if the person is in the GUI per registration
     * then the username is null
     */
    public static String usernameLogin = null;

    /**
     * Constructor that sets up the login interface and its action listeners.
     * Initializes the window design and functionality. The register button switches to the registration window.
     * The submit button verifies the entered username and password. If valid, the window switches to the appropriate
     * home window based on the user's role. If invalid, an error message is shown.
     */
    public LogIn() {
        ImageIcon icon = new ImageIcon("src/media/eye.png");
        setIconImage(icon.getImage());
        setTitle("LogIn");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(logInPanel);
        setSize(500, 500);
        setLocationRelativeTo(null);

        registerButton.addActionListener(_ -> {
            Register registerWindow = new Register();
            registerWindow.setVisible(true);
            LogIn.this.dispose();
        });

        submitButton.addActionListener(_ -> {
            String username = enterUsername.getText();
            String password = String.valueOf(enterPassword.getPassword());

            LoginStatus loginStatus = LoginStatus.login(username, password);

            switch (loginStatus) {
                case SUCCESS:
                    User user = User.setData(username);
                    usernameLogin = user.getUsername();
                    if (user.email.matches(".*\\.ad$")) {
                        AdminHome adminHome = new AdminHome();
                        adminHome.setVisible(true);
                        this.dispose();
                    } else if (user.email.matches(".*\\.as$")) {
                        String usernameAdmin = MainAssist.getAdmin();
                        if (usernameAdmin == null) {
                            JOptionPane.showMessageDialog(null, "Please wait till a admin will select you as his assistant!");
                        } else {
                            AssistHome assistHome = new AssistHome();
                            assistHome.setVisible(true);
                            this.dispose();
                        }
                    } else {
                        StudentHome studentHome = new StudentHome();
                        studentHome.setVisible(true);
                        this.dispose();
                    }
                    break;
                case INVALID_USERNAME:
                    JOptionPane.showMessageDialog(logInPanel, "Invalid username!", "General", JOptionPane.ERROR_MESSAGE);
                    break;
                case INVALID_PASSWORD:
                    JOptionPane.showMessageDialog(logInPanel, "Invalid password!", "General", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        });
    }

    /**
     * Setter
     * @param usernameLogin the username of the person that is using the GUI now
     */
    public static void setUsernameLogin(String usernameLogin) {
        LogIn.usernameLogin = usernameLogin;
    }
}