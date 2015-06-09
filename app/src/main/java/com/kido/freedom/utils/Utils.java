package com.kido.freedom.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Utils {
    public static boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
        } catch (ParseException e) {
            return false;
        }
        if (!sdf.format(testDate).equals(date)) {
            return false;
        }
        return true;
    }

    public static Date getDateFromString(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
        if (!sdf.format(testDate).equals(date)) {
            return null;
        }
        return testDate;
    }

    public static String getStrDateFromJsonDate(String jDate) {
        if (jDate == null) {
            return "";
        } else {
            String JSONDateToMilliseconds = "\\/(Date\\((.*?)(\\+.*)?\\))\\/";
            Pattern pattern = Pattern.compile(JSONDateToMilliseconds);
            Matcher matcher = pattern.matcher(jDate);
            String result = matcher.replaceAll("$2");
            return getDateFromTicks(Long.valueOf(result));
        }
    }

    public static long getTicksFromDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
        } catch (ParseException e) {
            return Long.parseLong(null);
        }
        if (!sdf.format(testDate).equals(date)) {
            return Long.parseLong(null);
        }
        return testDate.getTime();
    }

    public static String getFormatedTicksFromDate(String date) {
        String testdate;
        testdate = Long.toString(getTicksFromDate(date));
        return "/Date(" + testdate + "+0300)/";
    }

    public static String getDateFromTicks(long date) {
        Date testDate = new Date(date);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String sd = df.format(testDate);
        return sd;
    }

    public static boolean isNetworkAvailable(Context mCtx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mCtx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
