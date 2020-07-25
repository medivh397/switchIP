package com.medivh.ip;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Logger {

    public static void i(String msg){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String currentTime = sdf.format(new Date());
        System.out.println(currentTime+" Log: "+ msg);
    }
}

