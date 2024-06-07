/*
 * Project: ISupervision
 * Author: Sarah Berger
 * Last Change: 26.05.2024
 */

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * The class is for the menubar and buttons for the assistants
 */
public class MainAssist extends JFrame {

    /**
     * Creates a new window with a specific title and design.
     *
     * @param text  the title of the window
     * @param panel the design of the new window
     */
    public void borderNormal(String text, JPanel panel) {
        ImageIcon icon = new ImageIcon("src/media/eye.png");
        setIconImage(icon.getImage());
        setTitle(text);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    /**
     * Creates a menu bar displaying various options for projects and home.
     *
     * @return a new menu bar
     */
    public static JMenuBar menuBar() {
        JMenuBar assistentMenu = new JMenuBar();
        JMenu workMenu = new JMenu("Projects");
        JMenuItem projectWork = new JMenuItem("Project Work");
        JMenuItem bachelorthesis = new JMenuItem("Bachelor's Thesis");
        JMenuItem masterthesis = new JMenuItem("Master's Thesis");
        JMenu home = new JMenu("Home");
        JMenuItem assistHome = new JMenuItem("Assistent home");
        JMenu profile = new JMenu("Profile");
        JMenuItem updateProfile = new JMenuItem("Update Profile");

        workMenu.add(projectWork);
        workMenu.add(bachelorthesis);
        workMenu.add(masterthesis);
        home.add(assistHome);
        profile.add(updateProfile);
        assistentMenu.add(home);
        assistentMenu.add(workMenu);
        assistentMenu.add(profile);

        assistHome.addActionListener(_ -> {
            JFrame frame = new AssistHome();
            frame.setVisible(true);
            (SwingUtilities.getWindowAncestor(assistentMenu)).dispose();
        });
        projectWork.addActionListener(_ -> {
            JFrame frame = new AssistProject();
            frame.setVisible(true);
            (SwingUtilities.getWindowAncestor(assistentMenu)).dispose();
        });
        bachelorthesis.addActionListener(_ -> {
            JFrame frame = new AssistBachelor();
            frame.setVisible(true);
            (SwingUtilities.getWindowAncestor(assistentMenu)).dispose();
        });
        masterthesis.addActionListener(_ -> {
            JFrame frame = new AssistMaster();
            frame.setVisible(true);
            (SwingUtilities.getWindowAncestor(assistentMenu)).dispose();
        });
        updateProfile.addActionListener(_ -> {
            JFrame frame = new Profile("assistant");
            frame.setVisible(true);
            (SwingUtilities.getWindowAncestor(profile)).dispose();
        });

        return assistentMenu;
    }

    /**
     * Adds actions to the buttons to open new windows when clicked.
     *
     * @param create      the button for creating a theme
     * @param limit       the button for setting a limit
     * @param panel       a ScrollPane to be updated after an action
     * @param currentView the current window where the button was clicked (e.g., Bachelor's Thesis)
     * @param username    the username of the admin of the currently logged-in assistant
     */
    public void buttonAction(JButton create, JButton limit, JScrollPane panel, String currentView, String username) {
        create.addActionListener(_ -> {
            CreateButton createButton = new CreateButton(panel, currentView, username);
            createButton.setVisible(true);
        });

        limit.addActionListener(_ -> {
            LimitButton limitButton = new LimitButton(currentView, username);
            limitButton.setVisible(true);
        });
    }

    /**
     * Retrieves the username of the admin of the assistant currently logged in.
     *
     * @return the username of the admin
     */
    public static String getAdmin() {
        String usernameTemp = (LogIn.usernameLogin != null) ? LogIn.usernameLogin : Register.usernameRegister;
        String usernameAdmin = null;

        try {
            Connection connection = DriverManager.getConnection(General.databaseUrl, General.databaseName, General.databasePassword);

            String sql = "SELECT u.username FROM users u JOIN admin_assistants a ON u.user_id = a.admin_id WHERE a.assistant_id = (SELECT user_id FROM users WHERE username = ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, usernameTemp);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                usernameAdmin = resultSet.getString("username");
            }
            preparedStatement.close();
            resultSet.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usernameAdmin;
    }
}
