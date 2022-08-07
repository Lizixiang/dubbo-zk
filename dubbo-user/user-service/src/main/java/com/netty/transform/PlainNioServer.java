package com.netty.transform;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class PlainNioServer {

    public void serve() throws IOException {
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        ServerSocket socket = channel.socket();
        socket.bind(new InetSocketAddress(9022));
        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_ACCEPT);
        final ByteBuffer message = ByteBuffer.wrap("Hi".getBytes());
        for (;;) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                try {
                    if (selectionKey.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, message.duplicate());
                        System.out.println("accepted client:" + client);
                    }
                    if (selectionKey.isWritable()) {
                        SocketChannel client = (SocketChannel) selectionKey.channel();
                        ByteBuffer attachment = (ByteBuffer) selectionKey.attachment();
                        while (attachment.hasRemaining()) {
                            if (client.write(attachment) == 0) {
                                break;
                            }
                            client.close();
                        }
                    }
                } catch (IOException e) {
                    selectionKey.cancel();
                    selectionKey.channel().close();
                }
            }
        }
    }

}
