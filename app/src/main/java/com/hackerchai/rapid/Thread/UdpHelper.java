package com.hackerchai.rapid.Thread;


import android.net.wifi.WifiManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by 轶晟 on 2015/1/25.
 */
public class UdpHelper  implements Runnable {
    //指示监听线程是否终止
    public    Boolean IsThreadDisable = false;
    public String broadcastUsername;


    private static WifiManager.MulticastLock lock;
    InetAddress mInetAddress;
    public UdpHelper(WifiManager manager,String userName) {
        this.lock= manager.createMulticastLock("UDPwifi");
        this.broadcastUsername=userName;
    }

    public static void send(String message) {

        int server_port = 49160;

        DatagramSocket s = null;
        try {
            s = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        InetAddress local = null;
        try {
            local = InetAddress.getByName("255.255.255.255");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        int msg_length = message.length();
        byte[] messageByte = message.getBytes();
        DatagramPacket p = new DatagramPacket(messageByte, msg_length, local,
                server_port);
        try {

            s.send(p);
            s.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        while( true ){
            try {
                send(broadcastUsername);
                Thread.sleep(200);
            } catch (Exception e) {
                System.out.println( e.toString());
            }
            }
        }
    }



