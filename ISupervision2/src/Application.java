/*
 * Project: ISupervision
 * Author: Sarah Berger
 * Last Change: 26.05.2024
 */

import javax.swing.*;

/**
 * This class is responsible for starting the program and opening the start page (Index).
 */
public class Application extends JFrame {
    public static void main(String[] args) {
        Index frame = new Index();
        frame.setVisible(true);
    }
}
