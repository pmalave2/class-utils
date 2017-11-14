package com.general.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class Fecha {
    private Date fecha;
    private Locale locale = new Locale("es","VE");
    public Fecha() {
        super();
        this.fecha = new Date();
    }

    public Fecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Fecha sumarDias(Integer cantidad){
        Calendar selectedValue = Calendar.getInstance();
        selectedValue.setTime(fecha);
        selectedValue.add(Calendar.DAY_OF_MONTH, cantidad);
        fecha = selectedValue.getTime();

        return this;
    }
    
    public Fecha restarDias(Integer cantidad){
        Calendar selectedValue = Calendar.getInstance();
        selectedValue.setTime(fecha);
        selectedValue.add(Calendar.DAY_OF_MONTH, -cantidad);
        fecha = selectedValue.getTime();
        
        return this;
    }

    public Fecha restarAños(Integer cantidad){
        Calendar selectedValue = Calendar.getInstance();
        selectedValue.setTime(fecha);
        selectedValue.add(Calendar.YEAR, -cantidad);
        fecha = selectedValue.getTime();
        
        return this;
    }

    public Fecha añadirMinutos(Integer cantidad){
        Calendar selectedValue = Calendar.getInstance();
        selectedValue.setTime(fecha);
        selectedValue.add(Calendar.MINUTE, cantidad);
        fecha = selectedValue.getTime();
        
        return this;
    }

    public String getString(String patron){
        DateFormat outputFormat = new SimpleDateFormat(patron, locale);
        return outputFormat.format(fecha);
    }

    public String getJulianDate(){
        DateFormat outputFormat = new SimpleDateFormat("yyDDD", locale);
        return outputFormat.format(fecha);
    }

    public String getJulianDateDays(){
        DateFormat outputFormat = new SimpleDateFormat("DDD", locale);
        return outputFormat.format(fecha);
    }

    public int getDayOfWeek(){
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        return c.get(Calendar.DAY_OF_WEEK);
    }
    public int getDayOfMonth(){
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        return c.get(Calendar.DAY_OF_MONTH);
    }
    public static String getTimeFromMillis(Long millis){
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
        return hms;
    }

    public Fecha[] getFistAndLastDayOfMonthBefore(){
        Fecha[] fechas = new Fecha[2];

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        Date firstDayOfMonth = calendar.getTime();

        fechas[0] = new Fecha(firstDayOfMonth);

        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);

        Date lastDayOfMonth = calendar.getTime();

        fechas[1] = new Fecha(lastDayOfMonth);

        return fechas;
    }

    public void setToFistDayOfMonth(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        fecha = calendar.getTime();       
    }

    public static String parseDate(String date, String inPattern, String outPattern){
        DateFormat inputFormat = new SimpleDateFormat(inPattern);
        DateFormat outputFormat = new SimpleDateFormat(outPattern);
        try {
            return outputFormat.format(inputFormat.parse(date));
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parseDate(String date, String inPattern){
        DateFormat inputFormat = new SimpleDateFormat(inPattern);
        try {
            return inputFormat.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
        
    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
