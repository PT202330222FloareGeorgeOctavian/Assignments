package view;

import business.SimulationManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SimulationFrame extends JFrame {
    JLabel label1 = new JLabel("Servers: ");
    JLabel label2 = new JLabel("Clients: ");
    JLabel label3 = new JLabel("Simulation time: ");
    JLabel label4 = new JLabel("Min Arrival Time");
    JLabel label5 = new JLabel("Max Arrival Time");
    JLabel label6 = new JLabel("Min Service Time");
    JLabel label7 = new JLabel("Max Service Time");

    JButton button = new JButton("Start simulation");

    JTextField textField1 = new JTextField(6);
    JTextField textField2 = new JTextField(6);
    JTextField textField3 = new JTextField(6);
    JTextField textField4 = new JTextField(6);
    JTextField textField5 = new JTextField(6);
    JTextField textField6 = new JTextField(6);
    JTextField textField7 = new JTextField(6);

    private ArrayList<Integer> values = new ArrayList<>();

    public SimulationFrame() {
        super("Queue Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        label1.setFont(new Font("SansSerif", Font.BOLD, 14));
        label2.setFont(new Font("SansSerif", Font.BOLD, 14));
        label3.setFont(new Font("SansSerif", Font.BOLD, 14));
        label4.setFont(new Font("SansSerif", Font.BOLD, 14));
        label5.setFont(new Font("SansSerif", Font.BOLD, 14));
        label6.setFont(new Font("SansSerif", Font.BOLD, 14));
        label7.setFont(new Font("SansSerif", Font.BOLD, 14));

        textField1.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textField2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textField3.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textField4.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textField5.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textField6.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textField7.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(label1);
        panel.add(textField1);
        panel.add(label2);
        panel.add(textField2);
        panel.add(label3);
        panel.add(textField3);
        panel.add(label4);
        panel.add(textField4);
        panel.add(label5);
        panel.add(textField5);
        panel.add(label6);
        panel.add(textField6);
        panel.add(label7);
        panel.add(textField7);

        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.addActionListener(e -> {
            try {
                values.add(Integer.parseInt(textField1.getText()));
                values.add(Integer.parseInt(textField2.getText()));
                values.add(Integer.parseInt(textField3.getText()));
                values.add(Integer.parseInt(textField4.getText()));
                values.add(Integer.parseInt(textField5.getText()));
                values.add(Integer.parseInt(textField6.getText()));
                values.add(Integer.parseInt(textField7.getText()));
            }catch (NumberFormatException ex){
                error();
            }

            SimulationManager simulator = new SimulationManager(this);
            button.setEnabled(true);
            Thread sim = new Thread(simulator);
            sim.start();
            button.setText("Simulation Running");
            button.setBackground(new Color(255, 0, 50));
        });
        add(panel, BorderLayout.CENTER);
        add(button, BorderLayout.SOUTH);
        setSize(600, 200);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public ArrayList<Integer> retrieveData() {
        return values;
    }

    public void endSimulation() {
        button.setEnabled(false);
        button.setText("Simulation Ended");
        button.setBackground(Color.GREEN);
    }

    public void error() {
        button.setEnabled(false);
        button.setText("ERROR");
        button.setBackground(Color.YELLOW);
    }
}
