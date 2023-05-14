package model.validators;

public class EmailValidator {
    public static boolean validate(String input) {
        return input.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}
