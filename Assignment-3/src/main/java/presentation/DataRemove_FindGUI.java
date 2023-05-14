package presentation;

import model.validators.NumericalValidator;

import javax.swing.*;

/**
 * Build GUI for deleting / finding an entry by its id
 */
public class DataRemove_FindGUI extends JOptionPane{
    private String input;
    public DataRemove_FindGUI(String type) {
        if(type.equals("delete")) {
            input = showInputDialog(null, "Enter the id", "Delete", JOptionPane.QUESTION_MESSAGE);
        }else{
            input = showInputDialog(null, "Enter the id", "Find", JOptionPane.QUESTION_MESSAGE);

        }
    }

    /**
     * Getter for the id.
     * If the input contains only numbers, its valid
     * @return the id of the entry
     */
    public int getData(){
        if(NumericalValidator.containsOnlyNumbers(input)) {
            return Integer.parseInt(input);
        }
        return -1;
    }

}
