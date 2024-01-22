package com.example.sm_project.Converter;

import androidx.room.TypeConverter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DataConverter {
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final TimeZone UTC_TIMEZONE = TimeZone.getTimeZone("UTC");

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Date fromString(String dateString) {
        if (dateString == null) {
            return null;
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @TypeConverter
    public static String dateToString(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        dateFormat.setTimeZone(UTC_TIMEZONE);
        return dateFormat.format(date);
    }
}
