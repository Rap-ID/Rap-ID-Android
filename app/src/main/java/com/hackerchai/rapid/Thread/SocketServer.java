
package com.hackerchai.rapid.Thread;

/**
 * Created by hackerchai on 15-2-5.
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.hackerchai.rapid.Crypto;
import com.hackerchai.rapid.TokenParse;

public  class  SocketServer {
    Handler handler;
    private int PORT;
    private ServerSocket server = null;
    private String PAIR_KEY;
    private String  Username;
    private String  Password;
    private String Iccid;
    private String token_url;
    private String tokenContent;
    private String getToken;
    private String decryptMsg;
    private String encryptMsg;
    private String key="MKY%x!T%";
    public static final int SHOW_RESPONSE = 0;





    public SocketServer(String pair,String username,String password,String iccid,int PORT,Handler handler) {
        try {
            this.handler=handler;
            this.PORT=PORT;
            this.PAIR_KEY = pair;
            this.Username =username;
            this.Password = password;
            this.Iccid= iccid;
            server = new ServerSocket(PORT);

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
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            //Log.d("run", "receive");
            // TODO Auto-generated method stub
            while (!Thread.currentThread().isInterrupted()) {

                    try {
                        if ((msg = in.readLine()) != null) {
                            decryptMsg = Crypto.decrypt(msg,key);

                            if (decryptMsg.substring(0, 4).equals("PAIR")) {
                                if (decryptMsg.substring(4, 8).equals(PAIR_KEY)) {
                                    sendmsg("PAIROK" + PAIR_KEY);
                                    Message message = Message.obtain();
                                    String mes ="PAIR";
                                    message.obj=mes;
                                    message.what =SHOW_RESPONSE;
                                    handler.sendMessage(message);

                                } else {
                                    sendmsg("PAIRFAIL" + msg.substring(4, 8));


                                }
                            }
                            if (decryptMsg.substring(0, 4).equals("AUTH")) {
                                if (decryptMsg.substring(4, 8).equals(PAIR_KEY)) {
                                    HttpUtil token = new HttpUtil();
                                    token_url ="https://rapid.cotr.me/api/login";
                                    //token.setUrl(token_url);
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("username", Username);
                                    params.put("password", Password);
                                    params.put("iccid",Iccid);
                                    try {
                                        tokenContent = token.post(token_url,null,params);
                                        getToken = parseTokenWithJson(tokenContent);
                                        if (!getToken.equals("BAD_TOKEN")) {
                                            sendmsg("AUTHOK" + getToken);
                                            Message message = Message.obtain();
                                            String mes ="AUTH";
                                            message.obj=mes;
                                            message.what =SHOW_RESPONSE;
                                            handler.sendMessage(message);
                                        } else {
                                            sendmsg("AUTHFAIL");

                                        }

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } finally {
                                        if (token != null) {

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
            try {
                encryptMsg = Crypto.encrypt(sendMsg, key);

            } catch (Exception e) {
                e.printStackTrace();
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
            String err_code = tokenParse.getError().getId();
            String token = null;
            if (err_code.equals("0")) {
                token =tokenParse.getData();
            } else {
                token="BAD_TOKEN";
            }
            return token;

        }


    }
}




