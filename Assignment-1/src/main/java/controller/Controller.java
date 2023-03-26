package controller;

import model.*;
import view.Calculator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static model.Polynomial.evaluateInput;

public class Controller {
    private Operations op;
    private Calculator view;
    private ArrayList<String> operands;

    public Controller(Operations op, Calculator view){
        this.view = view;
        this.op = op;

        view.addButton(new AddListener());
        view.clearButton(new ClearListener());
        view.minusButton(new MinusListener());
        view.multiplyButton(new MultiplyListener());
        view.derivativeButton(new DerivativeListener());
        view.integrationButton(new IntegrationListener());
        view.divideButton(new DivisionListener());
    }

    public class AddListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                operands = view.getUserInput();
                Polynomial p1 = new Polynomial();
                p1 = evaluateInput(operands.get(0));
                Polynomial p2 = new Polynomial();
                p2 = evaluateInput(operands.get(1));
                view.setResult(op.add(p1, p2).toString());
            }catch(IllegalArgumentException ex){
                view.setResult("ERROR");
            }
        }
    }

    public class ClearListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                view.clear();
            }catch(IllegalArgumentException ex){
                view.setResult("ERROR");
            }
        }
    }

    public class MinusListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                operands = view.getUserInput();
                Polynomial p1 = new Polynomial();
                p1 = evaluateInput(operands.get(0));
                Polynomial p2 = new Polynomial();
                p2 = evaluateInput(operands.get(1));
                view.setResult(op.minus(p1, p2).toString());
            }catch(IllegalArgumentException ex){
                view.setResult("ERROR");
            }
        }
    }

    public class MultiplyListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                operands = view.getUserInput();
                Polynomial p1 = new Polynomial();
                p1 = evaluateInput(operands.get(0));
                Polynomial p2 = new Polynomial();
                p2 = evaluateInput(operands.get(1));
                view.setResult(op.multiply(p1, p2).toString());
            }catch(IllegalArgumentException ex){
                view.setResult("ERROR");
            }
        }
    }

    public class DivisionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                operands = view.getUserInput();
                Polynomial p1 = new Polynomial();
                p1 = evaluateInput(operands.get(0));
                Polynomial p2 = new Polynomial();
                p2 = evaluateInput(operands.get(1));
                ArrayList<Polynomial> result = op.divide(p1, p2);
                String output ="Quotient: "+ result.get(0).toString()+ " Remainder: "+result.get(1).toString();
                view.setResult(output);
            }catch(IllegalArgumentException ex){
                view.setResult("ERROR");
            }
        }
    }

    public class DerivativeListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String operand = view.getOperand1();
                Polynomial p1 = new Polynomial();
                p1 = evaluateInput(operand);
                view.setResult(op.derivation(p1).toString());
            }catch(IllegalArgumentException ex){
                view.setResult("ERROR");
            }
        }
    }

    public class IntegrationListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String operand = view.getOperand1();
                Polynomial p1 = new Polynomial();
                p1 = evaluateInput(operand);
                view.setResult(op.integration(p1).toString());
            }catch(IllegalArgumentException ex){
                view.setResult("ERROR");
            }
        }
    }
}
