/*
 * Project: ISupervision
 * Author: Sarah Berger
 * Last Change: 26.05.2024
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * The class is for everything that has to do with the admin and what will be shown to the admin and assistant.
 */
public class MainAdmin extends JFrame {

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
     * Creates smaller buttons that only close themselves and not the entire program.
     *
     * @param text  the window title
     * @param panel the design of the new window for the button
     */
    public void borderButton(String text, JPanel panel) {
        ImageIcon icon = new ImageIcon("src/media/eye.png");
        setIconImage(icon.getImage());
        setTitle(text);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(panel);
        setSize(400, 300);
        setLocationRelativeTo(null);
    }

    /**
     * Creates a menu bar displaying various options for projects, home, and assistants.
     *
     * @return a new menu bar
     */
    public static JMenuBar menuBar() {
        JMenuBar adminMenu = new JMenuBar();
        JMenu workMenu = new JMenu("Projects");
        JMenuItem projectWork = new JMenuItem("Project Work");
        JMenuItem bachelorthesis = new JMenuItem("Bachelor's Thesis");
        JMenuItem masterthesis = new JMenuItem("Master's Thesis");
        JMenu home = new JMenu("Home");
        JMenuItem adminHome = new JMenuItem("Admin home");
        JMenu assistant = new JMenu("Assistant");
        JMenuItem createAssistant = new JMenuItem("Create Assistant");
        JMenu profile = new JMenu("Profile");
        JMenuItem updateProfile = new JMenuItem("Update Profile");
        JMenu logOut = new JMenu("Log out");
        JMenuItem logingOut = new JMenuItem("Log out");

        workMenu.add(projectWork);
        workMenu.add(bachelorthesis);
        workMenu.add(masterthesis);
        home.add(adminHome);
        assistant.add(createAssistant);
        profile.add(updateProfile);
        logOut.add(logingOut);

        adminMenu.add(home);
        adminMenu.add(workMenu);
        adminMenu.add(assistant);
        adminMenu.add(profile);
        adminMenu.add(logOut);

        createAssistant.addActionListener(_ -> {
            JFrame frame = new SelectAssist();
            frame.setVisible(true);
            (SwingUtilities.getWindowAncestor(adminMenu)).dispose();
        });
        adminHome.addActionListener(_ -> {
            JFrame frame = new AdminHome();
            frame.setVisible(true);
            (SwingUtilities.getWindowAncestor(adminMenu)).dispose();
        });
        projectWork.addActionListener(_ -> {
            JFrame frame = new AdminProject();
            frame.setVisible(true);
            (SwingUtilities.getWindowAncestor(adminMenu)).dispose();
        });
        bachelorthesis.addActionListener(_ -> {
           JFrame frame = new AdminBachelor();
            frame.setVisible(true);
            (SwingUtilities.getWindowAncestor(adminMenu)).dispose();
        });
        masterthesis.addActionListener(_ -> {
            JFrame frame = new AdminMaster();
            frame.setVisible(true);
            (SwingUtilities.getWindowAncestor(adminMenu)).dispose();
        });
        updateProfile.addActionListener(_ -> {
            JFrame frame = new Profile("admin");
            frame.setVisible(true);
            (SwingUtilities.getWindowAncestor(adminMenu)).dispose();
        });
        logingOut.addActionListener(_ ->{
            JFrame frame = new Index();
            frame.setVisible(true);
            (SwingUtilities.getWindowAncestor(adminMenu)).dispose();
        });

        return adminMenu;
    }

    /**
     * Adds actions to the buttons to open new windows when clicked.
     *
     * @param create      creating a theme
     * @param delete      deleting a theme
     * @param limit       setting a limit
     * @param modify      modifying deadline and exam date (if Master's Thesis)
     * @param finish      marking as finished if a student finishes a theme
     * @param panel       a ScrollPane that will be updated after an action
     * @param currentView the current window from where the button was clicked (e.g., Bachelor's Thesis)
     * @param username    the username of the person who is logged in
     */
    public void buttonAction(JButton create, JButton delete, JButton limit, JButton modify, JButton finish, JScrollPane panel, String currentView, String username) {
        create.addActionListener(_ -> {
            CreateButton createButton = new CreateButton(panel, currentView, username);
            createButton.setVisible(true);
        });

        delete.addActionListener(_ -> {
            DeleteButton deleteButton = new DeleteButton(panel, currentView);
            deleteButton.setVisible(true);
        });

        limit.addActionListener(_ -> {
            LimitButton limitButton = new LimitButton(currentView, username);
            limitButton.setVisible(true);
        });

        if (!currentView.equals("Master's Thesis")) {
            modify.addActionListener(_ -> {
                ModifyButton modifyButton = new ModifyButton(currentView);
                modifyButton.setVisible(true);
            });
        } else {
            modify.addActionListener(_ -> {
                ModifyButtonMaster modifyButtonMaster = new ModifyButtonMaster();
                modifyButtonMaster.setVisible(true);
            });
        }

        finish.addActionListener(_ -> {
            FinishButton finishButton = new FinishButton(panel, currentView);
            finishButton.setVisible(true);
        });

    }

