package by.gsu.epamlab.webshop.model;

import com.mysql.cj.exceptions.NumberOutOfRange;

public enum RoundMethod {
    FLOOR {
        double roundFunction(double d) {
            return Math.floor(d);
        }
    },
    CEIL {
        double roundFunction(double d) {
            return Math.ceil(d);
        }
    },
    ROUND {
        double roundFunction(double d) {
            return Math.round(d);
        }
    };

    abstract double roundFunction(double d);

    public int round(double roundedValue, int d) {
        int result = 0;
        if (roundedValue <= Integer.MAX_VALUE) {
            int tenPow = pow10(d);
            result = (int) roundFunction(roundedValue / tenPow) * tenPow;

        } else {
            throw new NumberOutOfRange("Summ is out of max integer range.");
        }
        return result;
    }

    private static int pow10(int d) {
        int n = 10;
        if (d == 0) {
            n = 1;
        }
        while (d > 1) {
            n *= 10;
            d--;
        }
        return n;
    }
}
