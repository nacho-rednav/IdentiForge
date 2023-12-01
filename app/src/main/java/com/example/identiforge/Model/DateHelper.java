package com.example.identiforge.Model;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateHelper {

    public static String getCurrentDate(){
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = currentDate.format(formatter);

        return formattedDate;
    }

    public static long getTimeStamp(String day){
        LocalDate localDate = LocalDate.parse(day, java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        java.time.Instant instant = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
        return instant.toEpochMilli();
    }

    public static String formatDate(int day, int month, int year) {
        LocalDate localDate = LocalDate.of(year, month, day);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return localDate.format(formatter);
    }

    public static String getCurrentWekDay(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.parse(dateString, formatter);
        String weekday = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        return weekday.toLowerCase(Locale.ENGLISH);
    }

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
