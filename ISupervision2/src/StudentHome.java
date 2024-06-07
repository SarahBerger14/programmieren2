/*
 * Project: ISupervision
 * Author: Sarah Berger
 * Last Change: 26.05.2024
 */

import javax.swing.*;
import java.awt.*;

/**
 * The class is for the whole layout of the home window from the student view.
 */
public class StudentHome extends MainStudent {
    /**
     * The panel for the home interface
     */
    private JPanel studentPanel;

    /**
     * The scroll pane for the home interface
     */
    private JScrollPane homeScroll;

    /**
     * The headline for the home interface
     */
    private JLabel headline;

    /**
     * Constructor
     */
    public StudentHome() {
        border("Home", studentPanel);
        setJMenuBar(menuBar());
        String usernameTemp = (LogIn.usernameLogin != null) ? LogIn.usernameLogin : Register.usernameRegister;
        headline.setText("Welcome, " + usernameTemp + "!");

        Dimension size = homeScroll.getPreferredSize();
        homeScroll.setViewportView(studentAktiveWork());
        homeScroll.setPreferredSize(size);
    }


}
