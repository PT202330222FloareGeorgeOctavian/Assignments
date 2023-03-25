package model;

import java.util.*;

public class Operations {

    public Polynomial add(Polynomial p1, Polynomial p2) {
        Polynomial temp = new Polynomial();

        for (Integer exponent : p1.getHm().keySet()) {
            if (p2.getHm().containsKey(exponent)) {
                temp.addMon(exponent, p1.getHm().get(exponent) + p2.getHm().get(exponent));
            } else {
                temp.addMon(exponent, p1.getHm().get(exponent));
            }
        }

        for (Integer exponent : p2.getHm().keySet()) {
            if (!temp.getHm().containsKey(exponent)) {
                temp.addMon(exponent, p2.getHm().get(exponent));
            }
        }
        return temp;
    }

    public Polynomial minus(Polynomial p1, Polynomial p2) {
        Polynomial temp = new Polynomial();
        temp.getHm().putAll(p1.getHm());
        for (Integer exponent : p2.getHm().keySet()) {
            double p2Coefficient = p2.getHm().get(exponent);
            if (temp.getHm().containsKey(exponent)) {
                double p1Coefficient = temp.getHm().get(exponent);
                double resultCoefficient = p1Coefficient - p2Coefficient;
                if (resultCoefficient != 0.0) {
                    temp.getHm().put(exponent, resultCoefficient);
                } else {
                    temp.getHm().remove(exponent);
                }
            } else {
                temp.getHm().put(exponent, -p2Coefficient);
            }
        }
        return temp;
    }

    public Polynomial derivation(Polynomial polynomial) {
        Polynomial temp = new Polynomial();

        for (Integer power : polynomial.getHm().keySet()) {
            Double coefficient = polynomial.getHm().get(power);
            if (power == 0) {
                temp.addMon(0, 0.0);
            } else {
                temp.addMon(power - 1, coefficient * power);
            }
        }
        return temp;
    }

    public Polynomial integration(Polynomial polynomial) {
        Polynomial temp = new Polynomial();

        for (Integer power : polynomial.getHm().keySet()) {
            Double coefficient = polynomial.getHm().get(power);
            if (power == 0) {
                temp.addMon(1, coefficient);
            } else {
                temp.addMon(power + 1, coefficient / (power + 1));
            }
        }
        return temp;
    }

    public Polynomial multiply(Polynomial p1, Polynomial p2) {
        Polynomial temp = new Polynomial();
        for (Integer powerP1 : p1.getHm().keySet()) {
            double coefficientP1 = p1.getHm().get(powerP1);
            for (Integer powerP2 : p2.getHm().keySet()) {
                double coefficientP2 = p2.getHm().get(powerP2);
                int power = powerP1 + powerP2;
                double coefficient = coefficientP1 * coefficientP2;

                if (temp.getHm().containsKey(power)) {
                    double alreadyExistingCoefficient = coefficient + temp.getHm().get(power);
                    temp.addMon(power, alreadyExistingCoefficient);
                } else {
                    temp.addMon(power, coefficient);

                }
            }
        }
        return temp;
    }

    public ArrayList<Polynomial> divide(Polynomial p1, Polynomial p2) {
        ArrayList<Polynomial> returnedPolynomials = new ArrayList<>();
        Operations op = new Operations();
        Polynomial remainder = p1;
        Polynomial quotient = new Polynomial();

        if (remainder.getDegree() < p2.getDegree()) {
            quotient.addMon(0, 0.0);
        } else {
            while (remainder.getDegree() >= p2.getDegree()) {
                int higherPower = remainder.getDegree();
                int lowerPower = p2.getDegree();
                int power = higherPower - lowerPower;
                double coefficient = remainder.getHm().get(higherPower) / p2.getHm().get(lowerPower);

                quotient.addMon(power, coefficient);
                Polynomial temp = new Polynomial();
                temp.addMon(power, coefficient);

                temp = op.multiply(temp, p2);
                remainder = op.minus(remainder, temp);
            }
        }

        if (remainder.getHm().isEmpty()) {
            remainder.addMon(0, 0.0);
        }

        returnedPolynomials.add(quotient);
        returnedPolynomials.add(remainder);

        return returnedPolynomials;
    }
}
