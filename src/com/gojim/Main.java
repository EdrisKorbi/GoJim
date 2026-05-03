package com.gojim;

import com.gojim.ui.LoginFrame;

/**
 * Application entry point.
 * We use SwingUtilities.invokeLater to start Swing safely on the Event Dispatch
 * Thread.
 */
public class Main {
    public static void main(String[] args) {
        LoginFrame frame = new LoginFrame();
        frame.setVisible(true);
    }
}