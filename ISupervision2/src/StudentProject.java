/*
 * Project: ISupervision
 * Author: Sarah Berger
 * Last Change: 26.05.2024
 */

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

/**
 * This class represents the layout for the Project Work window from the student's perspective and handles the action for the submit button.
 * It allows students to select a project theme, set a deadline, and submit their project work.
 */
public class StudentProject extends MainStudent {
    /**
     * The JComboBox for selecting a theme
     */
    private JComboBox<String> themeSelection;

    /**
     * The button for submitting the selected theme
     */
    private JButton submitButton;

    /**
     * The scroll pane for the Project Work interface
     */
    private JScrollPane proScroll;

    /**
     * The panel for the Project Work interface
     */
    private JPanel proPanel;

    /**
     * Constructor for initializing the StudentProject class and setting up the UI components and event listeners.
     * Upon clicking the submit button, the selected project theme and deadline are stored in the database.
     * If the student is already assigned to a project, an error message is displayed.
     */
    public StudentProject() {
        border("Project Work", proPanel);
        setJMenuBar(menuBar());
        getThemeNoStudents("Project Work", themeSelection);

        Dimension size = proScroll.getPreferredSize();
        proScroll.setViewportView(studentWork("Project Work"));
        proScroll.setPreferredSize(size);



        submitButton.addActionListener(_ -> {
            String usernameTemp = (LogIn.usernameLogin != null) ? LogIn.usernameLogin : Register.usernameRegister;
            String themeTemp = Objects.requireNonNull(themeSelection.getSelectedItem()).toString();

            try {
                Connection connection = DriverManager.getConnection(General.databaseUrl, General.databaseName, General.databasePassword);

                String checkSql = "SELECT COUNT(*) AS count FROM themes WHERE student_id = (SELECT user_id FROM users WHERE username = ?) AND finished = 'no'";
                PreparedStatement checkStatement = connection.prepareStatement(checkSql);
                checkStatement.setString(1, usernameTemp);
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next() && resultSet.getInt("count") > 0) {
                    JOptionPane.showMessageDialog(null, "You are already registered for a work.");
                    return;
                }

                String updateSql = "UPDATE themes SET  student_id = (SELECT user_id FROM users WHERE username = ?) WHERE theme_name = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateSql);
                updateStatement.setString(1, usernameTemp);
                updateStatement.setString(2, themeTemp);
                updateStatement.executeUpdate();

                JOptionPane.showMessageDialog(null, "Project Work assigned successfully!");

                proScroll.setViewportView(studentWork("Project Work"));

                getThemeNoStudents("Project Work", themeSelection);

                connection.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
