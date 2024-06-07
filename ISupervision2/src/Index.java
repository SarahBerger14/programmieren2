/*
 * Project: ISupervision
 * Author: Sarah Berger
 * Last Change: 26.05.2024
 */

import javax.swing.*;

/**
 * This class represents the initial frame that opens when the program starts.
 */
public class Index extends JFrame {
    /**
     * The button for navigating to the login window
     */
    public JButton loginButton;

    /**
     * The button for navigating to the register window
     */
    public JButton registerButton1;

    /**
     * The panel for the index window
     */
    public JPanel indexPanel;

    /**
     * Constructor that sets up the action listeners for the buttons.
     * It contains two buttons: one for navigating to the login window and another for navigating to the register window.
     */
    public Index() {

        ImageIcon icon = new ImageIcon("src/media/eye.png");
        setIconImage(icon.getImage());
        setContentPane(indexPanel);
        setTitle("Index");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        registerButton1.addActionListener(_ -> {
            Register registerWindow = new Register();
            registerWindow.setVisible(true);
            Index.this.dispose();
        });

        loginButton.addActionListener(_ -> {
            LogIn logInWindow = new LogIn();
            logInWindow.setVisible(true);
            Index.this.dispose();
        });
    }
}