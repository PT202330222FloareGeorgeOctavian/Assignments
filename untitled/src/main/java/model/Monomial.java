package data;

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

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public Monomial addMon(Monomial m1, Monomial m2){
        Monomial temp = new Monomial(0.0,0);
        if (m1.getPower() == m2.getPower()){
            temp.setCoefficient(m1.getCoefficient()+m2.getCoefficient());
            temp.setPower(m1.getPower());
        }
        return temp;
    }

}
