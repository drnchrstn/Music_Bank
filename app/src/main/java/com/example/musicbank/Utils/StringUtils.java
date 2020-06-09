package com.example.musicbank.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class StringUtils {


        public static String timestampToDate(String timestamp){
            long currentTimeMillis = Long.parseLong(timestamp);
            Calendar cal = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat();
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date resultdate = new Date(currentTimeMillis);
            return dateFormat.format(resultdate);
        }


}
