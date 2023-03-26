package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Calculator {

    private final JFrame frame;
    private JTextField polinom1 = new JTextField(32);
    private JTextField polinom2 = new JTextField(32);
    private JTextField result = new JTextField(32);

    private JButton add = new JButton("+");
    private JButton  minus= new JButton("-");
    private JButton  integrate= new JButton("Sdx");
    private JButton  derivate= new JButton("d/dx");
    private JButton multiply = new JButton("*");
    private JButton divide = new JButton("/");
    private JButton clear = new JButton("C");

    public Calculator(){
        frame = new JFrame("Polynomial Calculator");
        init();
        frame.setVisible(true);
        frame.setSize(300, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void init(){
        //panel pentru text
        JPanel panelText = new JPanel();

        JLabel p1 = new JLabel("Operand 1: ");
        JLabel p2 = new JLabel("Operand 2: ");
        JLabel res = new JLabel("Rezultat: ");
        result.setEditable(false);
        panelText.setLayout(new BoxLayout(panelText, BoxLayout.Y_AXIS));
        panelText.add(p1);
        panelText.add(polinom1);
        panelText.add(p2);
        panelText.add(polinom2);
        panelText.add(res);
        panelText.add(result);

        //panel pentru butoane
        JPanel panelButton = new JPanel();
        JPanel interm1 = new JPanel();
        JPanel interm2 = new JPanel();
        JPanel interm3 = new JPanel();
        JPanel interm4 = new JPanel();

        JPanel mainPanel = new JPanel();
        add.setPreferredSize(new Dimension(125,50));
        minus.setPreferredSize(new Dimension(125,50));
        integrate.setPreferredSize(new Dimension(125,50));
        derivate.setPreferredSize(new Dimension(125,50));
        divide.setPreferredSize(new Dimension(125,50));
        multiply.setPreferredSize(new Dimension(125,50));
        clear.setPreferredSize(new Dimension(250,50));
        interm1.add(add);
        interm1.add(minus);
        interm2.add(multiply);
        interm2.add(divide);
        interm3.add(integrate);
        interm3.add(derivate);
        interm4.add(clear);
        add.setBackground(new Color(255, 255, 153));
        minus.setBackground(new Color(255, 255, 153));
        multiply.setBackground(new Color(255, 255, 153));
        derivate.setBackground(new Color(255, 255, 153));
        divide.setBackground(new Color(255, 255, 153));
        integrate.setBackground(new Color(255, 255, 153));
        clear.setBackground(new Color(255, 255, 153));

        panelButton.setLayout(new BoxLayout(panelButton, BoxLayout.Y_AXIS));
        panelButton.add(interm1);
        panelButton.add(interm2);
        panelButton.add(interm3);
        panelButton.add(interm4);
        //main panel
        mainPanel.add(panelText);
        mainPanel.add(panelButton);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        frame.setContentPane(mainPanel);
    }


    public String getOperand1(){
        if(polinom1.getText().isEmpty()){
            return null;
        }
        return polinom1.getText();
    }

    public String getOperand2(){
        if(polinom2.getText().isEmpty()){
            return null;
        }
        return polinom2.getText();
    }

    public ArrayList<String> getUserInput(){
        ArrayList<String> operands = new ArrayList<>();
        operands.add(getOperand1());
        operands.add(getOperand2());
        return operands;
    }

    public void setResult(String res){
        result.setText(res);
    }

    public void clear(){
        result.setText("");
        polinom1.setText("");
        polinom2.setText("");
    }
    public void addButton(ActionListener a){
        add.addActionListener(a);
    }

    public void clearButton(ActionListener a){
        clear.addActionListener(a);
    }

    public void minusButton(ActionListener a){
        minus.addActionListener(a);
    }

    public void derivativeButton(ActionListener a){
        derivate.addActionListener(a);
    }

    public void integrationButton(ActionListener a){
        integrate.addActionListener(a);
    }

    public void multiplyButton(ActionListener a){
        multiply.addActionListener(a);
    }

    public void divideButton(ActionListener a){
        divide.addActionListener(a);
    }
}
