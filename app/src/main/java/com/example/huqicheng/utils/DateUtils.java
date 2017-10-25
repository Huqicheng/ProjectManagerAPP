package com.example.huqicheng.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mark on 2017/10/25.
 */

public class DateUtils {
    public static Date parseStrToDate(String str, String model) {

        Date date = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(model);
            date = sdf.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
}
