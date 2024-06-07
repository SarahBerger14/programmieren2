/*
 * Project: ISupervision
 * Author: Sarah Berger
 * Last Change: 26.05.2024
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

/**
 * The class is for everything that has to do with the student.
 */
public class MainStudent extends JFrame {

    /**
     * Creates a new window with a specific title and design.
     *
     * @param text  the title of the window
     * @param panel the design of the new window
     */
    public void border(String text, JPanel panel) {
        ImageIcon icon = new ImageIcon("src/media/eye.png");
        setIconImage(icon.getImage());
        setTitle(text);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    /**
     * Creates a menu bar displaying various options for projects and home.
     *
     * @return a new menu bar
     */
    public static JMenuBar menuBar() {
        JMenuBar studentMenu = new JMenuBar();
        JMenu workMenu = new JMenu("Projects");
        JMenuItem projectWork = new JMenuItem("Project Work");
        JMenuItem bachelorthesis = new JMenuItem("Bachelor's Thesis");
        JMenuItem masterthesis = new JMenuItem("Master's Thesis");
        JMenu home = new JMenu("Home");
        JMenuItem studentHome = new JMenuItem("Student home");
        JMenu profile = new JMenu("Profile");
        JMenuItem updateProfile = new JMenuItem("Update Profile");

        workMenu.add(projectWork);
        workMenu.add(bachelorthesis);
        workMenu.add(masterthesis);
        home.add(studentHome);
        profile.add(updateProfile);

        studentMenu.add(home);
        studentMenu.add(workMenu);
        studentMenu.add(profile);

        studentHome.addActionListener(_ -> {
            JFrame frame = new StudentHome();
            frame.setVisible(true);
            (SwingUtilities.getWindowAncestor(studentMenu)).dispose();
        });
        projectWork.addActionListener(_ -> {
            JFrame frame = new StudentProject();
            frame.setVisible(true);
            (SwingUtilities.getWindowAncestor(studentMenu)).dispose();
        });
        bachelorthesis.addActionListener(_ -> {
            JFrame frame = new StudentBachelor();
            frame.setVisible(true);
            (SwingUtilities.getWindowAncestor(studentMenu)).dispose();
        });
        masterthesis.addActionListener(_ -> {
            JFrame frame = new StudentMaster();
            frame.setVisible(true);
            (SwingUtilities.getWindowAncestor(studentMenu)).dispose();
        });
        updateProfile.addActionListener(_ -> {
            JFrame frame = new Profile("student");
            frame.setVisible(true);
            (SwingUtilities.getWindowAncestor(studentMenu)).dispose();
        });
        return studentMenu;
    }

    /**
     * Shows the themes that no student is assigned to. Also displays the admin name, deadline, and for Master's Thesis, the exam date.
     *
     * @param type the type of work (Bachelor's Thesis, Master's Thesis, or Project Work)
     * @return a ScrollPane with the corresponding themes
     */
    public static JScrollPane studentWork(String type) {
        try {
            Connection connection = DriverManager.getConnection(General.databaseUrl, General.databaseName, General.databasePassword);
            String sql = "SELECT t.deadline, t.exam_date, t.theme_name, t.student_id, u.firstName, u.lastName FROM themes t JOIN users u ON t.admin_id = u.user_id WHERE t.type = ?;";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, type);
            ResultSet resultSet = statement.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Theme Name");
            model.addColumn("Professor Name");
            model.addColumn("Deadline");
            if (type.equals("Master's Thesis")) {
                model.addColumn("Exam Date");
            }

            while (resultSet.next()) {
                String deadline = resultSet.getString("deadline");
                String themeName = resultSet.getString("theme_name");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");

                resultSet.getInt("student_id");

                if (resultSet.wasNull()) {
                    Vector<String> row = new Vector<>();
                    row.add(themeName);
                    row.add(firstName + " " + lastName);
                    row.add(deadline != null ? deadline : "TBA");
                    if (type.equals("Master's Thesis")) {
                        String examDate = resultSet.getString("exam_date");
                        row.add(examDate != null ? examDate : "TBA");
                    }
                    model.addRow(row);
                }
            }

            JTable table = new JTable(model);

            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(250);
            columnModel.getColumn(1).setPreferredWidth(150);
            columnModel.getColumn(2).setPreferredWidth(150);
            if (type.equals("Master's Thesis")) {
                columnModel.getColumn(3).setPreferredWidth(150);
            }
            table.setPreferredScrollableViewportSize(new Dimension(table.getPreferredScrollableViewportSize().width, 300));

            JScrollPane scrollPane = new JScrollPane(table);
            table.setEnabled(false);

            resultSet.close();
            statement.close();
            connection.close();
            return scrollPane;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Shows the work that the student is assigned to.
     *
     * @return a ScrollPane with the student's active work
     */
    public static JScrollPane studentAktiveWork() {
        try {
            Connection connection = DriverManager.getConnection(General.databaseUrl, General.databaseName, General.databasePassword);
            String usernameTemp = (LogIn.usernameLogin != null) ? LogIn.usernameLogin : Register.usernameRegister;

            String sql = "SELECT t.type, t.deadline, t.exam_date, t.theme_name, t.admin_id, t.finished, a.firstName, a.lastName FROM themes t JOIN users u ON t.student_id = u.user_id JOIN users a ON t.admin_id = a.user_id WHERE u.username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, usernameTemp);
            ResultSet resultSet = statement.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Theme Name");
            model.addColumn("Type");
            model.addColumn("Deadline");
            model.addColumn("Exam Date");
            model.addColumn("Professor Name");

            while (resultSet.next()) {
                String themeName = resultSet.getString("theme_name");
                String typeName = resultSet.getString("type");
                String deadline = resultSet.getString("deadline");
                String examDate = resultSet.getString("exam_date");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String finished = resultSet.getString("finished");

                if (firstName != null && finished.equals("no")) {
                    Vector<String> row = new Vector<>();
                    row.add(themeName);
                    row.add(typeName);
                    row.add(deadline);
                    if (typeName.equals("Master's Thesis")) {
                        row.add(examDate != null ? examDate : "TBA");
                    } else {
                        row.add(examDate != null ? examDate : "-");
                    }
                    row.add(firstName + " " + lastName);
                    model.addRow(row);
                }
            }

            JTable table = new JTable(model);

            JScrollPane scrollPane = new JScrollPane(table);
            table.setEnabled(false);

            resultSet.close();
            statement.close();
            connection.close();

            return scrollPane;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Shows a JComboBox with all themes where no student is assigned, allowing a student to select a theme to be assigned to.
     *
     * @param currentView   the current type of work (e.g., Bachelor's Thesis)
     * @param themeComboBox the JComboBox to populate with available themes
     */
    public void getThemeNoStudents(String currentView, JComboBox<String> themeComboBox) {
        themeComboBox.removeAllItems();

        try {
            Connection connection = DriverManager.getConnection(General.databaseUrl, General.databaseName, General.databasePassword);

            String sql = "SELECT theme_name, student_id FROM themes WHERE type = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, currentView);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                resultSet.getString("student_id");
                if (resultSet.wasNull()) {
                    themeComboBox.addItem(resultSet.getString("theme_name"));
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
