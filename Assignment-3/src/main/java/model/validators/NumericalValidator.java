package model.validators;

public class NumericalValidator {
    /**
     * Check if the input matches the regex.
     * @param input the input String
     * @return bool value
     */
    public static boolean containsOnlyNumbers(String input) {
        return input.matches("[0-9]+");
    }
}
