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
 * This class is for creating a new theme.
 */
public class CreateButton extends MainAdmin {
    /**
     * The panel for creating a new theme
     */
    private JPanel createPanel;

    /**
     * The text field for the theme name
     */
    private JTextField createTheme;

    /**
     * The button for creating a new theme
     */
    private JButton createButton;

    /**
     * Constructor and ActionListener
     * If the limit set by the admin is exceeded, a message is shown. If the text field is empty, a message is shown.
     * If everything is okay, a new theme is inserted into the database in the themes table and currentCount is updated in the limits table.
     *
     * @param panel        the current window where the button was clicked, that shows the themes
     * @param currentView  the current type (e.g., Bachelor's Thesis) where the button was clicked
     * @param usernameTemp username of the admin
     */
    public CreateButton(JScrollPane panel, String currentView, String usernameTemp) {
        borderButton("Create", createPanel);
        createButton.addActionListener(_ -> {
            String themeName = createTheme.getText();

            try {
                Connection connection = DriverManager.getConnection(General.databaseUrl, General.databaseName, General.databasePassword);
                String checkSql = "SELECT max_count, currentCount FROM limits WHERE type = ? AND user_id = (SELECT user_id FROM users WHERE username = ?)";
                PreparedStatement checkStatement = connection.prepareStatement(checkSql);
                checkStatement.setString(1, currentView);
                checkStatement.setString(2, usernameTemp);
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next()) {
                    int max = resultSet.getInt("max_count");
                    int currentCount = resultSet.getInt("currentCount");

                    if (currentCount >= max) {
                        JOptionPane.showMessageDialog(null, "Limit reached!");
                    } else if (themeName.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please enter a theme!");
                    } else {
                        String insertSql = "INSERT INTO themes (admin_id, type, theme_name) SELECT user_id, ?, ? FROM users WHERE username = ?";
                        PreparedStatement insertStatement = connection.prepareStatement(insertSql);
                        insertStatement.setString(1, currentView);
                        insertStatement.setString(2, themeName);
                        insertStatement.setString(3, usernameTemp);
                        insertStatement.executeUpdate();

                        String updateSql = "UPDATE limits SET currentCount = currentCount + 1 WHERE type = ? AND user_id = (SELECT user_id FROM users WHERE username = ?)";
                        PreparedStatement updateStatement = connection.prepareStatement(updateSql);
                        updateStatement.setString(1, currentView);
                        updateStatement.setString(2, usernameTemp);
                        updateStatement.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Successfully created a new theme!");
                    }
                    panel.setViewportView(adminWork(currentView, usernameTemp));

                } else {
                    JOptionPane.showMessageDialog(null, "No limit found for selected type!");
                }
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.dispose();
        });
    }
}
