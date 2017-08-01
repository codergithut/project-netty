package io.java;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by lenovo on 2017/7/28.
 */
public class PlainOioServer {
    public void server(int port){
        final ServerSocket socket;
        try {
            socket = new ServerSocket(port);
            while(true){
                final Socket clientSocket = socket.accept();
                System.out.println("Accepted connection from " + clientSocket);
                new Thread(new Runnable() {
                    public void run() {
                        OutputStream out;
                        try {
                            out = clientSocket.getOutputStream();
                            out.write("Hi!\r\n".getBytes());
                            out.flush();
                            clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                clientSocket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }).run();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
