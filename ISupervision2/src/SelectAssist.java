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
import java.util.ArrayList;
import java.util.Objects;

/**
 * This class allows an admin to select an assistant from a list of available assistants.
 * The selected assistant is then assigned to the admin in the database.
 */
public class SelectAssist extends MainAdmin {
    /**
     * The panel for the select assistant interface
     */
    private JPanel votePanel;

    /**
     * The JComboBox for selecting an assistant
     */
    private JComboBox<String> selectAssist;

    /**
     * The button for submitting the selected assistant
     */
    private JButton submit;

    /**
     * Constructor and ActionListener
     * Initializes the Select Assistant window and sets up the action listener for the submit button.
     * When the submit button is clicked, the selected assistant is assigned to the admin in the database.
     */
    public SelectAssist() {
        borderNormal("Select Assistant", votePanel);
        setJMenuBar(menuBar());
        getSelectAssist(selectAssist);

        submit.addActionListener(_ -> {
            String usernameTemp = (LogIn.usernameLogin != null) ? LogIn.usernameLogin : Register.usernameRegister;
            String assist = Objects.requireNonNull(selectAssist.getSelectedItem()).toString();
            String[] parts = assist.split(" ", 2);
            String firstName = parts[0].trim();
            String lastName = parts[1].trim();

            try {
                Connection connection = DriverManager.getConnection(General.databaseUrl, General.databaseName, General.databasePassword);

                String sql = "INSERT INTO admin_assistants (admin_id, assistant_id) VALUES ((SELECT user_id FROM users WHERE username = ?), (SELECT user_id FROM users WHERE firstName = ? AND lastName = ?))";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, usernameTemp);
                preparedStatement.setString(2, firstName);
                preparedStatement.setString(3, lastName);
                preparedStatement.executeUpdate();

                JOptionPane.showMessageDialog(null, "Successfully added assistant!");

                getSelectAssist(selectAssist);

                preparedStatement.close();
                connection.close();

            } catch (Exception a) {
                a.printStackTrace();
            }
        });
    }

    /**
     * Populates the JComboBox with a list of available assistants who are not already assigned to an admin.
     *
     * @param selectAssist the JComboBox to be populated with assistant names and IDs
     */
    public void getSelectAssist(JComboBox<String> selectAssist) {
        selectAssist.removeAllItems();
        ArrayList<String> isAssistant = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(General.databaseUrl, General.databaseName, General.databasePassword);

            String checkSql = "SELECT assistant_id FROM admin_assistants WHERE assistant_id IN (SELECT user_id FROM user_roles JOIN roles ON user_roles.role_id = roles.role_id WHERE role_name = 'assistent')";
            PreparedStatement preparedStatementCheck = connection.prepareStatement(checkSql);
            ResultSet resultSetCheck = preparedStatementCheck.executeQuery();
            while (resultSetCheck.next()) {
                String temp = resultSetCheck.getString("assistant_id");
                isAssistant.add(temp);
            }

            String sql = "SELECT u.user_id, u.firstName, u.lastName FROM users u JOIN user_roles ur ON u.user_id = ur.user_id JOIN roles r ON r.role_id = ur.role_id WHERE r.role_name = 'assistent'";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (!isAssistant.contains(resultSet.getString("user_id"))) {
                    String firstName = resultSet.getString("firstName");
                    String lastName = resultSet.getString("lastName");
                    selectAssist.addItem(firstName + " " + lastName);
                }
            }

            preparedStatement.close();
            resultSet.close();
            connection.close();
        } catch (Exception a) {
            a.printStackTrace();
        }
    }
}