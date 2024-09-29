import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 *
 *
 * @author john
 */
public class AutomatedTesting {

    // Placeholder for the file path
    private final Path filePath;
    // Placeholder for a list of test arrays [ input1, input2, expectedOutput ]
    private List<Integer[]> inputExpectedOutput;

    // Constructor
    public AutomatedTesting(Path filePath) {
        this.filePath = filePath;
        inputExpectedOutput = new ArrayList<>(); // Instantiate List
    }

    public void startTesting(ArrayList<AWTEvent> listener) {
        /*
         * using a stream to read the file line by line, separate line tokens by
         * using ' ' as a delimiter and the looping through the map and pass
         * each new array element to addToNumberList. Each element is parsed to
         * an Integer.
         */
        try(Stream<String> st = Files.lines(filePath)) {
            st.map(l -> l.split(" "))
                    .forEach(x -> addToNumberList(
                            Integer.parseInt(x[0]),
                            Integer.parseInt(x[1]),
                            Integer.parseInt(x[2])));
        } catch (IOException e) {

        }
        runList(listener);
    }


    private void runList(ArrayList<AWTEvent> listener) {
        // Casts GUI objects to their respective type
        JTextField jt1 = (JTextField) listener.get(0).getSource();
        JTextField jt2 = (JTextField) listener.get(1).getSource();
        JTextField jt3 = (JTextField) listener.get(2).getSource();
        JButton jb = (JButton) listener.get(3).getSource();
        String out = "";
        // Loop through test data
        for(Integer[] x : inputExpectedOutput) {
            jt1.setText(x[0].toString()); // set text field 1
            jt2.setText(x[1].toString()); // set text field 2
            jb.doClick(); // clicks the button
            Integer ans = Integer.parseInt(jt3.getText()); // Placeholder for GUI answer
            out += "a: " + x[0] + ", b: " +
                    x[1] + ", gui out: " + ans+ ", expected: " + x[2] +
                    " -----> Pass: " + ans.equals(x[2]) + "\n";
        }
        // Prints results to console
        System.out.println(out);
        JOptionPane.showMessageDialog(new JFrame(), out);
    }

    // Helper class to add Integer arrays to the List
    private void addToNumberList(Integer a, Integer b, Integer c) {
        inputExpectedOutput.add(new Integer[]{a, b, c});
    }
}
