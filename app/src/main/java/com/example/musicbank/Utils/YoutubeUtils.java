package com.example.musicbank.Utils;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class YoutubeUtils {
    public static final String ACTION_YOUTUBE_BROADCAST = "youtube_broadcast";
    public static final String PUTEXTRA_BROADCAST = "youtube_extra";
    public static final String ACTION_YOUTUBERECEIVEDFROMOTHERAPP = "youtube_received_from_other_app";
    public static final String PUTEXTRA_BROADCAST_RECEIVED = "youtube_extra_received";
    public static final String PUTEXTRA_BROADCAST_RECEIVED_KEYWORD = "youtube_extra_received_keyword";

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "B");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static String format(long value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix + " Views": (truncated / 10) + suffix + " Views";
    }

    public static String getDuration(String durationStr) {
        if(durationStr == null){
            return "0";
        }
        String time = durationStr.substring(2);
        long duration = 0L;
        String timeComplete = "";
        Object[][] indexs = new Object[][]{{"H", 3600}, {"M", 60}, {"S", 1}};
        for(int i = 0; i < indexs.length; i++) {
            int index = time.indexOf((String) indexs[i][0]);
            if(index != -1) {
                String value = time.substring(0, index);
                if(index > 2){
                    if(i ==1) {
                        timeComplete = "24:";
                    }
                    value = time.substring(2, index);
                }

                time = time.substring(value.length() + 1);
                if(i != 2){
                    if(value.length() >1) {
                        timeComplete = timeComplete + value + ":";
                    }else{
                        timeComplete = timeComplete + "0" + value + ":";
                    }
                }else{
                    if(value.length() >1) {
                        timeComplete = timeComplete + value;
                    }else{
                        timeComplete = timeComplete + "0" + value;
                    }
                }
            }else{
                if(i == 1){
                    timeComplete = "00:" + timeComplete;
                }else if(i==2){
                    timeComplete = timeComplete + "00";
                }
            }

        }
        return timeComplete;
    }
}
