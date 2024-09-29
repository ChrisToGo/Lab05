import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class TestingToolGUI extends JFrame {

    // A list to hold both mouse and keyboard events
    private ArrayList<InputEventWrapper> recordedEvents = new ArrayList<>();
    private long startTime;
    private boolean recording = false;
    private Robot robot;

    public TestingToolGUI() {
        JButton recordButton = new JButton("Record");
        JButton stopButton = new JButton("Stop");
        JButton playButton = new JButton("Play");

        // Add buttons to the frame
        add(recordButton);
        add(stopButton);
        add(playButton);
        setLayout(new FlowLayout());

        // Set up frame properties
        setTitle("TestingTool");
        setSize(200, 200);
        setLocation(0, 0);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Initialize Robot for simulating mouse and keyboard events
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        // Add action listeners for the buttons
        recordButton.addActionListener(e -> startRecording());
        stopButton.addActionListener(e -> stopRecording());
        playButton.addActionListener(e -> playRecording());

        // Capture global mouse events
        Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
            if (event instanceof MouseEvent && recording) {
                recordMouseEvent((MouseEvent) event);
            }
        }, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);

        // Capture global keyboard events
        Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
            if (event instanceof KeyEvent && recording) {
                recordKeyEvent((KeyEvent) event);
            }
        }, AWTEvent.KEY_EVENT_MASK);
    }

    private void startRecording() {
        recordedEvents.clear();
        recording = true;
        startTime = System.currentTimeMillis();
        System.out.println("Recording started.");
    }

    private void stopRecording() {
        recording = false;
        System.out.println("Recording stopped.");
    }

    private void playRecording() {
        if (recordedEvents.isEmpty()) {
            System.out.println("No events recorded.");
            return;
        }

        System.out.println("Playing recording.");

        final double playbackSpeed = 5.0; // Adjust speed of the recording
        final int minDelay = 10; // Minimum delay of the events

        new Thread(() -> {
            long firstEventTime = recordedEvents.getFirst().getEventTime();

            for (int i = 0; i < recordedEvents.size(); i++) {
                InputEventWrapper eventWrapper = recordedEvents.get(i);

                // Calculate the time difference between events
                long timeDelta = (long) ((eventWrapper.getEventTime() - firstEventTime) / playbackSpeed);
                if (i > 0) {
                    long lastEventTime = recordedEvents.get(i - 1).getEventTime();
                    long actualDelay = Math.max(minDelay, (long) ((eventWrapper.getEventTime() - lastEventTime) / playbackSpeed));
                    try {
                        Thread.sleep(actualDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Simulate the event
                eventWrapper.simulateEvent(robot);
            }
            System.out.println("Playback finished.");
        }).start();
    }

    private void recordMouseEvent(MouseEvent event) {
        recordedEvents.add(new InputEventWrapper(event));
    }

    private void recordKeyEvent(KeyEvent event) {
        recordedEvents.add(new InputEventWrapper(event));
    }

    public static void main(String[] args) {
        new TestingToolGUI();
    }
}

// Wrapper class to handle both mouse and keyboard events
class InputEventWrapper {
    private final AWTEvent event;
    private final long eventTime;

    public InputEventWrapper(AWTEvent event) {
        this.event = event;
        this.eventTime = System.currentTimeMillis();
    }

    public long getEventTime() {
        return eventTime;
    }

    public void simulateEvent(Robot robot) {
        if (event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) event;
            int x = mouseEvent.getXOnScreen();
            int y = mouseEvent.getYOnScreen();

            switch (mouseEvent.getID()) {
                case MouseEvent.MOUSE_MOVED:
                case MouseEvent.MOUSE_DRAGGED:
                    robot.mouseMove(x, y);
                    break;
                case MouseEvent.MOUSE_PRESSED:
                    robot.mousePress(InputEvent.getMaskForButton(mouseEvent.getButton()));
                    break;
                case MouseEvent.MOUSE_RELEASED:
                    robot.mouseRelease(InputEvent.getMaskForButton(mouseEvent.getButton()));
                    break;
            }
        } else if (event instanceof KeyEvent) {
            KeyEvent keyEvent = (KeyEvent) event;

            switch (keyEvent.getID()) {
                case KeyEvent.KEY_PRESSED:
                    robot.keyPress(keyEvent.getKeyCode());
                    break;
                case KeyEvent.KEY_RELEASED:
                    robot.keyRelease(keyEvent.getKeyCode());
                    break;
            }
        }
    }
}