    /**
     * Shows the admin and assistant the themes created by the admin/assistant that haven't been finished by a student yet.
     *
     * @param type         the type of work (Bachelor's Thesis, Master's Thesis, or Project Work)
     * @param usernameTemp the username of the currently logged-in user or the admin for whom the themes should be shown
     * @return a ScrollPane with the corresponding themes
     */
    public static JScrollPane adminWork(String type, String usernameTemp) {
        try {
            Connection connection = DriverManager.getConnection(General.databaseUrl, General.databaseName , General.databasePassword );

            String sql = "SELECT theme_name, admin_id, finished FROM themes WHERE type = ? AND admin_id = (SELECT user_id FROM users WHERE username = ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, type);
            statement.setString(2, usernameTemp);
            ResultSet resultSet = statement.executeQuery();

            List<String> work = new ArrayList<>();
            while (resultSet.next()) {
                String finished = resultSet.getString("finished");
                if (finished.equals("no")) {
                    String workTemp = resultSet.getString("theme_name");
                    work.add(workTemp);
                }
            }

            JTextArea textArea = new JTextArea();
            for (String thesis : work) {
                textArea.append(thesis + "\n");
            }
            textArea.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(textArea);

            resultSet.close();
            statement.close();
            connection.close();

            return scrollPane;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Shows the admin and assistant the themes and work that students are currently working on, created by the admin/assistant.
     *
     * @param usernameTemp the username of the currently logged-in user or the admin for whom the themes should be shown
     * @return a ScrollPane with the corresponding active work
     */
    public static JScrollPane adminAktiveWork(String usernameTemp) {
        try {
            Connection connection = DriverManager.getConnection(General.databaseUrl, General.databaseName, General.databasePassword);

            String sql = "SELECT t.type, t.deadline, t.exam_date, t.theme_name, t.student_id, t.finished, u.firstName, u.lastName FROM themes t JOIN users u ON t.student_id = u.user_id WHERE admin_id = (SELECT user_id FROM users WHERE username = ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, usernameTemp);
            ResultSet resultSet = statement.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Theme Name");
            model.addColumn("Type");
            model.addColumn("Deadline");
            model.addColumn("Exam Date");
            model.addColumn("Student Name");

            while (resultSet.next()) {
                String themeName = resultSet.getString("theme_name");
                String typeName = resultSet.getString("type");
                String deadline = resultSet.getString("deadline");
                String examDate = resultSet.getString("exam_date");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String finished = resultSet.getString("finished");

                if (firstName != null && finished.equals("no")) {
                    Vector<String> row = new Vector<>();
                    row.add(themeName);
                    row.add(typeName);
                    row.add(deadline);
                    if (typeName.equals("Master's Thesis")) {
                        row.add(examDate);
                    } else {
                        row.add("-");
                    }
                    row.add(firstName + " " + lastName);
                    model.addRow(row);
                }
            }

            JTable table = new JTable(model);

            JScrollPane scrollPane = new JScrollPane(table);
            table.setEnabled(false);
            resultSet.close();
            statement.close();
            connection.close();

            return scrollPane;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Updates the ComboBox with available themes.
     *
     * @param currentView   the current view from which the ComboBox is needed (e.g., Bachelor's Thesis)
     * @param themeComboBox the ComboBox to be updated
     * @param usernameTemp  the username of the currently logged-in user or the admin for whom the themes should be shown
     */
    public void getThemeComboBox(String currentView, JComboBox<String> themeComboBox, String usernameTemp) {

        try {
            Connection connection = DriverManager.getConnection(General.databaseUrl, General.databaseName, General.databasePassword);
            String sql = "SELECT theme_name FROM themes WHERE type = ? AND admin_id = (SELECT user_id FROM users WHERE username = ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, currentView);
            preparedStatement.setString(2, usernameTemp);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                themeComboBox.addItem(resultSet.getString("theme_name"));
            }

            preparedStatement.close();
            resultSet.close();
            connection.close();
        } catch (Exception a) {
            a.printStackTrace();
        }
    }

    /**
     * Retrieves the themes that are not yet finished and adds them to the ComboBox.
     *
     * @param currentView   the current view from which the ComboBox is needed (e.g., Bachelor's Thesis)
     * @param themeComboBox the ComboBox to be updated
     * @param usernameTemp  the username of the currently logged-in user or the admin for whom the themes should be shown
     */
    public void getFinishedSelect(String currentView, JComboBox<String> themeComboBox, String usernameTemp) {

        themeComboBox.removeAllItems();

        try {
            Connection connection = DriverManager.getConnection(General.databaseUrl, General.databaseName, General.databasePassword);

            String sql = "SELECT theme_name, student_id FROM themes WHERE type = ? AND finished = 'no' AND admin_id = (SELECT user_id FROM users WHERE username = ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, currentView);
            preparedStatement.setString(2, usernameTemp);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                resultSet.getString("student_id");
                if (!resultSet.wasNull()) {
                    themeComboBox.addItem(resultSet.getString("theme_name"));
                }
            }
            preparedStatement.close();
            resultSet.close();
            connection.close();
        } catch (Exception a) {
            a.printStackTrace();
        }
    }
}