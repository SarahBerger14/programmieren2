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
 * The class is for the whole layout of the Bachelor's Thesis window from the student view and the action for the submit button.
 */
public class StudentBachelor extends MainStudent {
    /**
     * The panel for the Bachelor's Thesis interface
     */
    private JPanel bachelorPanel;

    /**
     * The JComboBox for selecting a theme
     */
    private JComboBox<String> themeSelection;

    /**
     * The button for submitting the selected theme
     */
    private JButton submitButton;

    /**
     * The scroll pane for the Bachelor's Thesis interface
     */
    private JScrollPane bachelorScroll;

    /*
     * Constructor and ActionListener
     * Checks if the user has finished a Project Work. If the user has already finished a Project Work,
     * they can be assigned a Bachelor's Thesis. Otherwise, they will receive an error message.
     * Checks if the user is already assigned to a Bachelor's Thesis; if so, displays an error message.
     * If the user is not already assigned, the selected date will be copied into the database.
     */
    public StudentBachelor() {
        border("Bachelor's Thesis", bachelorPanel);
        setJMenuBar(menuBar());
        getThemeNoStudents("Bachelor's Thesis", themeSelection);

        Dimension size = bachelorScroll.getPreferredSize();
        bachelorScroll.setViewportView(studentWork("Bachelor's Thesis"));
        bachelorScroll.setPreferredSize(size);

        submitButton.addActionListener(_ -> {
            String usernameTemp = (LogIn.usernameLogin != null) ? LogIn.usernameLogin : Register.usernameRegister;
            String themeTemp = Objects.requireNonNull(themeSelection.getSelectedItem()).toString();

            try {
                Connection connection = DriverManager.getConnection(General.databaseUrl, General.databaseName, General.databasePassword);
                String checkProjectSql = "SELECT finished, type FROM themes WHERE student_id = (SELECT user_id FROM users WHERE username = ?)";
                PreparedStatement checkProjectStmt = connection.prepareStatement(checkProjectSql);
                checkProjectStmt.setString(1, usernameTemp);
                ResultSet projectResultSet = checkProjectStmt.executeQuery();

                boolean projectWorkCompleted = false;

                while (projectResultSet.next()) {
                    if (projectResultSet.getString("finished").equals("yes") && projectResultSet.getString("type").equals("Project Work")) {
                        projectWorkCompleted = true;
                        break;
                    }
                }

                if (projectWorkCompleted) {
                    String checkSql = "SELECT COUNT(*) AS count FROM themes WHERE student_id = (SELECT user_id FROM users WHERE username = ?) AND finished = 'no'";
                    PreparedStatement checkStatement = connection.prepareStatement(checkSql);
                    checkStatement.setString(1, usernameTemp);
                    ResultSet resultSet = checkStatement.executeQuery();

                    if (resultSet.next() && resultSet.getInt("count") > 0) {
                        JOptionPane.showMessageDialog(null, "You are already registered for a work.");
                        return;
                    }

                    String updateSql = "UPDATE themes SET student_id = (SELECT user_id FROM users WHERE username = ?) WHERE theme_name = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateSql);
                    updateStatement.setString(1, usernameTemp);
                    updateStatement.setString(2, themeTemp);
                    updateStatement.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Bachelor's Thesis assigned successfully!");
                    bachelorScroll.setViewportView(studentWork("Bachelor's Thesis"));
                    getThemeNoStudents("Bachelor's Thesis", themeSelection);

                } else {
                    JOptionPane.showMessageDialog(null, "Please finish a Project Work first!");
                }
                connection.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}