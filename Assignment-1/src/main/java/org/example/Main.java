package org.example;

import controller.Controller;
import model.Operations;
import view.Calculator;

public class Main
{
    public static void main( String[] args )
    {
        Controller controller = new Controller(new Operations(), new Calculator());
    }
}
