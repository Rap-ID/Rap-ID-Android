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

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.hackerchai.wiauth.Thread.HttpRequest;
import com.hackerchai.wiauth.TokenParse;


public  class  SocketServer {
    private static final int PORT = 49161;
    private ServerSocket server = null;
    private String PAIR_KEY;
    Boolean isPair = false;
    private String  Username;
    private String  Password;
    private String token_url;
    private String tokenContent;
    private String getToken;


    public SocketServer(String pair,String username,String password) {
        try {
            this.PAIR_KEY = pair;
            this.Username =username;
            this.Password = password;
            server = new ServerSocket(PORT);
            //Log.d("recvMsg", "server start ...\n");
            Socket client = null;
            while (true) {
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
                sendmsg("RESPONSE_CONNECTED");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            //Log.d("run", "receive");
            // TODO Auto-generated method stub
            while (true) {
                try {

                    if ((msg = in.readLine()) != null) {
                        //Log.d("msg", msg);
                        // Log.d(msg.substring(0, 4),"test");
                        // Log.d(msg.substring(4, 8),"test");
                        if (msg.substring(0, 4).equals("PAIR")) {
                            // Log.d("PAIR_KEY",PAIR_KEY);
                            if (msg.substring(4, 8).equals(PAIR_KEY)) {
                                sendmsg("PAIROK" + PAIR_KEY);
                                isPair = true;

                            } else {
                                sendmsg("PAIRFAIL" +msg.substring(0,4));

                            }
                        }
                        if (msg.substring(0, 4).equals("AUTH") && isPair) {
                            HttpRequest token = new HttpRequest();
                            token_url = "http://wiauth.hackerchai.com/api/user/get_token/" + "account=" + Username + "&" + "password=" + Password;
                            Log.d("url", token_url);
                            try {
                                tokenContent = token.get(token_url);
                                getToken = parseTokenWithJson(tokenContent);
                                if (!getToken.equals("BAD_TOKEN")) {
                                    sendmsg("AUTHOK"+getToken);
                                }
                                else
                                {
                                    sendmsg("AUTHFAIL");
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                if (token != null) {
                                    token.shutdownHttpClient();
                                }

                            }
                        }
                            else{


                            }
                        } else {

                        }
                    }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }


        public void sendmsg(String sMesg) {

            String msg = sMesg;
           // Log.d("recvMsg", "recvMsg : \n" + msg);

            try {
                BufferedWriter br = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                br.write(msg+"\r\n");
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
                token="B";
            }
            return token;

        }
    }
}




