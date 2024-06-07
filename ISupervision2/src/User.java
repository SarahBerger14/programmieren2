/*
 * Project: ISupervision
 * Author: Sarah Berger
 * Last Change: 26.05.2024
 */

import java.sql.*;

/**
 * The class if for creating a student/a role and getting the users information
 */
public class User {
    /**
     * The username of the current user
     */
    public String username;

    /**
     * The email address of the current user
     */
    public String email;

    /**
     * Is for creating a new user if the person is not registered yet
     *
     * @param firstName  the first name of the user
     * @param secondName the last name of the user
     * @param username   the username of the user
     * @param email      the email address of the user
     * @param pass       the password of the user
     * @return the newly created User object if successful, otherwise null
     */
    public static User createUser(String firstName, String secondName, String username, String email, String pass) {
        User user = null;
        try {
            Connection connection = DriverManager.getConnection(General.databaseUrl, General.databaseName, General.databasePassword);
            Statement statement = connection.createStatement();
            String sql = "INSERT INTO users (firstName, lastName, username, email, password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, secondName);
            preparedStatement.setString(3, username);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, pass);

            int row = preparedStatement.executeUpdate();
            if (row > 0) {
                user = new User();
                user.username = username;
                user.email = email;
            }
            statement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * Is for making a new temp user with the current user's username and email
     *
     * @param username the username of the user
     * @return the User object with the given username if found, otherwise null
     */
    public static User setData(String username) {
        User user = null;
        try {
            Connection connection = DriverManager.getConnection(General.databaseUrl, General.databaseName, General.databasePassword);
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.username = resultSet.getString("username");
                user.email = resultSet.getString("email");
            }
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Creates a new role with the given role name.
     *
     * @param role_name the name of the role to be created
     */
    public void createRole(String role_name) {
        try {
            Connection connection = DriverManager.getConnection(General.databaseUrl, General.databaseName, General.databasePassword);
            String sql = "INSERT INTO roles(role_name)VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, role_name);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the username of the user.
     *
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }
}