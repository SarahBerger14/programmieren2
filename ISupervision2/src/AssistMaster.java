/*
 * Project: ISupervision
 * Author: Sarah Berger
 * Last Change: 26.05.2024
 */

import javax.swing.*;

/**
 * The class is for the whole layout of the Master's Thesis window from the assistant view.
 */
public class AssistMaster extends MainAssist {
    /**
     * For viewing the master's theses
     */
    private JScrollPane currentThesis;

    /**
     * For creating a new master's thesis
     */
    private JButton createButton;

    /**
     * For setting a limit for the master's theses
     */
    private JButton setLimitButton;

    /**
     * The panel for the master's thesis window
     */
    private JPanel masterPanel;

    /**
     * Constructor
     */
    public AssistMaster() {
        borderNormal("Master's Thesis", masterPanel);
        setJMenuBar(menuBar());
        String usernameAdmin = getAdmin();
        buttonAction(createButton, setLimitButton, currentThesis, "Master's Thesis", usernameAdmin);
        JScrollPane master = MainAdmin.adminWork("Master's Thesis", usernameAdmin);
        currentThesis.setViewportView(master);
    }
}