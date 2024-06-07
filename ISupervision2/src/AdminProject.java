/*
 * Project: ISupervision
 * Author: Sarah Berger
 * Last Change: 26.05.2024
 */

import javax.swing.*;

/**
 * The class is for the whole layout of the Project Work window from the admin view.
 */
public class AdminProject extends MainAdmin {
    /**
     * For creating a new project
     */
    private JButton createButton;

    /**
     * For deleting a project and its content
     */
    private JButton deleteButton;

    /**
     * For setting a limit for the project
     */
    private JButton setLimitButton;

    /**
     * For modifying a project
     */
    private JButton modifyButton;

    /**
     * The panel for the Project Work window
     */
    private JPanel projectPanel;

    /**
     * For viewing the projects
     */
    private JScrollPane currentProjects;

    /**
     * For setting a project as finished
     */
    private JButton finishedButton;

    /**
     * Constructor
     */
    public AdminProject() {
        borderNormal("Project Work", projectPanel);
        setJMenuBar(menuBar());
        String usernameTemp = (LogIn.usernameLogin != null) ? LogIn.usernameLogin : Register.usernameRegister;
        buttonAction(createButton, deleteButton, setLimitButton, modifyButton, finishedButton, currentProjects, "Project Work", usernameTemp);
        currentProjects.setViewportView(adminWork("Project Work", usernameTemp));
    }
}
