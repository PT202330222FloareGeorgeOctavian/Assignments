import model.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class PolynomialTest {
    @ParameterizedTest
    @MethodSource("polynomialProvider")
    public void polynomialProviderTest(String argument){
        assertDoesNotThrow( () -> {
            Polynomial.evaluateInput(argument);
        });

    }

    private static Stream<String> polynomialProvider(){
        return  Stream.of("1.2x^2+2.3x^0",
                "2x^a+2x^1",
                "a");
    }
}
