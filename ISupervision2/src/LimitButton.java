/*
 * Project: ISupervision
 * Author: Sarah Berger
 * Last Change: 26.05.2024
 */

import javax.swing.*;
import java.sql.*;

/**
 * This class allows an admin or assistant to set a limit on the number of works they can create.
 */
public class LimitButton extends MainAdmin {
    /**
     * The button for setting the limit
     */
    private JButton setLimitButton;

    /**
     * The panel for the limit selection interface
     */
    private JPanel limitPanel;

    /**
     * The combo box for selecting the limit
     */
    private JComboBox<Integer> limitN;

    /**
     * Constructor that sets up the limit selection interface and its action listener.
     * Creates a new JComboBox with numbers 1-15. Check if there is already a limit set.
     * If a limit exists, it ensures the new limit is higher than the current limit before updating it.
     * If no limit exists, it inserts the new limit into the limits table.
     *
     * @param currentView  the current type (e.g., Bachelor's Thesis) for which the limit is being set
     * @param usernameTemp the username of the admin setting the limit
     */
    public LimitButton(String currentView, String usernameTemp) {
        borderButton("Limit", limitPanel);

        Integer[] limitNumber = new Integer[15];
        for (int i = 0; i < limitNumber.length; i++) {
            limitNumber[i] = i + 1;
        }
        limitN.setModel(new DefaultComboBoxModel<>(limitNumber));

        setLimitButton.addActionListener(_ -> {

            int limitWork = 1 + limitN.getSelectedIndex();

            try {
                Connection connection = DriverManager.getConnection(General.databaseUrl, General.databaseName, General.databasePassword);

                String checkSql = "SELECT COUNT(*) FROM limits WHERE type = ? AND user_id = (SELECT user_id FROM users WHERE username = ?)";
                try (PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {
                    checkStatement.setString(1, currentView);
                    checkStatement.setString(2, usernameTemp);
                    try (ResultSet resultSet = checkStatement.executeQuery()) {
                        resultSet.next();
                        int count = resultSet.getInt(1);

                        if (count > 0) {
                            String checkMaxSql = "SELECT max_count FROM limits WHERE type = ? AND user_id = (SELECT user_id FROM users WHERE username = ?)";
                            try (PreparedStatement checkMaxStatement = connection.prepareStatement(checkMaxSql)) {
                                checkMaxStatement.setString(1, currentView);
                                checkMaxStatement.setString(2, usernameTemp);
                                try (ResultSet resultSet1 = checkMaxStatement.executeQuery()) {
                                    if (resultSet1.next()) {
                                        int maxCount = resultSet1.getInt("max_count");

                                        if (maxCount < limitWork) {
                                            String updateSql = "UPDATE limits SET max_count = ? WHERE type = ? AND user_id = (SELECT user_id FROM users WHERE username = ?)";
                                            try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                                                updateStatement.setInt(1, limitWork);
                                                updateStatement.setString(2, currentView);
                                                updateStatement.setString(3, usernameTemp);
                                                updateStatement.executeUpdate();
                                                JOptionPane.showMessageDialog(null, "Successfully updated limit the limit!");
                                                this.dispose();
                                            }
                                        } else {
                                            JOptionPane.showMessageDialog(null, "Please enter a higher limit than the current limit!");
                                        }
                                    }
                                }
                            }
                        } else {
                            String insertSql = "INSERT INTO limits (user_id, type, max_count) SELECT user_id, ?, ? FROM users WHERE username = ?";
                            try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                                insertStatement.setString(1, currentView);
                                insertStatement.setInt(2, limitWork);
                                insertStatement.setString(3, usernameTemp);
                                insertStatement.executeUpdate();
                                JOptionPane.showMessageDialog(null, "Successfully added a limit!");
                                this.dispose();
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}