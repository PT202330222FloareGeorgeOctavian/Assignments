package model;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Polynomial{

    Map<Integer,Double> hm = new HashMap<>();

    public Polynomial(Integer power, Double coefficient){
        hm.put(power ,coefficient);
    }

    public Polynomial(Monomial m){
        hm.put(m.getPower(), m.getCoefficient());
    }

    public Polynomial() {

    }

    public void addMon(Integer power, Double coefficient){
        this.hm.put(power, coefficient);
    }

    private static boolean validateString(String input) {
        String regex = "^[\\d*^Xx\\+\\-.]+$";
        return input.matches(regex);
    }

    public static Polynomial evaluateInput(String poly){
        boolean valid = validateString(poly);
        if(!valid) throw new IllegalArgumentException("Wrong input!");
        Polynomial polynomial = new Polynomial();
        final String regex ="[-+]?(\\d*\\.\\d+|\\d+(?![.\\d]))[xX]\\^[0-9]+";
        final String digits = "[-+]?(?:\\d*\\.\\d+|\\d+)(?=[xX])|(?<=\\^)[-+]?[0-9]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher  = pattern.matcher(poly);
        while(matcher.find()){
            Pattern p = Pattern.compile(digits);
            Matcher m = p.matcher(matcher.group());
            String coefficient = "";
            String power = "";

            while (m.find()) {
                if(coefficient.equals("")){
                    coefficient = m.group();
                }else{
                    power = m.group();
                }
            }
            polynomial.addMon(Integer.valueOf(power),Double.valueOf(coefficient));
        }
        return polynomial;
    }

    public Map<Integer, Double> getHm() {
        return hm;
    }

    public int getDegree(){
        int max = 0;
        for(Integer exponent: hm.keySet()){
            if(exponent > max) {
                int result = Double.compare(hm.get(exponent), 0.0);
                if(result!=0) {
                    max = exponent;
                }
            }
        }
        return max;
    }

    private TreeMap<Integer, Double> retrieveSortedData(){
        return new TreeMap<>(this.hm);
    }

    public String toString(){
        TreeMap<Integer, Double> tm = this.retrieveSortedData();
        StringBuilder stringBuilder = new StringBuilder();
        boolean begin = true;
        for(int exponent: tm.descendingKeySet()){
            double coefficient = hm.get(exponent);
            int result = Double.compare(coefficient, 0.0);
            if(result == 0) continue;
            if(begin){
                begin = false;
            }
            else {
                if (result > 0) {
                    stringBuilder.append("+");
                }//
            }
            stringBuilder.append(coefficient);
            if(exponent == 1){
                stringBuilder.append("x");
            }else if(exponent != 0){
                stringBuilder.append("x^").append(exponent);
            }
        }

        if(stringBuilder.length() == 0){
            return "0";
        }else{
            return stringBuilder.toString();
        }
    }
}
