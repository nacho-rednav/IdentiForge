package com.example.identiforge.Model;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateHelper {

    /*
    * Function to get the current day in format dd-mm-yyyy
    * */
    public static String getCurrentDate(){
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = currentDate.format(formatter);

        return formattedDate;
    }

    /*
    * Function to get the timestamp of the provided day
    * Day in format dd-mm-yyyy
    * */
    public static long getTimeStamp(String day){
        LocalDate localDate = LocalDate.parse(day, java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        java.time.Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return instant.toEpochMilli();
    }

    /*
    * Format that receives the integer values of a date an returns
    * a String with format dd-mm-yyyy
    * */
    public static String formatDate(int day, int month, int year) {
        LocalDate localDate = LocalDate.of(year, month, day);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return localDate.format(formatter);
    }

    /*
    * Function that receives a String in format dd-mm-yyyy and returns which
    * day of the week it is : monday, tuesday...
    * */
    public static String getCurrentWekDay(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.parse(dateString, formatter);
        String weekday = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        return weekday.toLowerCase(Locale.ENGLISH);
    }

    /*
    * Functions that adds int add days to String selectedDay (dd-mm-yyyy) and returns
    * the result in format dd-mm-yyyy
    * */
    public static String addDays(String selectedDay, int add) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            LocalDate date = LocalDate.parse(selectedDay, formatter);
            LocalDate nextDay = date.plusDays(add);
            return nextDay.format(formatter);

        } catch (Exception e) {
            e.printStackTrace();
            return selectedDay;
        }
    }
}
