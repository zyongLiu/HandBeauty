package com.liu.handbeauty.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Liu on 2016-06-06.
 */
public class TimeUtils {
    public static String convert(long mill){
        Date date=new Date(mill);
        String strs="";
        try {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            strs=sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strs;
    }
}
