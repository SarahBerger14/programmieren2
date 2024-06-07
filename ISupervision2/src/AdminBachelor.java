/*
 * Project: ISupervision
 * Author: Sarah Berger
 * Last Change: 26.05.2024
 */
import javax.swing.*;

/**
 * The class is for the whole layout of the Bachelor's Thesis window from the admin view.
 */
public class AdminBachelor extends MainAdmin {
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
     * For setting a limit for the Bachelor's Thesis
     */
    private JButton setLimitButton;

    /**
     * The panel for the Bachelor's Thesis window
     */
    private JPanel bachelorPanel;

    /**
     * The viewing the bachelor's theses
     */
    private JScrollPane currentThesis;

    /**
     * For setting a theme as finished
     */
    private JButton finishedButton;

    /**
     * Constructor
     */
    public AdminBachelor() {
        borderNormal("Bachelor's Thesis", bachelorPanel);
        setJMenuBar(menuBar());
        String usernameTemp = (LogIn.usernameLogin != null) ? LogIn.usernameLogin : Register.usernameRegister;
        buttonAction(createButton, deleteButton, setLimitButton, modifyButton, finishedButton,currentThesis,"Bachelor's Thesis", usernameTemp);
        currentThesis.setViewportView(adminWork("Bachelor's Thesis", usernameTemp));
    }
}
