package model;

public class Monomial {
    private double coefficient;
    private int power;

    public Monomial(double coefficient, int power){
        this.coefficient = coefficient;
        this.power = power;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public int getPower() {
        return power;
    }

}
