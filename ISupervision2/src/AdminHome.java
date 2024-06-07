/*
 * Project: ISupervision
 * Author: Sarah Berger
 * Last Change: 26.05.2024
 */

import javax.swing.*;
import java.awt.*;

/**
 * The class is for the whole layout of the home window from the admin view.
 */
public class AdminHome extends MainAdmin {
    /**
     * The panel for the admin home window
     */
    private JPanel adminHome;

    /**
     * The headline of the admin home window
     */
    private JLabel headline;

    /**
     * For viewing the active works
     */
    private JScrollPane aktiveWorks;

    /**
     * Constructor
     */
    public AdminHome() {
        borderNormal("Home", adminHome);
        setJMenuBar(menuBar());
        String usernameTemp = (LogIn.usernameLogin != null) ? LogIn.usernameLogin : Register.usernameRegister;
        headline.setText("Welcome, " + usernameTemp + "!");

        Dimension size = aktiveWorks.getPreferredSize();
        aktiveWorks.setViewportView(adminAktiveWork(usernameTemp));
        aktiveWorks.setPreferredSize(size);
    }

}
