package com.hackerchai.wiauth.tcpService;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class tcpService extends Service {
    ServerSocket serverSocket;
    Thread serverThread = null;

    public tcpService() {
    }

    @Override
    public void onCreate() {
        this.serverThread = new Thread(new ServerThread());
        this.serverThread.start();

    }
    @Override
    public void onDestroy() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }






    class ServerThread implements Runnable {

        public void run() {
            Socket socket = null;
            try {
                serverSocket = new ServerSocket(49161);
                Log.d("ServerThread","Run");
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (!Thread.currentThread().isInterrupted()) {

                try {


                    socket = serverSocket.accept();

                    CommunicationThread commThread = new CommunicationThread(socket);
                    new Thread(commThread).start();
                    Log.d("ServerThread","Listen");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }







    class CommunicationThread implements Runnable {

        private Socket clientSocket;

        private BufferedReader input;

        public CommunicationThread(Socket clientSocket) {

            this.clientSocket = clientSocket;

            try {

                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {


            while (true) {

                try {

                    String read = input.readLine();
                    Log.d(read,"resd line");

                    if (read.substring(0,3).equals("PAIR")) {
                        Log.d(read.substring(0,3),"PAIR_IS_TRUE");
                        SharedPreferences getPairKey = getSharedPreferences("userAuth", MODE_PRIVATE);
                        int pairKey =getPairKey.getInt("PAIR_KEY",0);
                        String sPairKey = Integer.toString(pairKey);
                        Log.d(sPairKey,"RECEIVE_PAIR_KEY");
                        if(read.substring(4).equals(sPairKey)) {
                            BufferedWriter returnPairKey = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                            returnPairKey.write("PAIROK"+sPairKey);
                            returnPairKey.flush();

                        }
                        else
                        {
                            BufferedWriter wrongPairKey = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                            wrongPairKey.write("PAIR_ERROR_WRONG_KEY");
                            wrongPairKey.flush();
                        }

                    } else {
                        Thread.currentThread().interrupt();


                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}