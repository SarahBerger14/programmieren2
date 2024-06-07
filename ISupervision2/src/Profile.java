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
 * This class is for updating user information.
 */
public class Profile extends JFrame{

    /**
     * The panel for the profile interface
     */
    private JPanel profilePanel;

    /**
     * The field for entering the new password
     */
    private JPasswordField enterNewPass;

    /**
     * The field for entering the username
     */
    private JTextField enterUsername;

    /**
     * The field for entering the last name
     */
    private JTextField enterLastName;

    /**
     * The button for updating the information
     */
    private JButton updateButton;

    /**
     * The field for entering the old password
     */
    private JPasswordField enterOldPass;

    /**
     * Constructor and ActionListener
     * The user can change the username, the last name and/or the password.
     * The old password has to be entered correctly to update everything.
     */
    public Profile(String role){
        ImageIcon icon = new ImageIcon("src/media/eye.png");
        setIconImage(icon.getImage());
        setTitle("Profile");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(profilePanel);
        setSize(800, 600);
        setLocationRelativeTo(null);

        switch (role) {
            case "admin" -> setJMenuBar(MainAdmin.menuBar());
            case "student" -> setJMenuBar(MainStudent.menuBar());
            case "assistant" -> setJMenuBar(MainAssist.menuBar());
        }

        updateButton.addActionListener(_ -> {
            String newPass = String.valueOf(enterNewPass.getPassword());
            String oldPass = String.valueOf(enterOldPass.getPassword());
            String username = enterUsername.getText();
            String lastName = enterLastName.getText();
            String usernameTemp = (LogIn.usernameLogin != null) ? LogIn.usernameLogin : Register.usernameRegister;
            boolean valid = false;
            try {
                Connection connection = DriverManager.getConnection(General.databaseUrl, General.databaseName, General.databasePassword);

                String sqlPas = "SELECT password FROM users WHERE username =?";
                PreparedStatement ps = connection.prepareStatement(sqlPas);
                ps.setString(1, usernameTemp);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    if (oldPass.equals(rs.getString("password"))) {
                        valid = true;
                    }
                }
                ps.close();

                if (!valid) {
                    JOptionPane.showMessageDialog(null, "The old password is not right!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (newPass.length() < 6 && !newPass.isEmpty() || newPass.contains(" ")) {
                    JOptionPane.showMessageDialog(null, "New password entered is too short", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!newPass.isEmpty()) {
                    String sql = "UPDATE users SET password=? WHERE username=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, newPass);
                    preparedStatement.setString(2, usernameTemp);
                    preparedStatement.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Successfully updated the password!");
                }

                if (username.length() > 20) {
                    JOptionPane.showMessageDialog(null, "Username is too long", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!username.isEmpty()) {
                    String sql = "UPDATE users SET username=? WHERE username=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, usernameTemp);
                    preparedStatement.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Successfully updated the username!");

                    usernameTemp = username;
                    if (LogIn.usernameLogin != null) {
                        LogIn.setUsernameLogin(username);
                    } else {
                        Register.setUsernameRegister(username);
                    }
                }

                if (!lastName.isEmpty()) {
                    String sql = "UPDATE users SET lastname=? WHERE username=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, lastName);
                    preparedStatement.setString(2, usernameTemp);
                    preparedStatement.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Successfully updated the last name!");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
}}