/*
 * Project: ISupervision
 * Author: Sarah Berger
 * Last Change: 26.05.2024
 */

import javax.swing.*;

/**
 * The class is for the whole layout of the Bachelor's Thesis window from the assistant view.
 */
public class AssistBachelor extends MainAssist {
    /**
     * For viewing the bachelor's theses
     */
    private JScrollPane currentThesis;

    /**
     * For creating a new bachelor's thesis
     */
    private JButton createButton;

    /**
     * For setting a limit for the bachelor's theses
     */
    private JButton setLimitButton;

    /**
     * The panel for the bachelor's these window
     */
    private JPanel bachPanel;

    /**
     * Constructor
     */
    public AssistBachelor() {
        borderNormal("Bachelor's Thesis", bachPanel);
        setJMenuBar(menuBar());

        String usernameAdmin = getAdmin();
        buttonAction(createButton, setLimitButton, currentThesis, "Bachelor's Thesis", usernameAdmin);
        JScrollPane bachelor = MainAdmin.adminWork("Bachelor's Thesis", usernameAdmin);
        currentThesis.setViewportView(bachelor);

    }
}
