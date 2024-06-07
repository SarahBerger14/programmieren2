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
 * The class is for the whole layout of the Master's Thesis window from the student view and the action for the submit button.
 */
public class StudentMaster extends MainStudent {
    /**
     * The JComboBox for selecting a theme
     */
    private JComboBox<String> themeSelection;

    /**
     * The button for submitting the selected theme
     */
    private JButton submitButton;

    /**
     * The panel for the Master's Thesis interface
     */
    private JPanel masterPanel;

    /**
     * The scroll pane for the Master's Thesis interface
     */
    private JScrollPane masterScroll;

    /**
     * Constructor and ActionListener
     * Checks if the user has finished a Bachelor's Thesis. If the user has already finished a Bachelor's Thesis,
     * they can be assigned a Master's Thesis. Otherwise, they will receive an error message.
     * Checks if the user is already assigned to a Master's Thesis; if so, displays an error message.
     * If the user is not already assigned, the selected date will be copied into the database.
     */
    public StudentMaster() {
        border("Master's Thesis", masterPanel);
        setJMenuBar(menuBar());
        getThemeNoStudents("Master's Thesis", themeSelection);

        Dimension size = masterScroll.getPreferredSize();
        masterScroll.setViewportView(studentWork("Master's Thesis"));
        masterScroll.setPreferredSize(size);

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
                    if (projectResultSet.getString("finished").equals("yes") && projectResultSet.getString("type").equals("Bachelor's Thesis")) {
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

                    JOptionPane.showMessageDialog(null,"Master's Thesis assigned successfully!");
                    masterScroll.setViewportView(studentWork("Master's Thesis"));
                    getThemeNoStudents("Master's Thesis", themeSelection);

                }
                else {
                    JOptionPane.showMessageDialog(null, "Please finish a Bachelor's Thesis first!");
                }
                connection.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
