
package com.hackerchai.wiauth.Thread;

/**
 * Created by hackerchai on 15-2-5.
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.hackerchai.wiauth.Crypto;
import com.hackerchai.wiauth.R;
import com.hackerchai.wiauth.Thread.HttpRequest;
import com.hackerchai.wiauth.TokenParse;
import com.hackerchai.wiauth.tcpService.authService;
import com.hackerchai.wiauth.tcpService.tcpService;

public  class  SocketServer {
    private int PORT;
    private ServerSocket server = null;
    private String PAIR_KEY;
    Boolean isPair = false;
    private String  Username;
    private String  Password;
    private String token_url;
    private String tokenContent;
    private String getToken;
    private String decryptMsg;
    private String encryptMsg;
    private String key="MKY%x!T%";
    private Notification NF;
    private tcpService tcpser;
    private authService authser;





    public SocketServer(String pair,String username,String password,int PORT) {
        try {
            this.PORT=PORT;
            this.PAIR_KEY = pair;
            this.Username =username;
            this.Password = password;
            server = new ServerSocket(PORT);

            Socket client = null;
            while (true) {
                Log.d("client","socket server");
                client = server.accept();
                TcpThread tcpThread = new TcpThread(client);
                new Thread(tcpThread).run();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class TcpThread implements Runnable {
        final Socket socket;
        private BufferedReader in = null;
        private String msg = "";


        public TcpThread(Socket socket) {
            this.socket = socket;
            try {

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            //Log.d("run", "receive");
            // TODO Auto-generated method stub
            Log.d("run","run");

            while (!Thread.currentThread().isInterrupted()) {

                    try {
                        String temp = Crypto.encrypt("PAIR2843", key);
                        Log.d("key",temp);
                        if ((msg = in.readLine()) != null) {
                            decryptMsg = Crypto.decrypt(msg,key);
                            Log.d("decrypt",decryptMsg);
                            if (decryptMsg.substring(0, 4).equals("PAIR")) {
                                if (decryptMsg.substring(4, 8).equals(PAIR_KEY)) {
                                    sendmsg("PAIROK" + PAIR_KEY);
                                    //tcpser.updateNotification("配对成功！");



                                } else {
                                    sendmsg("PAIRFAIL" + msg.substring(4, 8));


                                }
                            }
                            if (decryptMsg.substring(0, 4).equals("AUTH")) {
                                if (decryptMsg.substring(4, 8).equals(PAIR_KEY)) {
                                    HttpRequest token = new HttpRequest();
                                    token_url = "http://wiauth.hackerchai.com/api/user/get_token/" + "account=" + Username + "&" + "password=" + Password;
                                    Log.d("url", token_url);
                                    try {
                                        tokenContent = token.get(token_url);
                                        getToken = parseTokenWithJson(tokenContent);
                                        if (!getToken.equals("BAD_TOKEN")) {
                                            sendmsg("AUTHOK" + getToken);
                                            //authser.updateNotification("授权成功！");
                                        } else {
                                            sendmsg("AUTHFAIL");

                                        }

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } finally {
                                        if (token != null) {
                                            token.shutdownHttpClient();
                                        }
                                    }
                                } else {
                                    sendmsg("AUTHFAIL");
                                }
                            }
                            else{
                            }
                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
            }

        }


        public void sendmsg(String sMesg) {

            String sendMsg = sMesg;
            Log.d("send",sendMsg);
            try {
                encryptMsg = Crypto.encrypt(sendMsg, key);
                Log.d("Encrypt",encryptMsg);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Encrypt Wrong","Wrong");
            }
            try {

                BufferedWriter br = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                br.write(encryptMsg+"\r\n");
                br.flush();
                    /*
                    pout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    pout.println(msg);
                    pout.flush();
                    */
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        private String parseTokenWithJson (String jsonData) {
            Gson gsonToken = new Gson();
            TokenParse tokenParse = gsonToken.fromJson(jsonData, TokenParse.class);
            String err_code = tokenParse.getErr_code();
            Log.d("err_code", err_code);
            String token = null;
            if (err_code.equals("0")) {
                token = tokenParse.data.getToken();
                Log.d("token",token);
            } else {
                token="BAD_TOKEN";
            }
            return token;

        }



    }
}




