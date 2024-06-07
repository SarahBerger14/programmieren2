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
 * This class handles updating the deadline for a specific theme.
 */
public class ModifyButton extends MainAdmin{
    /**
     * The panel for the modification interface
     */
    private JPanel modifyPanel;

    /**
     * The combo box for selecting the theme
     */
    private JComboBox<String> themeSelection;

    /**
     * The combo box for selecting the year
     */
    private JComboBox<Integer> yearDeadline;

    /**
     * The combo box for selecting the month
     */
    private JComboBox<String> monthDeadline;

    /**
     * The combo box for selecting the day
     */
    private JComboBox<String> dayDeadline;

    /**
     * The button for submitting the changes
     */
    private JButton submitButton;

    /**
     * Constructor that sets up the modified deadline interface and its action listeners.
     * Initializes the window design and functionality for updating the deadline of a theme.
     *
     * @param currentView the current type (e.g., Bachelor's Thesis) where the button was clicked
     */
    public ModifyButton(String currentView) {
       borderButton("Modify", modifyPanel);
        General.datePicker(dayDeadline, monthDeadline, yearDeadline);
        String usernameTemp = (LogIn.usernameLogin != null) ? LogIn.usernameLogin : Register.usernameRegister;
        getThemeComboBox(currentView, themeSelection, usernameTemp);

        submitButton.addActionListener(_ -> {
            String themeTemp = Objects.requireNonNull(themeSelection.getSelectedItem()).toString();
            String deadlineTemp = Objects.requireNonNull(dayDeadline.getSelectedItem()) + "." + Objects.requireNonNull(monthDeadline.getSelectedItem()) + "." + Objects.requireNonNull(yearDeadline.getSelectedItem());

            try {
                Connection connection = DriverManager.getConnection(General.databaseUrl, General.databaseName, General.databasePassword);

                String sql = "UPDATE themes SET deadline = ? WHERE theme_name = ? ";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, deadlineTemp);
                preparedStatement.setString(2, themeTemp);
                preparedStatement.executeUpdate();

                JOptionPane.showMessageDialog(null, "Successfully Updated!");
                this.dispose();
                connection.close();

            } catch (Exception a) {
                a.printStackTrace();
            }
        });
    }
}