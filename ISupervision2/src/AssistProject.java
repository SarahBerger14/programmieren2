/*
 * Project: ISupervision
 * Author: Sarah Berger
 * Last Change: 26.05.2024
 */

import javax.swing.*;

/**
 * The class is for the whole layout of the Project Work window from the assistant view.
 */
public class AssistProject extends MainAssist {
    /**
     * For viewing, the project works
     */
    private JScrollPane currentThesis;

    /**
     * For creating a new project work
     */
    private JButton createButton;

    /**
     * For setting a limit for the project works
     */
    private JButton setLimitButton;

    /**
     * The panel for the project work window
     */
    private JPanel projectAsPanel;

    /**
     * Constructor
     */
    public AssistProject() {
        borderNormal("Project Work", projectAsPanel);
        setJMenuBar(menuBar());

        String usernameAdmin = getAdmin();
        buttonAction(createButton, setLimitButton, currentThesis, "Project Work", usernameAdmin);
        JScrollPane project = MainAdmin.adminWork("Project Work", usernameAdmin);
        currentThesis.setViewportView(project);
    }
}
