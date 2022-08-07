package com.netty.transform;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class PlainOioServer {

    public void serve() throws IOException {
        final ServerSocket serverSocket = new ServerSocket(9022);
        try {
            final Socket accept = serverSocket.accept();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OutputStream out = accept.getOutputStream();
                        out.write("Hi".getBytes(Charset.forName("UTF-8")));
                        out.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } finally {
                        try {
                            accept.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
