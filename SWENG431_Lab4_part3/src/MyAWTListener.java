import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * MyAwtListener class implements the interface AWTEventListener. This class provides
 * the logic for the listener.
 *
 * @author john
 */
public class MyAWTListener implements AWTEventListener {

    // An array list of the GUI object - cannot use List interface for AWTEvent
    ArrayList<AWTEvent> listener = new ArrayList<>();

    // Placeholder for the AutomatedTesting class
    private final AutomatedTesting test;

    // Constructor
    public MyAWTListener(AutomatedTesting test) {
        this.test = test;
    }

    @Override
    public void eventDispatched(AWTEvent awtEvent) {
        // Is accessed if the list size is < 4 and the mouse is pressed
        if ((listener.size() < 4) && (awtEvent.getID() == MouseEvent.MOUSE_PRESSED)) {
            listener.add(awtEvent);
            /*
             * Checks the list size after the last object was added to the list.
             * If this is done outside of this block, starting test will be called more
             * than once.
             */
            if (listener.size() == 4) {
                test.startTesting(listener);
            }
        }
    }
}
