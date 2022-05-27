package me.piggyster.api.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class NumberUtil {

    private final static TreeMap<Integer, String> romanMap = new TreeMap<>();

    static {
        romanMap.put(1000, "M");
        romanMap.put(900, "CM");
        romanMap.put(500, "D");
        romanMap.put(400, "CD");
        romanMap.put(100, "C");
        romanMap.put(90, "XC");
        romanMap.put(50, "L");
        romanMap.put(40, "XL");
        romanMap.put(10, "X");
        romanMap.put(9, "IX");
        romanMap.put(5, "V");
        romanMap.put(4, "IV");
        romanMap.put(1, "I");
    }

    public static String toRoman(int number) {
        int l = romanMap.floorKey(number);
        if(number == l) {
            return romanMap.get(number);
        }
        return romanMap.get(l) + toRoman(number - l);
    }

    private static final NavigableMap<BigDecimal, String> suffixes = new TreeMap<>();

    static {
        suffixes.put(BigDecimal.valueOf(1_000), "k");
        suffixes.put(BigDecimal.valueOf(1_000_000L), "M");
        suffixes.put(BigDecimal.valueOf(1_000_000_000L), "B");
        suffixes.put(BigDecimal.valueOf(1_000_000_000_000L), "T");
        suffixes.put(BigDecimal.valueOf(1_000_000_000_000_000L), "qd");
        suffixes.put(new BigDecimal("1000000000000000000"), "Qn");
        suffixes.put(new BigDecimal("1000000000000000000000"), "sx");
        suffixes.put(new BigDecimal("1000000000000000000000000"), "Sp");
        suffixes.put(new BigDecimal("1000000000000000000000000000"), "O");
        suffixes.put(new BigDecimal("1000000000000000000000000000000"), "N");
        suffixes.put(new BigDecimal("1000000000000000000000000000000000"), "de");
        suffixes.put(new BigDecimal("1000000000000000000000000000000000000"), "Ud");
        suffixes.put(new BigDecimal("1000000000000000000000000000000000000000"), "DD");
    }

    public static String withSuffix(BigDecimal value) {

        //if(value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if(value.compareTo(BigDecimal.ZERO) < 0) return "-" + format(value.negate());
        if(value.compareTo(BigDecimal.valueOf(1000)) < 0) return value.toString();

        Map.Entry<BigDecimal, String> entry = suffixes.floorEntry(value);
        BigDecimal divideBy = entry.getKey();
        String suffix = entry.getValue();


        BigDecimal truncated = value.divide(divideBy.divide(BigDecimal.TEN));
        return hasDecimal(truncated) ? (truncated.divide(BigDecimal.TEN)) + suffix : (truncated.divide(BigDecimal.TEN).toBigInteger()) + suffix;
    }

    public static String withSuffix(Number number) {
        return withSuffix(BigDecimal.valueOf(number.doubleValue()));
    }


    private static boolean hasDecimal(BigDecimal value) {
        return value.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0;
    }


    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("###,###.###");

    public static String format(Number number) {
        return DECIMAL_FORMAT.format(number);
    }

}
