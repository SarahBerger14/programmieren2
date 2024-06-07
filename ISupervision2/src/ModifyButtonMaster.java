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
 * This class is responsible for updating the deadline and exam date for a Master's Thesis.
 */
public class ModifyButtonMaster extends MainAdmin {
    /**
     * The combo box for selecting the theme
     */
    private JComboBox<String> themeSelection;

    /**
     * The combo box for selecting the year of the deadline
     */
    private JComboBox<Integer> yearDeadline;

    /**
     * The combo box for selecting the month of the deadline
     */
    private JComboBox<String> monthDeadline;

    /**
     * The combo box for selecting the day of the deadline
     */
    private JComboBox<String> dayDeadline;

    /**
     * The combo box for selecting the year of the exam date
     */
    private JComboBox<Integer> yearExam;

    /**
     * The combo box for selecting the day of the exam date
     */
    private JComboBox<String> dayExam;

    /**
     * The combo box for selecting the month of the exam date
     */
    private JComboBox<String> monthExam;

    /**
     * The button for submitting the changes
     */
    private JButton submitButton;

    /**
     * The panel for the modification interface
     */
    private JPanel masterModify;

    /**
     * Constructor that sets up the modified deadline and exam date interface and its action listeners.
     * Initializes the window design and functionality for updating the deadline and exam date of Master's Thesis themes.
     */
    public ModifyButtonMaster() {
        String usernameTemp = (LogIn.usernameLogin != null) ? LogIn.usernameLogin : Register.usernameRegister;
        borderButton("Modify", masterModify);
        General.datePicker(dayDeadline, monthDeadline, yearDeadline);
        General.datePicker(dayExam, monthExam, yearExam);
        getThemeComboBox("Master's Thesis", themeSelection, usernameTemp);

        submitButton.addActionListener(_ -> {
            String themeTemp = Objects.requireNonNull(themeSelection.getSelectedItem()).toString();
            String deadlineTemp = Objects.requireNonNull(dayDeadline.getSelectedItem()) + "." + Objects.requireNonNull(monthDeadline.getSelectedItem()) + "." + Objects.requireNonNull(yearDeadline.getSelectedItem());
            String examDateTemp = Objects.requireNonNull(dayExam.getSelectedItem()) + "." + Objects.requireNonNull(monthExam.getSelectedItem()) + "." + Objects.requireNonNull(yearExam.getSelectedItem());

            try {
                Connection connection = DriverManager.getConnection(General.databaseUrl, General.databaseName, General.databasePassword);

                String sql = "UPDATE themes SET deadline = ?, exam_date = ? WHERE theme_name = ? ";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, deadlineTemp);
                preparedStatement.setString(2, examDateTemp);
                preparedStatement.setString(3, themeTemp);
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
