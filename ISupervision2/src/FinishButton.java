/*
 * Project: ISupervision
 * Author: Sarah Berger
 * Last Change: 26.05.2024
 */

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * This class is for verifying that a student has finished their work.
 */
public class FinishButton extends MainAdmin {
    /**
     * The panel for verifying that a student has finished their work
     */
    private JPanel finishPanel;

    /**
     * The combobox for selecting a theme
     */
    private JComboBox<String> themeSelect;

    /**
     * The button for verifying that a student has finished their work
     */
    private JButton submitButton;

    /**
     * Constructor and ActionListener
     * When the finish button is clicked, the selected theme will be marked as finished.
     * The 'finished' attribute in the themes table will be updated to 'yes'.
     *
     * @param panel       the current window where the button was clicked, that shows the themes
     * @param currentView the current type (e.g., Bachelor's Thesis) where the button was clicked
     */
    public FinishButton(JScrollPane panel, String currentView) {
        borderButton("Finish", finishPanel);
        String username = (LogIn.usernameLogin != null) ? LogIn.usernameLogin : Register.usernameRegister;
        getFinishedSelect(currentView, themeSelect, username);

        submitButton.addActionListener(_ -> {
            String themeTemp = (String) themeSelect.getSelectedItem();

            try {
                Connection connection = DriverManager.getConnection(General.databaseUrl, General.databaseName, General.databasePassword);
                String sql = "UPDATE themes SET finished = 'yes' WHERE admin_id = (SELECT user_id FROM users WHERE username = ?) AND theme_name = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, themeTemp);
                preparedStatement.executeUpdate();

                JOptionPane.showMessageDialog(finishPanel, "Successfully updated a theme to finished!");
                panel.setViewportView(adminWork(currentView, username));
                this.dispose();

                connection.close();
            } catch (Exception a) {
                a.printStackTrace();
            }
        });
    }
}
