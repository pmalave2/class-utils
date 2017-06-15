package com.general.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Numero {
    public static final String PATTERN_PUNTO_DECIMAL = "#,##0.00";
    public static final String PATTERN_DECIMAL = "0.00";
    public static final Locale LOCALE_US = Locale.US;
    public static final Locale LOCALE_VE = new Locale("es", "VE");
    private final DecimalFormat formateador;

    public Numero() {
        formateador = new DecimalFormat(PATTERN_PUNTO_DECIMAL, DecimalFormatSymbols.getInstance(LOCALE_VE));
    }
    public Numero(String pattern) {
        formateador = new DecimalFormat(pattern, DecimalFormatSymbols.getInstance(LOCALE_VE));
    }
    public Numero(String pattern, Locale locale) {
        formateador = new DecimalFormat(pattern, DecimalFormatSymbols.getInstance(locale));
    }

    public String formatNumeroSinPunto(String str) {
        if (str == null) {
            return null;
        }
    	Double monto = Double.parseDouble(str);
    	
        return formatNumero(monto);
    }
    
    public String formatNumeroSinPunto(Integer str) {
        if (str == null) {
            return null;
        }
    	Double monto = str.doubleValue();
    	
        return formatNumero(monto);
    }
    
    public String formatNumeroSinPunto(Long str) {
        if (str == null) {
            return null;
        }
    	Double monto = str.doubleValue();
    	
        return formatNumero(monto);
    }
    
    public String formatNumero(String str) {
        if (str == null) {
            return null;
        }
    	Double monto = Double.parseDouble(str);

    	return formatNumero_(monto);
    }
    
    public String formatNumero_(Double str) {
        if (str == null) {
            return null;
        }
        
        //formateador.setRoundingMode(RoundingMode.CEILING);
        formateador.setMaximumFractionDigits(2);
        
        return formateador.format(str);
    }
    
    public String formatNumero(BigDecimal str) {
        if (str == null) {
            return null;
        }
        
        return formatNumero_(str.setScale(2, RoundingMode.HALF_UP).doubleValue());
    }

    public String formatNumero(Double str) {
        if (str == null) {
            return null;
        }
        
        str /= 100;
        
        //formateador.setRoundingMode(RoundingMode.CEILING);
        formateador.setMaximumFractionDigits(2);
        
        return formateador.format(str);
    }
    
    public static Long sum(String... str) {
        Long n = 0l;
        
        for (String s : str) {
            n += Long.parseLong(s);
        }
        
        return n;
    }
    
    public static BigDecimal sum(BigDecimal... augends) {
        BigDecimal bd = new BigDecimal(0d);
        
        for (java.math.BigDecimal a : augends){
            bd = bd.add(a);
        }
        
        return bd;
    }
}
