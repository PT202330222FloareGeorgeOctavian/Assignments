package presentation;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

/**
 * GUI class used for building a graphical interface used for inserting data in the database
 */
public class DataInsertionGUI<T> extends JFrame {
    private final Class<T> type;
    private JButton confirm = new JButton("Confirm");
    private JPanel[] panels;
    private JTextField[] tfs;
    public DataInsertionGUI(Class<T> type) {
        this.type = type;
        JPanel mainPanel = new JPanel();
        Field[] fields = type.getDeclaredFields();
        panels = new JPanel[fields.length];
        tfs = new JTextField[fields.length];

        for (int i = 1; i < type.getDeclaredFields().length; i++) {
            panels[i] = new JPanel();
            tfs[i] = new JTextField();
            tfs[i].setColumns(25);
            panels[i].add(new JLabel(fields[i].getName()));
            panels[i].add(tfs[i]);
            mainPanel.add(panels[i]);
        }

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel buttonPanel = new JPanel(); 
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(confirm);
        mainPanel.add(buttonPanel);

        setSize(300, 300);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }


    /**
     * Retrieve the text from the JTextFields.
     * @return a String[] of the input
     */
    public String[] getData(){
        String[] data = new String[tfs.length];
        for(int i=1;i<tfs.length;i++){
            data[i-1] = tfs[i].getText();
        }
        return data;
    }

    /**
     * retrieve the confirm button in order to set its ActionListener
     * @return the "confirm" JButton
     */
    public JButton getConfirm() {
        return confirm;
    }
}
