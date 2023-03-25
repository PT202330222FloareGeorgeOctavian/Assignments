import model.Operations;
import model.Polynomial;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static model.Polynomial.evaluateInput;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OperationsTest {
    Operations operations;
    static Polynomial test1;
    static Polynomial test2;
    static Polynomial result;

    @Before
    public void setup(){
        operations = new Operations();
        test1 = new Polynomial();
        test2 = new Polynomial();
        result = new Polynomial();
    }

    @Test
    public void addTestPass(){
        test1 = evaluateInput("2.0x^3");
        test2 = evaluateInput("5.0x^4+3.0x^2");
        result = operations.add(test1, test2);
        System.out.println(result.getHm());
        HashMap<Integer, Double> testMap = new HashMap<>();
        testMap.put(2, 3.0);
        testMap.put(3, 2.0);
        testMap.put(4, 5.0);

        assertTrue(testMap.equals(result.getHm()));
    }

    @Test
    public void addTestFail(){
        test1 = evaluateInput("2x^3");
        test2 = evaluateInput("5x^4+3x^2");
        result = operations.add(test1, test2);

        HashMap<Integer, Double> testMap = new HashMap<>();
        testMap.put(3, 3.0);
        testMap.put(1, 2.0);
        testMap.put(5, 5.0);

        assertTrue(testMap.equals(result.getHm()));
    }

    @Test
    public void minusTestPass(){
        test1 = evaluateInput("5.0x^5-3.0x^0");
        test2 = evaluateInput("2.0x^3+1.0x^5");
        result = operations.minus(test1, test2);
        HashMap<Integer, Double> testMap = new HashMap<>();
        testMap.put(0,-3.0);
        testMap.put(5,4.0);
        testMap.put(3,-2.0);
        assertEquals(testMap, result.getHm());
    }

    @Test
    public void minusTestFail(){
        test1 = evaluateInput("5.0x^5-3.0x^0");
        test2 = evaluateInput("2.0x^3+1.0x^5");
        result = operations.minus(test1, test2);
        HashMap<Integer, Double> testMap = new HashMap<>();
        testMap.put(1,-3.0);
        testMap.put(2,6.0);
        testMap.put(-3,2.0);
        assertEquals(testMap, result.getHm());
    }

    @Test
    public void multiplyTestPass(){
        test1 = evaluateInput("3x^3+1x^0");
        test2 = evaluateInput("1x^2");
        result = operations.multiply(test1, test2);
        HashMap<Integer, Double> testMap = new HashMap<>();
        testMap.putAll(result.getHm());
        assertEquals(testMap, result.getHm());
    }

    @Test
    public void multiplyTestFail(){
        test1 = evaluateInput("3x^3+1x^0");
        test2 = evaluateInput("1x^2");
        result = operations.multiply(test1, test2);
        HashMap<Integer, Double> testMap = new HashMap<>();
        testMap.put(1, 3.0);
        testMap.put(2, 2.0);
        testMap.put(6, -5.0);

        assertEquals(testMap, result.getHm());
    }

    @Test
    public void divideTestPass(){
        test1 = evaluateInput("1x^3-2x^2+6x^1-5x^0");
        test2 = evaluateInput("1x^2-1x^0");
        ArrayList<Polynomial> divisionResult = operations.divide(test1, test2);
        HashMap<Integer, Double> quotient = new HashMap<>();
        quotient.put(1,1.0);
        quotient.put(0,-2.0);
        HashMap<Integer, Double> remainder = new HashMap<>();
        remainder.put(1,7.0);
        remainder.put(0, -7.0);

        assertEquals(quotient, divisionResult.get(0).getHm());
        assertEquals(remainder, divisionResult.get(1).getHm());
    }

    @Test
    public void divideTestFail(){
        test1 = evaluateInput("1x^1-2x^2+6x^1-5x^0");
        test2 = evaluateInput("1x^2-1x^0");
        ArrayList<Polynomial> divisionResult = operations.divide(test1, test2);
        HashMap<Integer, Double> quotient = new HashMap<>();
        quotient.put(2,3.0);
        quotient.put(0,-2.0);
        HashMap<Integer, Double> remainder = new HashMap<>();
        remainder.put(1,7.0);
        remainder.put(5, -7.0);

        assertEquals(quotient, divisionResult.get(0).getHm());
        assertEquals(remainder, divisionResult.get(1).getHm());
    }

    @Test
    public void derivationTestPass(){
        test1 = evaluateInput("5x^5+4x^2");
        result= operations.derivation(test1);
        HashMap<Integer, Double> testMap = new HashMap<>();
        testMap.put(4, 25.0);
        testMap.put(1, 8.0);
        assertEquals(testMap, result.getHm());
    }

    @Test
    public void derivationTestFall(){
        test1 = evaluateInput("5x^5+4x^2");
        result= operations.derivation(test1);
        HashMap<Integer, Double> testMap = new HashMap<>();
        testMap.put(2, 1.0);
        testMap.put(5, 2.0);
        assertEquals(testMap, result.getHm());
    }

    @Test
    public void integrationTestPass(){
        test1 = evaluateInput("5x^4+4x^1");
        result = operations.integration(test1);
        HashMap<Integer, Double> testMap = new HashMap<>();
        testMap.put(5, 1.0);
        testMap.put(2, 2.0);
        assertEquals(testMap, result.getHm());
    }

    @Test
    public void integrationTestFail(){
        test1 = evaluateInput("5x^4+4x^1");
        result = operations.integration(test1);
        HashMap<Integer, Double> testMap = new HashMap<>();
        testMap.put(5, 3.0);
        testMap.put(3, 1.3);
        assertEquals(testMap, result.getHm());
    }
}
