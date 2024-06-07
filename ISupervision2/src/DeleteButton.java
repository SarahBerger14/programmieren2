/*
 * Project: ISupervision
 * Author: Sarah Berger
 * Last Change: 26.05.2024
 */

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Objects;

/**
 * This class is for deleting a theme and its information.
 */
public class DeleteButton extends MainAdmin {
    /**
     * The button for deleting a theme and its information
     */
    private JButton deleteButton;

    /**
     * The combobox for selecting a theme
     */
    private JComboBox<String> themeSelection;

    /**
     * The panel for deleting a theme and its information
     */
    private JPanel deletePanel;

    /**
     * The checkbox for confirming the deletion
     */
    private JCheckBox checkbox;

    /**
     * Constructor and ActionListener
     * Deletes a theme and its information from the themes table, and decrements the current work count in the limits table by one.
     * The Checkbox has to be selected, if not, a message will be shown.
     *
     * @param panel        the current window where the button was clicked, that shows the themes
     * @param currentView  the current type (e.g., Bachelor's Thesis) where the button was clicked
     */
    public DeleteButton(JScrollPane panel, String currentView) {
        borderButton("Delete", deletePanel);
        String username = (LogIn.usernameLogin != null) ? LogIn.usernameLogin : Register.usernameRegister;
      getThemeComboBox(currentView, themeSelection,username);

        deleteButton.addActionListener(_ -> {
            boolean check = checkbox.isSelected();
            String themeTemp = Objects.requireNonNull(themeSelection.getSelectedItem()).toString();

            try {
                Connection connection = DriverManager.getConnection(General.databaseUrl, General.databaseName, General.databasePassword);

                if(check) {
                    String deleteSql = "DELETE FROM themes WHERE theme_name = ? AND admin_id = (SELECT user_id FROM users WHERE username = ?)";
                    PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
                    deleteStatement.setString(1, themeTemp);
                    deleteStatement.setString(2, username);
                    deleteStatement.executeUpdate();

                    String updateSql = "UPDATE limits SET currentCount = currentCount - 1 WHERE type = ? AND user_id = (SELECT user_id FROM users WHERE username = ?)";
                    PreparedStatement updateStatement = connection.prepareStatement(updateSql);
                    updateStatement.setString(1, currentView);
                    updateStatement.setString(2, username);
                    updateStatement.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Successfully deleted a theme and its information!");
                    panel.setViewportView(adminWork(currentView, username));
                    this.dispose();

                } else {
                    JOptionPane.showMessageDialog(null, "Checkbox is not selected!");
                }
                connection.close();
            } catch (Exception a) {
                a.printStackTrace();
            }
        });
    }
}
