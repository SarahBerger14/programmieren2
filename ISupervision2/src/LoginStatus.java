/*
 * Project: ISupervision
 * Author: Sarah Berger
 * Last Change: 26.05.2024
 */

import java.sql.*;

/**
 * The LoginStatus enum represents the possible outcomes of a login attempt.
 */
public enum LoginStatus {
    /**
     * The login attempt was successful.
     */
    SUCCESS,

    /**
     * The username does not exist.
     */
    INVALID_USERNAME,

    /**
     * The password is incorrect.
     */
    INVALID_PASSWORD;

    /**
     * Checks if the provided username and password are correct. If everything is correct, it returns SUCCESS. If the username
     * does not exist or the password is incorrect, it returns the corresponding status.
     * This method is also useful for registration to check if the username already exists.
     *
     * @param username the username to be checked
     * @param password the password to be checked
     * @return the login status indicating the result of the login attempt
     */
    public static LoginStatus login(String username, String password) {
        LoginStatus loginStatus = LoginStatus.INVALID_USERNAME;

        try {
            Connection connection = DriverManager.getConnection(General.databaseUrl, General.databaseName, General.databasePassword);
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                resultSet.getString("password");
                if (password.equals(resultSet.getString("password"))) {
                    loginStatus = LoginStatus.SUCCESS;
                } else {
                    loginStatus = LoginStatus.INVALID_PASSWORD;
                }
            }
            statement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginStatus;
    }
}