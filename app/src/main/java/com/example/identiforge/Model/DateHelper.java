package com.example.identiforge.Model;

import java.time.LocalDate;
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

    public static String getCurrentWekDay(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.parse(dateString, formatter);
        String weekday = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        return weekday.toLowerCase(Locale.ENGLISH);
    }

    public static String nextDay(String selectedDay) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            LocalDate date = LocalDate.parse(selectedDay, formatter);
            LocalDate nextDay = date.plusDays(1);
            return nextDay.format(formatter);

        } catch (Exception e) {
            e.printStackTrace();
            return selectedDay;
        }
    }

    public static String prevDay(String selectedDay) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            LocalDate date = LocalDate.parse(selectedDay, formatter);
            LocalDate nextDay = date.plusDays(-1);
            return nextDay.format(formatter);

        } catch (Exception e) {
            e.printStackTrace();
            return selectedDay;
        }
    }
}
