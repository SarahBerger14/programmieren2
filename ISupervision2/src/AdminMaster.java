/*
 * Project: ISupervision
 * Author: Sarah Berger
 * Last Change: 26.05.2024
 */
import javax.swing.*;

/**
 * The class is for the whole layout of the Master's Thesis window from the admin view.
 */
public class AdminMaster extends MainAdmin {
    /**
     * For creating a new theme
     */
    private JButton createButton;

    /**
     * For deleting a theme and its content
     */
    private JButton deleteButton;

    /**
     * For modifying a theme
     */
    private JButton modifyButton;

    /**
     * For setting a limit for the Master's Thesis
     */
    private JButton setLimitButton;

    /**
     * The panel for the Master's Thesis window
     */
    private JPanel masterPanel;

    /**
     * For viewing the master's theses
     */
    private JScrollPane currentThesis;

    /**
     * For setting a theme as finished
     */
    private JButton finishedButton;

    /**
     * Constructor
     */
    public AdminMaster() {
        borderNormal("Master's Thesis", masterPanel);
        setJMenuBar(menuBar());
        String usernameTemp = (LogIn.usernameLogin != null) ? LogIn.usernameLogin : Register.usernameRegister;
        buttonAction(createButton, deleteButton, setLimitButton, modifyButton, finishedButton, currentThesis, "Master's Thesis", usernameTemp);
        currentThesis.setViewportView(adminWork("Master's Thesis", usernameTemp));
    }
}
