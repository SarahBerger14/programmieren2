/*
 * Project: ISupervision
 * Author: Sarah Berger
 * Last Change: 26.05.2024
 */

import javax.swing.*;
import java.awt.*;

/**
 * The class is for the whole layout of the home window from the assistant view.
 */
public class AssistHome extends MainAssist {
    /**
     * The headline for the home window
     */
    private JLabel headline;

    /**
     * For viewing the active works
     */
    private JScrollPane aktiveWorks;

    /**
     * The panel for the home window
     */
    private JPanel assistPanel;

    /**
     * Constructor
     */
    public AssistHome() {
        borderNormal("Home", assistPanel);
        setJMenuBar(menuBar());
        String usernameTemp = (LogIn.usernameLogin != null) ? LogIn.usernameLogin : Register.usernameRegister;
        headline.setText("Welcome, " + usernameTemp + "!");
        String usernameAdmin = getAdmin();
        Dimension size = aktiveWorks.getPreferredSize();
        aktiveWorks.setViewportView(MainAdmin.adminAktiveWork(usernameAdmin));
        aktiveWorks.setPreferredSize(size);
    }
}
