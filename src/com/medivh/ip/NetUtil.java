package com.medivh.ip;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class NetUtil {

    public synchronized static void changeIpAndWait() {

        try {
            long start = System.currentTimeMillis();

            stop();
            Thread.sleep(2000);
            start();
            int tryCount = 0;
            long startCheckTime = System.currentTimeMillis();
            while (!isNetConnected()) {
                Logger.i("Check Net");
                Thread.sleep(3000);
                if (tryCount++ > 10 || (System.currentTimeMillis() - startCheckTime > 30 * 1000)) {
                    stop();
                    start();
                    tryCount = 0;
                    startCheckTime = System.currentTimeMillis();
                }
            }

            Thread.sleep(1000);
            long cost = System.currentTimeMillis() - start;
            Logger.i("IP切换完成:" + "  耗时: " + cost);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stop() throws Exception {
        Logger.i("pppoe stop begin");
        execCmd("pppoe-stop");
        Logger.i("pppoe stop end");
    }


    public static void start() throws Exception {
        Logger.i("pppoe start begin");
        execCmd("pppoe-start");
        Logger.i("pppoe start end");
    }

    public static void execCmd(String cmd) throws Exception{
        Process pro = Runtime.getRuntime().exec(cmd);
        pro.waitFor();
        InputStream in = pro.getInputStream();
        BufferedReader read = new BufferedReader(new InputStreamReader(in));
        try{
            String line;
            while ((line = read.readLine()) != null) {
                Logger.i(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            in.close();
            read.close();
        }
    }

    public static boolean isNetConnected() {
        boolean conect = isConnect("www.baidu.com");
        Logger.i("isNetConnected:" + conect);
        return conect;
    }


    public static boolean isConnect(String host) {
        boolean connect = false;
        Runtime runtime = Runtime.getRuntime();
        Process process;

        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            Logger.i("os name:" + System.getProperty("os.name"));

            if (System.getProperty("os.name").contains("Windows")) {
                process = runtime.exec("ping -n 1 " + host);
            } else {
                process = runtime.exec("ping -c 1 " + host);
            }
            is = process.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }


            if (null != sb && !sb.toString().equals("")) {
                String logString = "";
                if (sb.toString().indexOf("TTL") > 0 || sb.toString().contains("1 received")) {
                    // 网络畅通
                    connect = true;
                } else {
                    // 网络不畅通
                    connect = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return connect;
    }


}
