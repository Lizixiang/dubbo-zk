package com.dubbo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author lizixiang
 * @since 2021/12/1
 */
//@ServerEndpoint(value = "/websocket/{userId}")
@ServerEndpoint(value = "/ws/1", configurator = GetHttpSessionConfigurator.class)
@Component
public class WebsocketServer {

    private static final Logger logger = LoggerFactory.getLogger(WebsocketServer.class);

    private static CopyOnWriteArraySet<WebsocketServer> wsSet = new CopyOnWriteArraySet<WebsocketServer>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //会话窗口的ID标识
    private String userId = "";

    @PostConstruct
    public void init() {
        logger.info("WebsocketServerEndpoint init success");
    }

    /**
     * 连接成功
     */
    @OnOpen
    public void onOpen(Session session) {
        logger.info("onOpen >> 连接成功, sessionId:{}", userId);
        try {
            this.session = session;
            // 将当前websocket对象存入到Set集合中
            wsSet.add(this);
            this.userId = userId;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     *  连接关闭
     */
    @OnClose
    public void onClose() {
        // 移除当前Websocket对象
        wsSet.remove(this);
        logger.info("onClose >> 连接关闭, sessionId:{}", this.userId);
    }

    /**
     * 收到客户端消息
     *
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("接收到sessionId:{}的信息：{}", userId, message);

        //发送信息
        for (WebsocketServer websocketServer : wsSet) {
            try {
                websocketServer.sendMessage(String.format("接收到sessionId：{}的信息：{}", userId, message));
            } catch (Exception e) {
                logger.error("发送失败，sessionId:{}, {}", userId,  e.getMessage());
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable e) {
        logger.error("onOpen >> 连接失败,sessionId:{}, {}", userId, e.getMessage());
    }

    /**
     * 推送消息
     *
     * @param message
     */
    public void sendMessage(String message) throws IOException {
        logger.info("sessionId:{}, 发送消息:{}", userId, message);
        this.session.getBasicRemote().sendText(message);
    }

}
