package com.medivh.ip;

public class ChangeIpDemo {

    public static void main(String[] args) {
        do{
            NetUtil.changeIpAndWait();
        } while(!NetUtil.isNetConnected());
    }
}
