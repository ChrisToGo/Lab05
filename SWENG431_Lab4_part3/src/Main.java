import gui.MyGUI;

import java.awt.*;
import java.nio.file.Paths;

/**
 * This is the driver file for the application.
 *
 * @author john
 */
public class Main {
    public static void main(String[] args) {
        // Toolkit for testing GUI
        Toolkit tk = Toolkit.getDefaultToolkit();
        // Event listener class - pass Testing class with file path to input data
        MyAWTListener listener = new MyAWTListener(
                new AutomatedTesting(Paths.get("input.txt")));

        // The listener for the mouse event
        //tk.addAWTEventListener(listener, AWTEvent.MOUSE_EVENT_MASK);

        // Launches the GUI to be tested
        MyGUI gui = new MyGUI();
        gui.setSize(400,300);     // Just for size
        gui.setVisible(true);       // to make the window viewable

        //Invoke the testing tool to record stop play
        new TestingToolGUI();
    }
}