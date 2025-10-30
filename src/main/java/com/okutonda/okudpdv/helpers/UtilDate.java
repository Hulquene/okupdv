/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.okutonda.okudpdv.helpers;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 *
 * @author kenny
 */
public class UtilDate {

    public static String getDateNow() {
//        String dateFormated = "";
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyy");
        return format.format(now);
//        return dateFormated;
    }

    public static String formatDateNow(Date now) {
//        String dateFormated = "";
//        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyy");
        return format.format(now);
//        return dateFormated;
    }

    public static String getDateTimeNow() {
//        String dateFormated = "";
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/M/yyy HH:mmm:ss");
        return format.format(now);
//        return dateFormated;
    }
    
    public static String getDateTime(Date now) {
//        String dateFormated = "";
//        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/M/yyy HH:mmm:ss");
        return format.format(now);
//        return dateFormated;
    }

    public static String getDateFormated() {
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/M/yyyTHH:mmm:ss");
        return format.format(now);
    }

    public static int getYear() {
        return LocalDate.now().getYear();
    }

    public static String getDateFormatISO8601() {
//        Date now = new Date();
        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        String formattedDate = now.format(formatter);
        return formattedDate;
//        SimpleDateFormat format = new SimpleDateFormat("dd/M/yyyTHH:mmm:ss");
//        return format.format(now);
    }

    public static String FormatDateISO8601(ZonedDateTime now) {
//        Date now = new Date();
//        ZonedDateTime now = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        String formattedDate = now.format(formatter);
        return formattedDate;
//        SimpleDateFormat format = new SimpleDateFormat("dd/M/yyyTHH:mmm:ss");
//        return format.format(now);
    }

    public static String getFormatDataNow() {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return date.format(formato);
    }

    public static String formatData(LocalDateTime date) {
//        LocalDateTime agora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return date.format(formato);
    }
}
