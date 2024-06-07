/*
 * Project: ISupervision
 * Author: Sarah Berger
 * Last Change: 26.05.2024
 */

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * This class is for error handling and date picking.
 */
public class General {
    /**
     * The database connection url.
     */
    public static String databaseUrl = "jdbc:mysql://localhost:3306/isupervision";

    /**
     * The database username.
     */
    public static String databaseName = "Sarah";

    /**
     * The database password.
     */
    public static String databasePassword = "1964Sarah1";

    /**
     * This method takes the provided values and checks if they violate any validation criteria. If any validation errors are found,
     * a boolean called 'valid' will be set to false, and an error message will pop up, informing the user of the issue. The method
     * then returns the 'valid' boolean.
     *
     * @param firstName   The user's first name
     * @param secondName  The user's second name
     * @param username    The user's username
     * @param email       The user's email address
     * @param pass        The user's password
     * @param confPass    The confirmation of the user's password
     * @param loginStatus The user's login status
     * @return A boolean indicating whether the provided values are valid
     */
    public static boolean valid(String firstName, String secondName, String username, String email, String pass, String confPass, LoginStatus loginStatus) {
        boolean valid = true;
        if (firstName.isEmpty() || secondName.isEmpty() || email.isEmpty() || username.isEmpty() || pass.isEmpty() || confPass.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill all the required fields!", "Error", JOptionPane.ERROR_MESSAGE);
            valid = false;
        }
        if (!pass.equals(confPass)) {
            JOptionPane.showMessageDialog(null, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            valid = false;
        }
        if (pass.length() < 6) {
            JOptionPane.showMessageDialog(null, "Password must be at least 6 characters!", "Error", JOptionPane.ERROR_MESSAGE);
            valid = false;
        }
        if (pass.contains(" ")) {
            JOptionPane.showMessageDialog(null, "Password must not contain spaces!", "Error", JOptionPane.ERROR_MESSAGE);
            valid = false;
        }
        if (username.length() > 20) {
            JOptionPane.showMessageDialog(null, "Username is too long! Please enter less then 20 characters!", "Error", JOptionPane.ERROR_MESSAGE);
            valid = false;
        }
        if (!email.matches(".*\\.(ad|st|as)$") ||
                !email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}") ||
                email.indexOf('@') != email.lastIndexOf('@')) {
            JOptionPane.showMessageDialog(null, "Please enter a valid email address! Example: Max@Musterman.st/ad/as", "Error", JOptionPane.ERROR_MESSAGE);
            valid = false;
        }
        if (loginStatus == LoginStatus.SUCCESS || loginStatus == LoginStatus.INVALID_PASSWORD) {
            JOptionPane.showMessageDialog(null, "User already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            valid = false;
        }
        return valid;
    }

    /**
     * This method takes three JComboBox components and fills them with appropriate values for days, months, and years.
     * It also dynamically adjusts the days in the month based on the selected month and year, accounting for months
     * with 28, 29, 30, or 31 days.
     *
     * @param day   JComboBox for days
     * @param month JComboBox for months
     * @param year  JComboBox for years
     */
    public static void datePicker(JComboBox<String> day, JComboBox<String> month, JComboBox<Integer> year){
        String[] months = { "January", "February", "March", "April",
                "May", "June", "July", "August",
                "September", "October", "November", "December"
        };
        month.setModel(new DefaultComboBoxModel<>(months));

        Integer[] years = new Integer[3];
        for (int i = 0; i < 3; i++) {
            years[i] = 2025 + i;       }
        year.setModel(new DefaultComboBoxModel<>(years));

        String[] daysOld = new String[31];
        for (int i = 0; i < 31; i++) {
            daysOld[i] = String.format("%02d", i + 1);
        }
        day.setModel(new DefaultComboBoxModel<>(daysOld));

        ActionListener daysNew = _ -> {
            int max;
            if (Objects.requireNonNull(month.getSelectedItem()).toString().equals("January") || month.getSelectedItem().toString().equals("March") || month.getSelectedItem().toString().equals("May") || month.getSelectedItem().toString().equals("July") ||
                    month.getSelectedItem().toString().equals("August") || month.getSelectedItem().toString().equals("October") || month.getSelectedItem().toString().equals("December")) {
                max = 31;
            } else if (month.getSelectedItem().toString().equals("February") && (((int) Objects.requireNonNull(year.getSelectedItem()) % 4 == 0 && (int) year.getSelectedItem() % 100 != 0) || ((int) year.getSelectedItem() % 400 == 0))) {
                max = 29;
            } else if (month.getSelectedItem().toString().equals("February")) {
                max = 28;
            } else {
                max = 30;
            }

            String[] days = new String[max];
            for (int i = 0; i < max; i++) {
                days[i] = String.format("%02d", i + 1);
            }
            day.setModel(new DefaultComboBoxModel<>(days));};

        month.addActionListener(daysNew);
        year.addActionListener(daysNew);
    }
}